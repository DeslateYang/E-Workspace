package com.des.butler;

import com.des.butler.tools.ActivityCollector;
import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;
import com.des.butler.tools.TDPitem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LogIn extends ButlerActivity {
	
	private EditText getUsername;
	private EditText getPassword;
	private Button loginButt;
	private Button signUpButt;
	private ImageView background;
	private ImageButton headPortrait;
	private long exitTime = 0;
	private ButlerDataBase dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
	private String username;
	private String password;
	//private boolean LoginState;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        getWindow().setStatusBarColor(com.des.butler.MainActivity.themeColor);
        Drawable drawable = getResources().getDrawable(R.drawable.bar);
        getActionBar().setBackgroundDrawable(drawable);
        
        initView();
        username="�û���";
        password="����";
        //getStorage();
        getUsername.setText(username);
        getPassword.setText(password);
        loginButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(LogIn.this,"����˵�¼", Toast.LENGTH_SHORT).show();//////////////////////////
				//saveLogin();
				username=getUsername.getText().toString();
				password=getPassword.getText().toString();
				//Toast.makeText(LogIn.this,"������"+username+"-"+password, Toast.LENGTH_SHORT).show();////////////////////////////////
				if (!((username==null)||(username.equals("�û���"))||(username.equals(""))||(password==null)||(password.equals("����"))||(password.equals("")))){
					//Toast.makeText(LogIn.this,"��Ϣ��������", Toast.LENGTH_SHORT).show();
					SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
					Cursor cursor=db.query("Users",null, "name=?", new String[]{""+username}, null, null, null);
					String tempPsw=null;
					int tempId=0;
					if(cursor.moveToFirst()){
						do{
							tempPsw=cursor.getString(cursor.getColumnIndex("password"));
							tempId=cursor.getInt(cursor.getColumnIndex("id"));
							
						}while(cursor.moveToNext());
					}
					//Toast.makeText(LogIn.this,"��ȡ��"+tempPsw+"|"+tempId, Toast.LENGTH_SHORT).show();
					if(tempId!=0||tempPsw!=null){
						//Toast.makeText(LogIn.this,"��ȡ��������һ������", Toast.LENGTH_SHORT).show();
						if(tempId!=0&&tempPsw!=null){
							//Toast.makeText(LogIn.this,"�������ݾ���ȡ", Toast.LENGTH_SHORT).show();
							if(tempPsw.equals(password)){
								
								
								//com.des.butler.MainActivity.userId=tempId;
								Boolean logInState=true;
								Intent intent = new Intent();
								intent.putExtra("user_id",tempId);
								intent.putExtra("state", logInState);
								//Toast.makeText(LogIn.this,"�û�ID��"+tempId+"��¼״̬��"+logInState, Toast.LENGTH_SHORT).show();
								setResult(RESULT_OK,intent);
								finish();
							}else{
								int aa=tempPsw.length();
								int bb=password.length();
								//Toast.makeText(LogIn.this,"��"+tempPsw+"��"+"����"+aa+"������"+"��"+password+"������"+bb, Toast.LENGTH_SHORT).show();
								Toast.makeText(LogIn.this,"�û��������벻��", Toast.LENGTH_SHORT).show();
							}
						}else {
							Toast.makeText(LogIn.this,"�û�������Ϣ����������Ϊ������ע������,�����Ե�¼"+username, Toast.LENGTH_SHORT).show();
							if(tempId!=0&&tempPsw==null){
								ContentValues values =new ContentValues();
								values.put("password",password);
								db.update("Users", values, "id=?", new String[]{""+tempId});
							    values.clear();
							    //fjdioqwjf
							    
							}else{
								Toast.makeText(LogIn.this,"�û�id������", Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						//Toast.makeText(LogIn.this,"��ȡ��ʧ��", Toast.LENGTH_SHORT).show();
						Toast.makeText(LogIn.this,"�����û���Ϣ�����ڣ�����б���ע��", Toast.LENGTH_SHORT).show();
					}
				}else {
					
					Toast.makeText(LogIn.this,"������û���������", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        signUpButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				saveLogin();
				username=getUsername.getText().toString();
				password=getPassword.getText().toString();
				if (!((username==null)||(username.equals("�û���"))||(username.equals(""))||(password==null)||(password.equals("����"))||(password.equals("")))){
					SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
					Cursor cursor=db.query("Users",null, "name=?", new String[]{""+username}, null, null, null);
					String tempPsw=null;
					int tempId=0;
					if(cursor.moveToFirst()){
						do{
							tempPsw=cursor.getString(cursor.getColumnIndex("password"));
							tempId=cursor.getInt(cursor.getColumnIndex("id"));
							
						}while(cursor.moveToNext());
					}
					if(tempId==0){
						try{
							ContentValues values =new ContentValues();
							values.put("name", username);
							values.put("password",password);
							db.insert("Users", null, values);
						    values.clear();
						    Toast.makeText(LogIn.this,"ע��ɹ������¼", Toast.LENGTH_SHORT).show();
						    
						    Cursor c=db.rawQuery("select last_insert_rowid() from Items", null);
						    int newId;
						    if(cursor.moveToFirst()){
						    	newId=cursor.getInt(0);
						    }else{newId=0;}
						    moveAllInformation(0,newId);
						}catch(Exception e){
							e.printStackTrace();
			                Toast.makeText(LogIn.this,"ע��ʧ��",Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(LogIn.this,"�����û���Ϣ���ڣ�����е�¼", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(LogIn.this,"������û���������", Toast.LENGTH_SHORT).show();
				}
			}
		});
        headPortrait.setOnClickListener(new OnClickListener(){
        	long[] mHits = new long[3];
        	@Override
			public void onClick(View v){
        		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
		        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
		            //�����û���ǿ�Ƶ�¼
		        	username=getUsername.getText().toString();
		        	password=getPassword.getText().toString();
		        	if (!((username==null)||(username.equals("�û���"))||(username.equals(""))||(password==null)||(password.equals("����"))||(password.equals("")))){
		        		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		        		Cursor cursor=db.query("Users",null, "name=?", new String[]{""+username}, null, null, null);
						String tempPsw=null;
						int tempId=0;
						if(cursor.moveToFirst()){
							do{
								tempPsw=cursor.getString(cursor.getColumnIndex("password"));
								tempId=cursor.getInt(cursor.getColumnIndex("id"));
								
							}while(cursor.moveToNext());
						}
						if(tempId!=0||tempPsw!=null){
							com.des.butler.MainActivity.LoginState=true;
							if(tempId!=0&&tempPsw!=null){
								if(tempPsw.equals(password)){
									Toast.makeText(LogIn.this,"��������¼", Toast.LENGTH_SHORT).show();
									//com.des.butler.MainActivity.userId=tempId;
									Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								}else{
									Toast.makeText(LogIn.this,"��ǿ�Ƶ�¼������������", Toast.LENGTH_SHORT).show();
									ContentValues values =new ContentValues();
									values.put("password",tempPsw);
									db.update("Users", values, "id=?", new String[]{""+tempId});
								    values.clear();
									Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								}
							}else {
								if(tempId!=0&&tempPsw==null){
									Toast.makeText(LogIn.this,"�û����������𻵣���Ϊ���Զ����������벢����", Toast.LENGTH_SHORT).show();
									ContentValues values =new ContentValues();
									values.put("password",tempPsw);
									db.update("Users", values, "id=?", new String[]{""+tempId});
								    values.clear();
								    Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								    
								}else{}
							}
						}else{
							Toast.makeText(LogIn.this,"�����û���Ϣ�����ڣ�����б���ע��", Toast.LENGTH_SHORT).show();
						}
		        	}else if(username!=null&&password==null){
		        		//ֻ�������û���
		        		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		        		Cursor cursor=db.query("Users",null, "name=?", new String[]{""+username}, null, null, null);
						String tempPsw=null;
						int tempId=0;
						if(cursor.moveToFirst()){
							do{
								tempPsw=cursor.getString(cursor.getColumnIndex("password"));
								tempId=cursor.getInt(cursor.getColumnIndex("id"));
								
							}while(cursor.moveToNext());
						}
						if(tempId!=0||tempPsw!=null){
							com.des.butler.MainActivity.LoginState=true;
							if(tempId!=0&&tempPsw!=null){
								//ֻ���û�����½һ�������û�
								Toast.makeText(LogIn.this,"����ǿ�Ƶ�¼", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.putExtra("user_id",tempId);
								intent.putExtra("state", true);
								setResult(RESULT_OK,intent);
								finish();
							}else {
								if(tempId!=0&&tempPsw==null){
									//ֻ���û�����¼һ��ȱ��������û�
									Toast.makeText(LogIn.this,"ǿ�Ƶ�¼������������һ������", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								}else{}
							}
						}else{
							//ֻ���û�������ȫ�������û������Ϣ
							Toast.makeText(LogIn.this,"ע��Ϊ���û�����������һ������", Toast.LENGTH_SHORT).show();
							ContentValues values =new ContentValues();
							values.put("name", username);
							values.put("password",tempPsw);
							db.insert("Users", null, values);
						    values.clear();
						    Intent intent = new Intent();
							intent.putExtra("user_id",tempId);
							intent.putExtra("state", true);
							setResult(RESULT_OK,intent);
							finish();
						}
		        	}else{
		        		Toast.makeText(LogIn.this,"������Ҫ����һ���û�����...", Toast.LENGTH_SHORT).show();
		        	}
					
		            
		        	mHits = null;
		            mHits = new long[7];
		        }
			}
		});
        
	}
	public void initView(){
		getUsername=(EditText)findViewById(R.id.get_username);
		getPassword=(EditText)findViewById(R.id.get_password);
		loginButt=(Button)findViewById(R.id.log_in_butt);
		signUpButt=(Button)findViewById(R.id.sign_up_butt);
		background=(ImageView)findViewById(R.id.background);
		headPortrait=(ImageButton)findViewById(R.id.head_portrait);
		Bitmap obitmap=BitmapFactory.decodeResource(getResources(),R.drawable.login_background);
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
		background.setImageBitmap(bitmap);
	}
	@Override
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "�ٵ�һ���˳�����", Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishall();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void saveLogin(){
		SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
		sp.edit()
		    .putString("username", username)
		    .putString("password", password)
		    .apply();
	}
	public void getStorage(){
		SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
		username=sp.getString("username", null);
		password=sp.getString("password", null);
		
	}
	public void moveAllInformation(int origionalId,int newId){
        SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		
        ContentValues values =new ContentValues();
		values.put("id",newId);
		db.update("Items", values, "user=?", new String[]{""+origionalId});
	    values.clear();
	}
	
}
