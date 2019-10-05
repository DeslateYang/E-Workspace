package com.des.butler.tools;

import android.app.Activity;
import android.os.Bundle;

public class ButlerActivity extends Activity {
	//private ButlerDataBase dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,3);
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
