package com.des.butler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;
import com.des.butler.tools.MClickableSpan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.Toast;

public class ItemEdit extends ButlerActivity {
	
	private int hour;
	private int minute;
	private int Id=0;
	private String title;
	private int year;
	private int month;
	private int day;
	private String Time;
	private int length;
	private String detail;
	private int imageId;
	private int alarm;
	private int setCover=0;
	private int width=120;
	private Uri imageUri;
	private TextView log;
	private EditText getTitle;
	private EditText getHour;
	private EditText getMinute;
	private EditText getDetail;
	private Button backButt;
	private Button saveItem;
	private Button timeSetNow;
	private Button addPic;
	private Button takePhoto;
	private static final int IMAGE_CODE = 1;
	private static final int TAKE_PHOTO = 2;
	private static final int EDIT_PHOTO = 3;
	private ButlerDataBase dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.item_edit);
        
        length=0;
        alarm=0;
		getBasicDataFromIntent();
        initView();
        getItemDataFromDataBaseById();
        putDataIntoText();
        setDataUpdate();
        setButtonFunction();
        setPicClick();
    }
    //@Override
    //public void onSaveInstanceState(Bundle outState,PersistableBundle outPersistentState){
    //	super.onSaveInstanceState(outState,outPersistentState);
    //	outState.putString("state", getDetail.getText().toString());
    //}
    //protected void onStop(){
    //	super.onStop();
    //	saveToDataBase();
    //}
	
	public void getItemDataFromDataBaseById(){
		title="输入标题";
		detail="添加详情";
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		Cursor cursor=db.query("Items",null, "id=?", new String[]{""+Id}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				title=cursor.getString(cursor.getColumnIndex("title"));
				String yearStr=cursor.getString(cursor.getColumnIndex("year"));
				String monthStr=cursor.getString(cursor.getColumnIndex("month"));
				String dayStr=cursor.getString(cursor.getColumnIndex("day"));
				String time=cursor.getString(cursor.getColumnIndex("time"));
				Time=time;
				String lengthStr=cursor.getString(cursor.getColumnIndex("length"));
				detail=cursor.getString(cursor.getColumnIndex("detail"));
				String alarmStr=cursor.getString(cursor.getColumnIndex("alarm"));
				String imageStr=cursor.getString(cursor.getColumnIndex("image"));
				
				try {int a= Integer.parseInt(yearStr);year=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(monthStr);month=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(dayStr);day=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(lengthStr);length=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(alarmStr);alarm=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(imageStr);imageId=a;} catch (NumberFormatException e) {e.printStackTrace();}
				
				
				int first =Time.indexOf(":");
				int second=Time.lastIndexOf(":");
				String hourStr=Time.substring(0, first);
				String minuteStr=Time.substring(first+1,second);
				try {int a= Integer.parseInt(hourStr);hour=a;} catch (NumberFormatException e) {e.printStackTrace();}
				try {int a= Integer.parseInt(minuteStr);minute=a;} catch (NumberFormatException e) {e.printStackTrace();}
			
			}while(cursor.moveToNext());
		}
	}
	public void saveToDataBase(){
		makeTime();
		if(setCover==0){
		String objStr=getDetail.getText().toString();
		int indexStart=objStr.indexOf("[pic");
		int indexEnd=objStr.indexOf("]");
		if(indexStart!=-1&&indexEnd!=-1){
			String IdStr=objStr.substring(indexStart+4, indexEnd);
			int picId=0;
			try {int a= Integer.parseInt(IdStr);picId=a;} catch (NumberFormatException e) {e.printStackTrace();}
			imageId=picId;
		}else{imageId=0;}
		
		}
		if(Id==0){
			SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
			ContentValues values =new ContentValues();
			values.put("title",title);
			values.put("year",year);
			values.put("month",month);
			values.put("day",day);
			values.put("time",Time);
			values.put("length",length);
			values.put("detail",detail);
			values.put("image",imageId);
			values.put("alarm",alarm);
			values.put("user",com.des.butler.MainActivity.userId);
		    db.insert("Items", null, values);
		    values.clear();
		    
		    Cursor cursor=db.rawQuery("select last_insert_rowid() from Items", null);
		    if(cursor.moveToFirst()){
		    	Id=cursor.getInt(0);
		    }
		    
		}else{
			SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
			ContentValues values =new ContentValues();
			values.put("title",title);
			values.put("year",year);
			values.put("month",month);
			values.put("day",day);
			values.put("time",Time);
			values.put("length",length);
			values.put("detail",detail);
			values.put("image",imageId);
			values.put("alarm",alarm);
			values.put("user",com.des.butler.MainActivity.userId);
		    db.update("Items", values, "id=?", new String[]{""+Id});
		    values.clear();
		    
		}
		
		
	}
	public void getBasicDataFromIntent(){
		Intent intent=getIntent();
        Id =intent.getIntExtra("Id",0);
        day =intent.getIntExtra("Day",0);
        month =intent.getIntExtra("Month",0);
        year =intent.getIntExtra("Year",0);
	}
	
	public void initView(){
		//page= (TextView)findViewById(R.id.item_page_title);
		getTitle=(EditText)findViewById(R.id.title_edit);
        getHour=(EditText)findViewById(R.id.hour_get);
        getMinute=(EditText)findViewById(R.id.minute_get);
        getDetail=(EditText)findViewById(R.id.detail_edit);
        backButt=(Button)findViewById(R.id.back_butt);
        saveItem=(Button)findViewById(R.id.save_item);
        timeSetNow=(Button)findViewById(R.id.time_set_now);
        addPic=(Button)findViewById(R.id.add_pic);
        takePhoto=(Button)findViewById(R.id.take_photo);
        log=(TextView)findViewById(R.id.item_page_title);
        
        getDetail.setMovementMethod(LinkMovementMethod.getInstance());
        
        getDetail.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility") @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.detail_edit && canVerticalScroll(getDetail)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });

	}
	
	public void putDataIntoText(){
		String hourStr=""+hour;
		log.setText("事项："+year+"年"+month+"月"+day+"日");
		getTitle.setText(title);
		getHour.setText(hourStr);
		getMinute.setText(""+minute);
		getDetail.setText(detail);
		initPic(getDetail);
		//Editable aaaa=getDetail.getText();
		//Toast.makeText(ItemEdit.this,detail, Toast.LENGTH_SHORT).show();/////////////////////
	}
	public void setDataUpdate(){
		getTitle.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {  
		        title=getTitle.getText().toString();
		        saveToDataBase();
		    }
		    @Override  
		    public void beforeTextChanged(CharSequence text, int start, int count,int after) {
		    	
		    }  
		    @Override  
		    public void afterTextChanged(Editable edit) {
		    	
		    }  
		});
		getHour.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {  
		        String hourStr=getHour.getText().toString();
		        try {
				    int a= Integer.parseInt(hourStr);
				    hour=a;
				} catch (NumberFormatException e) {
				    e.printStackTrace();
				}
		        saveToDataBase();
		    }
		    @Override  
		    public void beforeTextChanged(CharSequence text, int start, int count,int after) {
		    	
		    }  
		    @Override  
		    public void afterTextChanged(Editable edit) {
		    	
		    }  
		});
		getMinute.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {  
		        String minuteStr=getMinute.getText().toString();
		        try {
				    int a= Integer.parseInt(minuteStr);
				    minute=a;
				} catch (NumberFormatException e) {
				    e.printStackTrace();
				}
		        saveToDataBase();
		    }
		    @Override  
		    public void beforeTextChanged(CharSequence text, int start, int count,int after) {
		    	
		    }  
		    @Override  
		    public void afterTextChanged(Editable edit) {
		    	
		    }  
		});
		getDetail.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {  
		        detail=getDetail.getText().toString();
		        saveToDataBase();
		    }
		    @Override  
		    public void beforeTextChanged(CharSequence text, int start, int count,int after) {
		    	
		    }  
		    @Override  
		    public void afterTextChanged(Editable edit) {
		    	
		    }
		    
		});
	}
	public void setButtonFunction(){
		timeSetNow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				getHourAndMinuteNow();
				
			}
		});
        backButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.DAYLIST");
			    intent.putExtra("Day", day);
			    intent.putExtra("Month", month);
			    intent.putExtra("Year", year);
				startActivity(intent);
				finish();
			}
		});
        saveItem.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				saveToDataBase();
				Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.DAYLIST");
			    intent.putExtra("Day", day);
			    intent.putExtra("Month", month);
			    intent.putExtra("Year", year);
				startActivity(intent);
				finish();
			}
		});
        addPic.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			Intent getAlbum = new Intent(Intent.ACTION_PICK);//////////////////////////
    	        getAlbum.setType("image/*");
    	        startActivityForResult(getAlbum,IMAGE_CODE);
    		}
    	});
        takePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				File outputImage=new File(Environment.getExternalStorageDirectory(),"photo.jpg");
				try{
					if(outputImage.exists()){
						outputImage.delete();
					}
					outputImage.createNewFile();
				}catch(IOException e){
					e.printStackTrace();
				}
				imageUri=Uri.fromFile(outputImage);/////////////////////////////////////////////
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,TAKE_PHOTO);//                            拍照
			}
		});
	}
	public void getHourAndMinuteNow(){
		java.util.Calendar now =java.util.Calendar.getInstance();
		hour=now.get(java.util.Calendar.HOUR_OF_DAY);
		minute=now.get(java.util.Calendar.MINUTE);
		getHour.setText(""+hour);
		getMinute.setText(""+minute);
	}
	public void makeTime(){
		String hourStr=""+hour;
		String minuteStr=""+minute;
		
	    if(hour<=24&&hour>=0){
	    if(hourStr.length()==1){
			hourStr="0"+hourStr;
		}else{if(hourStr.length()==2){}else{
			//Toast.makeText(ItemEdit.this, "自动调整小时格式", Toast.LENGTH_SHORT).show();
		}}}else{}
		
	    if(minute<=60&&minute>=0){
		    if(hourStr.length()==1){
				minuteStr="0"+minuteStr;
			}else{if(minuteStr.length()==2){}else{
				//Toast.makeText(ItemEdit.this, "自动调整分钟格式", Toast.LENGTH_SHORT).show();
			}}}else{}
		Time=hourStr+":"+minuteStr+":00";
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//              onActivityResult
    	//Bitmap bm = null;
       //ContentResolver resolver = getContentResolver();
        if(requestCode == IMAGE_CODE){//                                                           返回图库图片
            try{
                Uri originalUri = data.getData();
                //bm = MediaStore.Images.Media.getBitmap(resolver,originalUri);
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = ItemEdit.this.getContentResolver().query(originalUri,proj,null,null,null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                insertImg(path);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ItemEdit.this,"图片插入失败",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == TAKE_PHOTO){//                                                     返回相机照片，启动相片编辑
        	if (resultCode==RESULT_OK){
        		try{
        			Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        			// 这里还要存储图片信息到数据库，并把bitmap以processBitmapAndInsert方法插入！！！
        			File Butler =new File(Environment.getExternalStorageDirectory(),"Butler");
        			if(!Butler.exists()){
        				Butler.mkdir();
        				if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(ItemEdit.this, "创建Butler到"+Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();}
        			}
        			File Gallery=new File(Butler,"Gallery");
        			if(!Gallery.exists()){
        				Gallery.mkdir();
        				if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(ItemEdit.this, "创建Gallery到"+Butler, Toast.LENGTH_SHORT).show();}
        			}
        			java.util.Calendar now =java.util.Calendar.getInstance();
    				int Pday=now.get(java.util.Calendar.DAY_OF_MONTH);
    				int Pmonth=now.get(java.util.Calendar.MONTH)+1;
    				int Pyear=now.get(java.util.Calendar.YEAR);
    				int Phour=now.get(java.util.Calendar.HOUR_OF_DAY);
    				int Pminute=now.get(java.util.Calendar.MINUTE);
    				int Psecond=now.get(java.util.Calendar.SECOND);
    				String User=com.des.butler.MainActivity.getUserName(ItemEdit.this,com.des.butler.MainActivity.userId);
    				String fileName ="photo:"+User+"-"+Pyear+"-"+Pmonth+"-"+Pday+"-"+Phour+"-"+Pminute+"-"+Psecond+".jpg";
    				if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(ItemEdit.this, fileName, Toast.LENGTH_SHORT).show();}
    				File file=new File(Gallery,fileName);
    				try{
    					FileOutputStream fos =new FileOutputStream(file);
    					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    					fos.flush();
    					fos.close();
    				}catch(FileNotFoundException e){
    					e.printStackTrace();
    					if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(ItemEdit.this, "文件未创建成功", Toast.LENGTH_SHORT).show();}
    				}catch(IOException e){
    					e.printStackTrace();
    					if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(ItemEdit.this, "IO输出异常", Toast.LENGTH_SHORT).show();}
    			    }
    				String path = file.getPath();
    				insertImg(path);
    				
        		}catch(FileNotFoundException e){
        			e.printStackTrace();
        			
        		}
        		
        	}
        }

    }
	private void insertImg(String path){
        int picId=pathAndPicId(path);
		String picIdStr = "[pic"+picId+"]";
        Bitmap bitmap = BitmapFactory.decodeFile(path); //根据path解析出bitmap
        if(bitmap != null){
            SpannableString ss = getBitmapMime(path,picIdStr,picId);
            getDetail.append("\n");
            insertPhotoToEditText(ss);
            getDetail.append("\n");
            getDetail.append("\n");
        }
    }
	public int pathAndPicId(String path){
		int picId=0;
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		while(picId==0){
			Cursor cursor=db.query("Pictures",null, "path=?", new String[]{path}, null, null, null);
			if(cursor.moveToFirst()){
				do{
					picId=cursor.getInt(cursor.getColumnIndex("id"));
				}while(cursor.moveToNext());
			}
			if(picId==0){
				ContentValues values =new ContentValues();
				values.put("path",path);
				db.insert("Pictures", null, values);
				values.clear();
			}
		}
		return picId;
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
	private void insertPhotoToEditText(SpannableString ss){
        Editable et = getDetail.getText();
        int start = getDetail.getSelectionStart();
        if (start<0){
        	start=et.toString().length();
        }
        et.insert(start,ss);
        getDetail.setText(et);
        getDetail.setSelection(start+ss.length());
        getDetail.setFocusableInTouchMode(true);
        getDetail.setFocusable(true);
    }
	private SpannableString getBitmapMime(String path,String picIdStr,int picId) {
        SpannableString ss = new SpannableString(picIdStr);
        Bitmap obitmap;
        obitmap=BitmapFactory.decodeFile(path);
        if 	(obitmap==null){
        	obitmap = Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
        	obitmap.eraseColor(Color.parseColor("#84C1FF")); // 填充颜色
        	Canvas canvas = new Canvas(obitmap);
            Paint paint = new Paint();
            paint.setTextSize(50);
            paint.setColor(Color.WHITE);
            paint.setFlags(100);
            paint.setStyle(Paint.Style.FILL); //用于设置字体填充的类型
            canvas.drawText("图片丢失",100,175,paint);
            
        }
        ss=processBitmapAndInsert(obitmap,picIdStr,picId);
        return ss;
    }
	public SpannableString processBitmapAndInsert(Bitmap obitmap,String picIdStr,int picId){//        图片适应屏幕
		
		final int AApicId=picId;
		int w=obitmap.getWidth();//                                                   并以Clickable、SpannableString插入
        int h=obitmap.getHeight();
        
        WindowManager wm=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int vw=wm.getDefaultDisplay().getWidth();
        
        float k=0.9f;
        float r;
        Bitmap bitmap;
        if (w>=vw){
        	r=(float)vw/(float)w;
        	r=r*k;
        	if(r>0){Matrix matrix=new Matrix();
        	matrix.postScale(r,r);
        	bitmap=Bitmap.createBitmap(obitmap,0,0,w,h,matrix,false);}else{
        		Toast.makeText(ItemEdit.this, "图片缩放比例计算异常"+vw+"   "+w, Toast.LENGTH_SHORT).show();
        		bitmap=null;
        	}
        }else{
        	bitmap=obitmap;
        	r=1;
        }
        
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        SpannableString ss = new SpannableString(picIdStr);
        ss.setSpan(imageSpan, 0, picIdStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        com.des.butler.tools.MClickableSpan clickablespan=new com.des.butler.tools.MClickableSpan(picId){
        	
        	@Override
        	public void onClick(View v){
        		
        		//Toast.makeText(ItemEdit.this, "响应点击事件", Toast.LENGTH_SHORT).show();/////////
        		Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.PICTURE");
				intent.putExtra("ID", AApicId);
			    startActivity(intent);
				saveToDataBase();
			    //finish();
        	}
        };
        ss.setSpan(clickablespan,0, picIdStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        //bitmap.recycle();
        //obitmap.recycle();
        //Toast.makeText(ItemEdit.this, "  "+r+"  "+vw+"  "+w, Toast.LENGTH_SHORT).show();//////////////////////////////////
        return ss;
	}
	public void initPic(EditText object){
		String objStr=object.getText().toString();
		Editable processed=object.getText();
		int fromIndex=0;
		int indexStart=objStr.indexOf("[pic");
		int indexEnd=objStr.indexOf("]");
		while(indexStart!=-1&&indexEnd!=-1){
			int tempId=1;
			String tempIdStr=objStr.substring(indexStart+4, indexEnd);
			String tempPicIdStr=objStr.substring(indexStart,indexEnd+1);
			try {int a= Integer.parseInt(tempIdStr);tempId=a;} catch (NumberFormatException e) {e.printStackTrace();}
			String tempPath=pathAndPicId(tempId,"none");
			SpannableString tempSS=getBitmapMime(tempPath,tempPicIdStr,tempId);
			processed.delete(indexStart, indexEnd+1);
			processed.insert(indexStart, tempSS);
			fromIndex=indexEnd+1;
			indexStart=objStr.indexOf("[pic",fromIndex);
			indexEnd=objStr.indexOf("]",fromIndex);
			
		}
		//Toast.makeText(ItemEdit.this, processed, Toast.LENGTH_SHORT).show();///////////////////
		object.setText(processed);
	}
	private boolean canVerticalScroll(EditText contentEt) {
	    int scrollY = contentEt.getScrollY();
	    int scrollRange = contentEt.getLayout().getHeight();
	    int scrollExtent = contentEt.getHeight() - contentEt.getCompoundPaddingTop() - contentEt.getCompoundPaddingBottom();
	    int scrollDifference = scrollRange - scrollExtent;

	    if (scrollDifference == 0) {
	        return false;
	    }
	    return (scrollY > 0) || (scrollY < scrollDifference - 1);
	}
	public void setPicClick(){
		
	}

}
