package com.des.butler.tools;


import android.widget.ImageView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public abstract class SwZoomDragImageView extends ImageView implements View.OnTouchListener{
    private static final String TAG = SwZoomDragImageView.class.getSimpleName();
    private boolean support_touch = true;//֧�ִ����¼�
 
    private int mode = 0;// ��ʼ״̬
    private static final int MODE_DRAG = 1;//ƽ��
    private static final int MODE_ZOOM = 2;//����
 
    private static final float MAX_SCALE = 4f, MIN_SCALE = 1f;//���Ŵ�������С�Ŵ���
    float total_scale = MIN_SCALE , current_scale;//total_scale���ŷ�Χ2-1����С��1�ص���1��������2�ص���2
 
    private Matrix matrixNow = new Matrix();
    private Matrix matrixBefore = new Matrix();
    private Matrix mInitializationMatrix = new Matrix();//��ʼ����ֵ
 
    private PointF actionDownPoint = new PointF();//�����
    private PointF dragPoint = new PointF();//ƽ�Ƶ�
    private PointF startPoint = new PointF();//������
    private PointF mInitializationScalePoint = new PointF();//��ʼ���ŵ�
    private PointF mCurrentScalePoint = new PointF(0, 0);//��ǰ���ŵ�
    private float startDis;//������ʼ����
    /** ������ָ���м�� */
    private PointF midPoint = new PointF(0,0);
 
    public SwZoomDragImageView(Context context) {
        this(context, null);
    }
    public SwZoomDragImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SwZoomDragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }
    private void initData() {
        if (support_touch) {
            setOnTouchListener(this);
        }
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SwZoomDragImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] viewLocation = new int[2];
                SwZoomDragImageView.this.getLocationInWindow(viewLocation);
                int viewX = viewLocation[0]; // x ���ꣻ��bug����viewpage�С�
                int viewY = viewLocation[1]; // y ����
                mInitializationScalePoint.set( SwZoomDragImageView.this.getWidth() / 2, viewY + SwZoomDragImageView.this.getHeight() / 2);//��ʼ������λ��
                Log.i("yangxun", "�ؼ� ��" + mInitializationScalePoint.x + "�ߣ�" + mInitializationScalePoint.y);
            }
        });
    }
 
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);//��ջ���
        if (matrixNow != null) {
            canvas.concat(matrixNow);
//            canvas.setMatrix(matrixNow);//��ʾ������
        }
        super.onDraw(canvas);
    } @Override
    public void setImageMatrix(Matrix matrix) {
        matrixNow.set(matrix);
        invalidate();
    }
 
 
    public void resetImageMatrix() {
        this.matrixNow.set(mInitializationMatrix);
        invalidate();
    }
    //��С������ֵ
    private void resetToMinStatus(){
        mCurrentScalePoint.set(0, 0);
        total_scale = MIN_SCALE;
    }
    //���������ֵ
    private void resetToMaxStatus(){
        total_scale = MAX_SCALE;
    }
    public float getInitializationBitmapHeight(){
        return getHeight()*total_scale;
    }
    public float getInitializationBitmapWidth(){
        return getWidth()*total_scale;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (total_scale != 1) {
            getParent().requestDisallowInterceptTouchEvent(true);//�����¼���������
        }
        /** ͨ�������㱣������λ MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN://��ָ����
                mode = MODE_DRAG;
                matrixBefore.set(getImageMatrix());
                matrixNow.set(getImageMatrix());
                dragPoint.set(event.getX(), event.getY());
                actionDownPoint.set(event.getX(), event.getY());
                break;
 
            case MotionEvent.ACTION_POINTER_DOWN://˫ָ����
                getParent().requestDisallowInterceptTouchEvent(true);//�����¼���������
                mode = MODE_ZOOM;
                startPoint.set(event.getX(), event.getY());
                startDis = distance(event);
                /** ����������ָ����м�� */
                if (startDis > 10f) {
                    //��¼��ǰImageView�����ű���
                    matrixBefore.set(getImageMatrix());
                    matrixNow.set(getImageMatrix());
                }
                break;
 
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG && total_scale > 1) {
                    float dx = event.getX() - dragPoint.x;
                    float dy = event.getY() - dragPoint.y;
                    dragPoint.set(event.getX(), event.getY());
                    imgTransport(dx, dy);
                } else if (mode == MODE_ZOOM) {//����
                    float endDis = distance(event);
                    midPoint = mid(event);
                    if (endDis > 10f) {
                        current_scale = endDis / startDis;//���ű���
                        total_scale *= current_scale;
                        matrixNow.postScale(current_scale, current_scale, midPoint.x, midPoint.y);
                        invalidate();
                    }
                    startDis = endDis;
                }
                break;
 
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);//�����¼�����ȡ������
                mode = 0;
                if(mode == MODE_DRAG)
                    checkClick(event.getX(),event.getY(), actionDownPoint.x, actionDownPoint.y);
                break;
 
            case MotionEvent.ACTION_POINTER_UP:
                checkZoomValid();
                mode = 0;
                break;
 
        }
        return true;
    }
    /**
     * ƽ��ͼƬ
     * @param x
     * @param y
     */
    public void imgTransport(float x,float y){
        mCurrentScalePoint.set(mCurrentScalePoint.x + x, mCurrentScalePoint.y + y);
        if (mCurrentScalePoint.x >= ((total_scale - 1) * getWidth()) / 2) {
            mCurrentScalePoint.x = ((total_scale - 1) * getWidth()) / 2;
            x = 0;
        } else {
            if (mCurrentScalePoint.x <= -((total_scale - 1) * getWidth()) / 2) {
                mCurrentScalePoint.x = -((total_scale - 1) * getWidth()) / 2;
                x = 0;
            }
        }
        if (mCurrentScalePoint.y >= ((total_scale - 1) * getHeight()) / 2) {
            mCurrentScalePoint.y = ((total_scale - 1) * getHeight()) / 2;
            y = 0;
        } else {
            if (mCurrentScalePoint.y <= -((total_scale - 1) * getHeight()) / 2) {
                mCurrentScalePoint.y = -((total_scale - 1) * getHeight()) / 2;
                y = 0;
            }
        }
        Log.i(TAG, "mCurrentScalePoint.x:" + mCurrentScalePoint.x + "   x:" + x);
        matrixNow.postTranslate(x, y);
        invalidate();
    }
    private boolean checkZoomValid() {
        if(mode == MODE_ZOOM){
            if(total_scale>MAX_SCALE){
                resetToMaxStatus();
                matrixNow.set(mInitializationMatrix);
                matrixNow.postScale(MAX_SCALE, MAX_SCALE, mInitializationScalePoint.x, mInitializationScalePoint.y);
                matrixNow.postTranslate(mCurrentScalePoint.x, mCurrentScalePoint.y);
                invalidate();
                return false;
            }else if(total_scale<MIN_SCALE){
                resetToMinStatus();
                matrixNow.set(mInitializationMatrix);
                invalidate();
                return false;
            }
            invalidate();
        }
        return true;
    }
 
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
 
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }
 
    boolean checkClick(float last_x,float last_y,float now_x,float now_y){
        float x_d = Math.abs(last_x - now_x);
        float y_d = Math.abs(last_y - now_y);
        if(x_d<10 && y_d<10){//����¼�
            //�������¼�
        }
        if (total_scale == 1) {
            matrixNow.set(mInitializationMatrix);
            invalidate();
        }
        return false;
    }
    @Override
    public Matrix getImageMatrix() {
        return matrixNow;
    }
    public Matrix getBeforeImageMatrix() {
        return matrixBefore;
    }
}
