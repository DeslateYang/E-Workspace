package com.des.butler.tools;

import java.util.ArrayList;
import java.util.List;

import com.des.butler.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityCollector {
	
	public static List<Activity> activities=new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){
		
		activities.add(activity);
		
	}
	
	public static void removeActivity(Activity activity){
		
		activities.remove(activity);
		
	}
	
	public static void finishall(){
		
		for (Activity activity : activities){
			if (!activity.isFinishing()){
				activity.finish();
			}
		}
	}
    public static void finishallExceptMain(Context c){
		
		for (Activity activity : activities){
			if (!activity.isFinishing()){
				activity.finish();
			}
		}
		Intent intent=new Intent(c,MainActivity.class);
		c.startActivity(intent);
	}

}
