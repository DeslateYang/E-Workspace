package com.des.butler.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ButlerDataBase extends SQLiteOpenHelper {

	public static final String CREATE_ITEM="create table Items("
			+"id integer primary key autoincrement,"
			+"user integer,"
			+"title text,"
			+"year integer,"
			+"month integer,"
			+"day intenger,"
			+"time time,"
			+"length integer,"
			+"detail text,"
			+"alarm integer," 
			+"image int)";
	public static final String CREATE_PIC="create table Pictures("
			+"id integer primary key autoincrement,"
			+"name text,"
			+"path text,"
			+"type text,"
			+"creator text,"
			+"createtime text,"
			+"location real,"
			+"latitude real,"
			+"longitude integer," 
			+"equipment text)";
	public static final String CREATE_USERS="create table Users("
			+"id integer primary key autoincrement,"
			+"name text,"
			+"phonenumber text,"
			+"password text)";
	public static final String CREATE_LOGIN="create table Login("
			+"id integer primary key autoincrement,"
			+"user integer,"
			+"state integer)";
			
			
	private Context mContext;
	
	public ButlerDataBase(Context context,String name,CursorFactory factory,int version){
		super(context,name,factory,version);
		mContext=context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_ITEM);
		db.execSQL(CREATE_LOGIN);
		Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
		db.execSQL(CREATE_PIC);
		db.execSQL(CREATE_USERS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    if(oldVersion==3){
	    	db.execSQL(CREATE_LOGIN);
	    }else if(oldVersion==2){
	    	db.execSQL(CREATE_LOGIN);
			db.execSQL("ALTER TABLE Items ADD COLUMN user integer");
			db.execSQL(CREATE_USERS);
			Toast.makeText(mContext, "数据库更新成功:2>3", Toast.LENGTH_SHORT).show();
		}else if(oldVersion==1){
			db.execSQL(CREATE_LOGIN);
			db.execSQL("ALTER TABLE Items ADD COLUMN user integer");
			db.execSQL(CREATE_USERS);
		    db.execSQL(CREATE_PIC);
		    Toast.makeText(mContext, "数据库更新成功:1>3", Toast.LENGTH_SHORT).show();
		}
		
	}

}
