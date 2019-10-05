package com.des.butler;

import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends ButlerActivity {
	private Switch switchDeveloperState;
	private TextView showVersion;
	private Button logOutButt;
	private ButlerDataBase dbHelperForButler;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getWindow().setStatusBarColor(com.des.butler.MainActivity.themeColor);
        Drawable drawable = getResources().getDrawable(R.drawable.bar);
        getActionBar().setBackgroundDrawable(drawable);
        
        dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
        
        switchDeveloperState=(Switch)findViewById(R.id.developer_state);
        showVersion=(TextView)findViewById(R.id.version_show);
        logOutButt=(Button)findViewById(R.id.log_out_butt);
        
        showVersion.setText("版本：Butler"+com.des.butler.MainActivity.ButlerVersion);
        
        switchDeveloperState.setChecked(com.des.butler.MainActivity.DeveloperState);
        switchDeveloperState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					com.des.butler.MainActivity.DeveloperState=true;
				}else{
					com.des.butler.MainActivity.DeveloperState=false;
				}
				
			}
		});
        logOutButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				com.des.butler.MainActivity.userId=0;
				com.des.butler.MainActivity.LoginState=false;
				SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		    	ContentValues values =new ContentValues();
		        values.put("user",0);
		    	values.put("state",0);
		    	db.update("Login", values, "id=?", new String[]{""+1});
		    	values.clear();
				com.des.butler.tools.ActivityCollector.finishallExceptMain(Settings.this);
				
			}
		});
        Button command=(Button)findViewById(R.id.command);
        
        command.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				EditText operation=(EditText)findViewById(R.id.operation);
				String operationStr=operation.getText().toString();
				SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		        try{
		        	db.execSQL(operationStr);
		        	Toast.makeText(Settings.this,"语句执行成功", Toast.LENGTH_SHORT).show();
		        }catch(Exception e){
		        	e.printStackTrace();
		        	Toast.makeText(Settings.this,"语句不正确，执行失败", Toast.LENGTH_SHORT).show();
		        }
			}
		});
        
        
    }

}
