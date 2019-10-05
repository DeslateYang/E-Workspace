package com.des.butler.tools;

import java.util.List;

import com.des.butler.ItemEdit;
import com.des.butler.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ViewHolder") public class TdpAdapter extends ArrayAdapter<TDPitem> {

	private int resourceId;
	private LinearLayout Manage;
	private View view;
	private TDPitem tdpitem;
	private MyClickListener mListener1;
	private MyClickListener mListener2;
	private Button cancelManage;
	private Button deleteItem;
	public ImageView pic_image;
	private ButlerDataBase dbHelperForButler;
	private Context Context;
	
	public TdpAdapter(Context context,int textViewResourceId,List<TDPitem> objects,MyClickListener listener1,MyClickListener listener2){
		super(context,textViewResourceId,objects);
		Context=context;
		resourceId=textViewResourceId;
		mListener1 = listener1;
		mListener2 = listener2;
		dbHelperForButler=new ButlerDataBase(context,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		tdpitem=getItem(position);
		
		view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		Manage=(LinearLayout)view.findViewById(R.id.manage_item);
		TextView log=(TextView)view.findViewById(R.id.log);
		TextView title=(TextView)view.findViewById(R.id.t_item);
		TextView briefDetail=(TextView)view.findViewById(R.id.d_item);
		pic_image=(ImageView)view.findViewById(R.id.p_item);
		cancelManage=(Button)view.findViewById(R.id.cancel_manage);
		deleteItem=(Button)view.findViewById(R.id.delete_item);
		
		log.setText(tdpitem.getLog());
		title.setText(tdpitem.getTitle());
		briefDetail.setText(tdpitem.getBriefDetail());
		setImage(tdpitem.getimageId());
		
		cancelManage.setOnClickListener(mListener1);
		deleteItem.setOnClickListener(mListener2);
		
		if(tdpitem.getState()==TDPitem.SHOW){
			Manage.setVisibility(View.GONE);
		}else if(tdpitem.getState()==TDPitem.MANAGE){
			Manage.setVisibility(View.VISIBLE);
		}
		
		return view;
    }

	public static abstract class MyClickListener implements OnClickListener {
        
        @Override
        public void onClick(View v) {
            myOnClick(v);
        }
        public abstract void myOnClick(View v);
    }
	public void setImage(int picId){
		if(picId==0){
			pic_image.setImageResource(R.drawable.none);
		}else{
			String path=pathAndPicId(picId,"none");
			//Bitmap bm=BitmapFactory.decodeFile(path);
			try{
				Bitmap bm = com.des.butler.tools.ImageUtils.getSmallBitmap(path,120,40);
				pic_image.setImageBitmap(bm);
			}catch(Exception e){
				Bitmap bitmap = Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
		        bitmap.eraseColor(Color.parseColor("#84C1FF")); // 填充颜色
		        Canvas canvas = new Canvas(bitmap);
		        Paint paint = new Paint();
		        paint.setTextSize(50);
		        paint.setColor(Color.WHITE);
		        paint.setFlags(100);
		        paint.setStyle(Paint.Style.FILL); //用于设置字体填充的类型
		        canvas.drawText("图片丢失",100,175,paint);
		        pic_image.setImageBitmap(bitmap);
		    }
			
		}
		
	}
	public String pathAndPicId(int PicId,String none){
		String path=null;
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		Cursor cursor=db.query("Pictures",null, "id=?", new String[]{""+PicId}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				path=cursor.getString(cursor.getColumnIndex("path"));
			}while(cursor.moveToNext());
		}
		if(path!=null){
		    return path;
		}else{
			return none;
		}
	}


}
