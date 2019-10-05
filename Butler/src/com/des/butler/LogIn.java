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
        username="用户名";
        password="密码";
        //getStorage();
        getUsername.setText(username);
        getPassword.setText(password);
        loginButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(LogIn.this,"点击了登录", Toast.LENGTH_SHORT).show();//////////////////////////
				//saveLogin();
				username=getUsername.getText().toString();
				password=getPassword.getText().toString();
				//Toast.makeText(LogIn.this,"输入了"+username+"-"+password, Toast.LENGTH_SHORT).show();////////////////////////////////
				if (!((username==null)||(username.equals("用户名"))||(username.equals(""))||(password==null)||(password.equals("密码"))||(password.equals("")))){
					//Toast.makeText(LogIn.this,"信息输入完整", Toast.LENGTH_SHORT).show();
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
					//Toast.makeText(LogIn.this,"获取到"+tempPsw+"|"+tempId, Toast.LENGTH_SHORT).show();
					if(tempId!=0||tempPsw!=null){
						//Toast.makeText(LogIn.this,"获取到了至少一条数据", Toast.LENGTH_SHORT).show();
						if(tempId!=0&&tempPsw!=null){
							//Toast.makeText(LogIn.this,"两条数据均获取", Toast.LENGTH_SHORT).show();
							if(tempPsw.equals(password)){
								
								
								//com.des.butler.MainActivity.userId=tempId;
								Boolean logInState=true;
								Intent intent = new Intent();
								intent.putExtra("user_id",tempId);
								intent.putExtra("state", logInState);
								//Toast.makeText(LogIn.this,"用户ID："+tempId+"登录状态："+logInState, Toast.LENGTH_SHORT).show();
								setResult(RESULT_OK,intent);
								finish();
							}else{
								int aa=tempPsw.length();
								int bb=password.length();
								//Toast.makeText(LogIn.this,"“"+tempPsw+"“"+"长度"+aa+"不等于"+"”"+password+"”长度"+bb, Toast.LENGTH_SHORT).show();
								Toast.makeText(LogIn.this,"用户名与密码不符", Toast.LENGTH_SHORT).show();
							}
						}else {
							Toast.makeText(LogIn.this,"用户密码信息不完整，已为您重新注册密码,请重试登录"+username, Toast.LENGTH_SHORT).show();
							if(tempId!=0&&tempPsw==null){
								ContentValues values =new ContentValues();
								values.put("password",password);
								db.update("Users", values, "id=?", new String[]{""+tempId});
							    values.clear();
							    //fjdioqwjf
							    
							}else{
								Toast.makeText(LogIn.this,"用户id不存在", Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						//Toast.makeText(LogIn.this,"获取到失败", Toast.LENGTH_SHORT).show();
						Toast.makeText(LogIn.this,"本地用户信息不存在，请进行本地注册", Toast.LENGTH_SHORT).show();
					}
				}else {
					
					Toast.makeText(LogIn.this,"请填好用户名和密码", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        signUpButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				saveLogin();
				username=getUsername.getText().toString();
				password=getPassword.getText().toString();
				if (!((username==null)||(username.equals("用户名"))||(username.equals(""))||(password==null)||(password.equals("密码"))||(password.equals("")))){
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
						    Toast.makeText(LogIn.this,"注册成功，请登录", Toast.LENGTH_SHORT).show();
						    
						    Cursor c=db.rawQuery("select last_insert_rowid() from Items", null);
						    int newId;
						    if(cursor.moveToFirst()){
						    	newId=cursor.getInt(0);
						    }else{newId=0;}
						    moveAllInformation(0,newId);
						}catch(Exception e){
							e.printStackTrace();
			                Toast.makeText(LogIn.this,"注册失败",Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(LogIn.this,"本地用户信息存在，请进行登录", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(LogIn.this,"请填好用户名和密码", Toast.LENGTH_SHORT).show();
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
		            //根据用户名强制登录
		        	username=getUsername.getText().toString();
		        	password=getPassword.getText().toString();
		        	if (!((username==null)||(username.equals("用户名"))||(username.equals(""))||(password==null)||(password.equals("密码"))||(password.equals("")))){
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
									Toast.makeText(LogIn.this,"已正常登录", Toast.LENGTH_SHORT).show();
									//com.des.butler.MainActivity.userId=tempId;
									Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								}else{
									Toast.makeText(LogIn.this,"已强制登录并保存新密码", Toast.LENGTH_SHORT).show();
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
									Toast.makeText(LogIn.this,"用户密码数据损坏，已为您自动保存新密码并登入", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(LogIn.this,"本地用户信息不存在，请进行本地注册", Toast.LENGTH_SHORT).show();
						}
		        	}else if(username!=null&&password==null){
		        		//只输入了用户名
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
								//只有用户名登陆一个完整用户
								Toast.makeText(LogIn.this,"免密强制登录", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.putExtra("user_id",tempId);
								intent.putExtra("state", true);
								setResult(RESULT_OK,intent);
								finish();
							}else {
								if(tempId!=0&&tempPsw==null){
									//只有用户名登录一个缺少密码的用户
									Toast.makeText(LogIn.this,"强制登录，但建议设置一个密码", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									intent.putExtra("user_id",tempId);
									intent.putExtra("state", true);
									setResult(RESULT_OK,intent);
									finish();
								}else{}
							}
						}else{
							//只有用户名，完全不存在用户相关信息
							Toast.makeText(LogIn.this,"注册为新用户，建议设置一个密码", Toast.LENGTH_SHORT).show();
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
		        		Toast.makeText(LogIn.this,"你至少要输入一个用户名吧...", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "再点一次退出程序", Toast.LENGTH_LONG).show();
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
