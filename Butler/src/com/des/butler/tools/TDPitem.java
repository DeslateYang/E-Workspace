package com.des.butler.tools;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale") public class TDPitem {
	
	private int itemId;
	
	private String title;
	
	private String briefDetail;
	
	private int imageId;
	
	private String log;
	
	int state;
	
	public static final int SHOW=0;
	
	public static final int MANAGE=1;
	
	public TDPitem(int itemId,String title,String detail,int imageId,String log,int state){
		this.itemId=itemId;
		this.title=brief(title,10);
		this.briefDetail=brief(detail,50);
		this.imageId=imageId;
		this.log=processLog(log);
		this.state=state;
	}
	
	public String processLog(String origionalLog){
		int first =origionalLog.indexOf(":");
		int second=origionalLog.lastIndexOf(":");
		int hour=0;
		int minute=0;
		String hourStr=origionalLog.substring(0, first);
		String minuteStr=origionalLog.substring(first+1,second);
		try {int a= Integer.parseInt(hourStr);hour=a;} catch (NumberFormatException e) {e.printStackTrace();}
		try {int a= Integer.parseInt(minuteStr);minute=a;} catch (NumberFormatException e) {e.printStackTrace();}
		String fhourStr = String.format("%02d", hour);
		String fminuteStr = String.format("%02d", minute);
		String processedLog=fhourStr+":"+fminuteStr;
		return processedLog;
	}
	
	public String brief(String detail,int a){
		
		detail = detail.replaceAll("(\r\n|\r|\n|\n\r)", ""); 
		int length=detail.length();
		if(length>=a){
			detail=detail.substring(0,a);
			briefDetail=detail+" ...";
		}else{
			briefDetail=detail;
		}
		return briefDetail;
	}
	
	public int getItemId(){
		return itemId;
	}
	public String getTitle(){
		return title;
	}
	public String getBriefDetail(){
		return briefDetail;
	}
	public int getimageId(){
		return imageId;
	}
	public String getLog(){
		return log;
	}
	public int getState(){
		return state;
	}
	public void setState(int state){
		this.state=state;
	}

}
