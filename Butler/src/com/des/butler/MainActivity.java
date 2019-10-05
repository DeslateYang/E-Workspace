package com.des.butler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;
import com.des.butler.tools.TDPitem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ButlerActivity {
	
	private ButlerDataBase dbHelperForButler;
	public static boolean DeveloperState;
	private TextView showDeveloperState;
	private TextView developerOutput;
	private TextView showUserName;
	
	private int LOG_IN=1;
	
	public static int userId=0;
	public static boolean LoginState;
	
	public static final String ButlerVersion = "1.3.3fix";
	public static final int DataBaseVersion = 4;
	public static int themeColor=0xff84C1FF;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
    }
	public void refresh(){
    	setContentView(R.layout.activity_main);
    	getWindow().setStatusBarColor(themeColor);
    	Drawable drawable = getResources().getDrawable(R.drawable.bar);
    	getActionBar().setBackgroundDrawable(drawable);
        dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,DataBaseVersion);
        dbHelperForButler.getWritableDatabase();
        
        showUserName=(TextView)findViewById(R.id.show_user_name);
        showDeveloperState=(TextView)findViewById(R.id.show_developer_state);
        developerOutput=(TextView)findViewById(R.id.out_put);
        
        testLoginState();
        
        if (LoginState==false||userId==0){
        	Intent intent=new Intent("com.des.butler.ACTION_START");
			intent.addCategory("com.des.butler.LOGIN");
		    startActivityForResult(intent,LOG_IN);
		    Toast.makeText(MainActivity.this,"ʹ��ǰ���¼", Toast.LENGTH_SHORT).show();
        }else{
        	//Toast.makeText(MainActivity.this,"userId="+userId, Toast.LENGTH_SHORT).show();
        	String userName=getUserName(this,userId);
        	Toast.makeText(MainActivity.this,"��¼�ɹ�,��ӭ��"+userName, Toast.LENGTH_SHORT).show();
        	showUserName.setText("��ǰ�û���"+userName);
        	
        }
        
        
        DeveloperState=false;
        if(DeveloperState==false){
        	showDeveloperState.setVisibility(View.INVISIBLE);
        	developerOutput.setVisibility(View.INVISIBLE);
        }else{
        	showDeveloperState.setVisibility(View.VISIBLE);
        	developerOutput.setVisibility(View.VISIBLE);
        }
        
        Button caleButt=(Button)findViewById(R.id.cale_butt);
        caleButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				//Intent intent=new Intent(MainActivity.this,DailyList.class);
				Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.CALENDAR");
			    
				startActivity(intent);
				
			}
		});
        ImageButton addButt=(ImageButton)findViewById(R.id.main_add);
        addButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent=new Intent("com.des.butler.ACTION_START");
	    		intent.addCategory("com.des.butler.ITEMEDIT");
	    		java.util.Calendar now =java.util.Calendar.getInstance();
				int Day=now.get(java.util.Calendar.DAY_OF_MONTH);
				int Month=now.get(java.util.Calendar.MONTH)+1;
				int Year=now.get(java.util.Calendar.YEAR);
	    		intent.putExtra("Day", Day);
	    	    intent.putExtra("Month", Month);
	    	    intent.putExtra("Year", Year);
	    	    int zero=0;
	    	    intent.putExtra("Id", zero);
	    		startActivity(intent);
			}
		});
    }
    public static String getUserName(Context context,int userId){
		String userName=null;
		ButlerDataBase dbHelperForButler=new ButlerDataBase(context,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		Cursor cursor=db.query("Users",null, "id=?", new String[]{""+userId}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				userName=cursor.getString(cursor.getColumnIndex("name"));
				
			}while(cursor.moveToNext());
		}
		return userName;
	}
    public void saveLoginState(){
    	SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
    	ContentValues values =new ContentValues();
        int IntLoginState;
        if(LoginState){IntLoginState=1;}else{IntLoginState=0;}
    	values.put("user",userId);
    	values.put("state",IntLoginState);
    	db.update("Login", values, "id=?", new String[]{""+1});
    	//Toast.makeText(MainActivity.this,"����״̬�û�ID��"+userId+"��¼״̬��"+LoginState, Toast.LENGTH_SHORT).show();
    	values.clear();
    	
    	
	}
    public void testLoginState(){
    	SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		Cursor cursor=db.query("Login",null, "id=?", new String[]{""+1}, null, null, null);
		Boolean stateExistance=false;
		if(cursor.moveToFirst()){
			do{
				userId=cursor.getInt(cursor.getColumnIndex("user"));
				int IntLoginState=cursor.getInt(cursor.getColumnIndex("state"));
				LoginState=(IntLoginState==1)?true:false;
				//Toast.makeText(MainActivity.this,"�û�ID��"+userId+"��¼״̬��"+LoginState, Toast.LENGTH_SHORT).show();
				stateExistance=true;
			}while(cursor.moveToNext());
		}
		if(stateExistance){
			//Toast.makeText(MainActivity.this,"��ȡ���û�ID��"+userId+"��¼״̬��"+LoginState, Toast.LENGTH_SHORT).show();
		}else{
			
			ContentValues values =new ContentValues();
			values.put("user",0);
	    	values.put("state",0);
	    	db.insert("Login", null, values);
		    values.clear();
	    	//Toast.makeText(MainActivity.this,"�״ε�½������״̬�洢", Toast.LENGTH_SHORT).show();
		}
		
	}
	
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
    	if (requestCode==LOG_IN){
    		if(resultCode==RESULT_OK){
    			//Toast.makeText(MainActivity.this,"��¼�ɹ������Խ���Intent", Toast.LENGTH_SHORT).show();
    			userId=data.getIntExtra("user_id",0);
    			LoginState=data.getBooleanExtra("State", true);
    			//Toast.makeText(MainActivity.this,"�û�ID��"+userId+"��¼״̬��"+LoginState, Toast.LENGTH_SHORT).show();
    			saveLoginState();
    			refresh();
    		}
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int id = item.getItemId();
        
    	if (id == R.id.action_add) {
    		Intent intent=new Intent("com.des.butler.ACTION_START");
    		intent.addCategory("com.des.butler.ITEMEDIT");
    		java.util.Calendar now =java.util.Calendar.getInstance();
			int Day=now.get(java.util.Calendar.DAY_OF_MONTH);
			int Month=now.get(java.util.Calendar.MONTH)+1;
			int Year=now.get(java.util.Calendar.YEAR);
    		intent.putExtra("Day", Day);
    	    intent.putExtra("Month", Month);
    	    intent.putExtra("Year", Year);
    	    int zero=0;
    	    intent.putExtra("Id", zero);
    		startActivity(intent);
    		
            return true;
            
        }else if(id==R.id.action_settings){
        	Intent intent=new Intent("com.des.butler.ACTION_START");
    		intent.addCategory("com.des.butler.SETTINGS");
    		startActivity(intent);
    		//Toast.makeText(MainActivity.this, "����", Toast.LENGTH_LONG).show();
            
            return true;
        }else if(id==R.id.action_clone){
        	Toast.makeText(this,"��ʼ����", Toast.LENGTH_SHORT).show();
        	File Butler =new File(Environment.getExternalStorageDirectory(),"Butler");
			if(!Butler.exists()){
				Butler.mkdir();
				if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(MainActivity.this, "����Butler��"+Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();}
			}
			File Gallery=new File(Butler,"Gallery");
			if(!Gallery.exists()){
				Gallery.mkdir();
				if(com.des.butler.MainActivity.DeveloperState==true){Toast.makeText(MainActivity.this, "����Gallery��"+Butler, Toast.LENGTH_SHORT).show();}
			}
        	ButlerDataBase dbHelperForButler=new ButlerDataBase(MainActivity.this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
    		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
    		
    		int successClone=0;
    		int pass=0;
    		int brokenRecord=0;
    		int unexistPic=0;
    		int failToDecode=0;
    		
    		Cursor cursor1=db.rawQuery("select count (*) from Pictures",null);
    		cursor1.moveToFirst();
    		Long total = cursor1.getLong(0);
    		cursor1.close();
    		int tempId=1;
    		while(tempId<=total){
    			Cursor cursor2=db.query("Pictures",null,"id=?", new String[]{""+tempId}, null, null, null);
    			if(cursor2.moveToFirst()){
    				do{
    					String path=null;
    					try{
    						//int a=1;
    						path=cursor2.getString(cursor2.getColumnIndex("path"));//����Id���ҵ�path
    						//Toast.makeText(this,"���ڴ���"+path, Toast.LENGTH_SHORT).show();
    						//path="ahfeioqfoiqe";
    						File tempFile=new File(path);
    						if(tempFile.exists()){//����ļ�������
    							int a=path.indexOf("Butler/Gallery/");
    							if(a==-1){
    							    try{
    							    	Bitmap bitmap=null;
    								    bitmap = BitmapFactory.decodeFile(path); //����path������bitmap
    							    	if (bitmap==null){
    							    		failToDecode++;//����ʧ��+1
    							    		
    							    	}else{
    							    		String fileName =tempFile.getName()+".jpg";//�����ļ���
    				    		    		File file=new File(Gallery,fileName);
    				    		    		try{
    				    			    		FileOutputStream fos =new FileOutputStream(file);//�ļ������
    				    		     			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);//��bitmapѹ��
    				    			    		fos.flush();
    				    			    		fos.close();
    				    			    		successClone++;
    				    			    	}catch(FileNotFoundException e){
    				    		    			e.printStackTrace();
    				    		    			failToDecode++;
    				    		    		}catch(IOException e){
    				    		    			e.printStackTrace();
    				    		    			failToDecode++;
    				    		    		}
    				    		    		String newPath = file.getPath();
    				    		    		ContentValues values =new ContentValues();
    				    		    		values.put("path",newPath);
    				    		    		db.update("Pictures", values, "id=?", new String[]{""+tempId});
    				    		    	    values.clear();
    				    		    	    
    							    	}
    							    	tempId++;
    						    	}catch(Exception e){
    							    	failToDecode++;//����ʧ��+1
    							    	tempId++;
    							    }
    							}else{
    								pass++;
    								tempId++;
    							}
    						}else{
    							unexistPic++;//�ļ�������+1
    							tempId++;
    						}
    					}catch(Exception e){
    						brokenRecord++;//Id���ܲ���path����¼��+1
    						tempId++;
    					}
    					
    				}while(cursor2.moveToNext());
    			}
    		}
    		Toast.makeText(this,"ͼƬ������ɣ���������"+total+"���������豸�ݣ�"+pass+"���ɹ����ݣ�"+successClone+",��¼����"+brokenRecord+"���ļ���ʧ��"+unexistPic+"������ʧ�ܣ�"+failToDecode, Toast.LENGTH_SHORT).show();
    		
    		
        	
        }
        return super.onOptionsItemSelected(item);
    }
}
