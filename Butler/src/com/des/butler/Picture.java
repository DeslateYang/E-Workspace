package com.des.butler;

import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;
//import com.des.butler.tools.SwZoomDragImageView;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Picture extends ButlerActivity {
	
	private ButlerDataBase dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(com.des.butler.MainActivity.themeColor);
        
        
        setContentView(R.layout.picture);
        //SwZoomDragImageView pic=(SwZoomDragImageView)findViewById(R.id.picture);
        ImageView pic=(ImageView)findViewById(R.id.picture);
        Intent intent=getIntent();
        int picId=intent.getIntExtra("ID",0);
        String path=null;
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		Cursor cursor=db.query("Pictures",null, "id=?", new String[]{""+picId}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				path=cursor.getString(cursor.getColumnIndex("path"));
			}while(cursor.moveToNext());
		}
		//Bitmap obitmap=BitmapFactory.decodeResource(getResources(),R.drawable.login_background);
		Bitmap obitmap=BitmapFactory.decodeFile(path);
		WindowManager wm=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int vw=wm.getDefaultDisplay().getWidth();
        int vh=wm.getDefaultDisplay().getHeight();
        int w=obitmap.getWidth();
        int h=obitmap.getHeight();
        float k=0.9f;
        float rw;
        float rh;
        Bitmap bitmap;
        rw=(float)vw/(float)w;
        rh=(float)vh/(float)h;
        float r;
        if (rw<=rh){
        	r=rw;
        }else{
        	r=rh;
        }
        //r=rw;
        if(r>0){Matrix matrix=new Matrix();
    	matrix.postScale(r,r);
    	bitmap=Bitmap.createBitmap(obitmap,0,0,w,h,matrix,false);
    	}else{
    		bitmap=obitmap;
    	}
		pic.setImageBitmap(bitmap);
		Button back=(Button)findViewById(R.id.back_butt_in_pic);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
	}

}
