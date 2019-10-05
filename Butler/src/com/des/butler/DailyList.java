package com.des.butler;

import java.util.ArrayList;
import java.util.List;

import com.des.butler.tools.ButlerActivity;
import com.des.butler.tools.ButlerDataBase;
import com.des.butler.tools.TDPitem;
import com.des.butler.tools.TdpAdapter;
import com.des.butler.tools.TdpAdapter.MyClickListener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DailyList extends ButlerActivity {
	
	private int Day;
	private int Month;
	private int Year;
	private int selectedPosition;
	private int selectedId;
    private ButlerDataBase dbHelperForButler=new ButlerDataBase(this,"Butler.db",null,com.des.butler.MainActivity.DataBaseVersion);
	private List<TDPitem> items=new ArrayList<TDPitem>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_list);
        getWindow().setStatusBarColor(com.des.butler.MainActivity.themeColor);
        Drawable drawable = getResources().getDrawable(R.drawable.bar);
        getActionBar().setBackgroundDrawable(drawable);
        
        Intent intent=getIntent();
        Day =intent.getIntExtra("Day",0);
        Month =intent.getIntExtra("Month",0);
        Year =intent.getIntExtra("Year",0);
        
        TextView test=(TextView)findViewById(R.id.textView2);
        test.setText(Year+"Äê"+Month+"ÔÂ"+Day+"ÈÕ");
        
        initItems();
        TdpAdapter adapter=new TdpAdapter(DailyList.this,R.layout.tdp_item,items,mListener1,mListener2);
        ListView dailyList=(ListView)findViewById(R.id.daily_list);
        dailyList.setAdapter(adapter);
        
        dailyList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        		TDPitem item=items.get(position);
        		Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.ITEMEDIT");
			    intent.putExtra("Day", Day);
			    intent.putExtra("Month", Month);
			    intent.putExtra("Year", Year);
			    intent.putExtra("Id", item.getItemId());
				startActivity(intent);
				finish();
        	}
        });
        dailyList.setOnItemLongClickListener(new OnItemLongClickListener(){
        	@Override
        	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id){
        		selectedPosition=position;
        		
        		TDPitem item=items.get(position);
        		item.setState(TDPitem.MANAGE);
        		selectedId=item.getItemId();
        		
        		TdpAdapter adapter=new TdpAdapter(DailyList.this,R.layout.tdp_item,items,mListener1,mListener2);
                ListView dailyList=(ListView)findViewById(R.id.daily_list);
                dailyList.setAdapter(adapter);
                
                return true;
        	}
        });
        
    }
	private MyClickListener mListener1 = new MyClickListener() {
		@Override
		public void myOnClick(/*int position,*/ View v) {
		        	
		            TDPitem item=items.get(selectedPosition);
	        		item.setState(TDPitem.SHOW);
	        		selectedId=item.getItemId();
		            
		            TdpAdapter adapter=new TdpAdapter(DailyList.this,R.layout.tdp_item,items,mListener1,mListener2);
	                ListView dailyList=(ListView)findViewById(R.id.daily_list);
	                dailyList.setAdapter(adapter);
		         }
		     };
    private MyClickListener mListener2 = new MyClickListener() {
		         @Override
		       public void myOnClick(View v) {
		        	 SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
			         db.delete("Items", "id=?", new String[]{""+selectedId});
			         TDPitem item=items.get(selectedPosition);
		        		item.setState(TDPitem.SHOW);
		        		selectedId=item.getItemId();
			            
		        		initItems();
		        		TdpAdapter adapter=new TdpAdapter(DailyList.this,R.layout.tdp_item,items,mListener1,mListener2);
		                ListView dailyList=(ListView)findViewById(R.id.daily_list);
		                dailyList.setAdapter(adapter);
			        	
		         }
		     };
	private void initItems(){
		items.clear();
		SQLiteDatabase db = dbHelperForButler.getWritableDatabase();
		
        Cursor cursor=db.query("Items",null, "month=? and day=? and user=?", new String[]{""+Month,""+Day,""+com.des.butler.MainActivity.userId}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				int id=cursor.getInt(cursor.getColumnIndex("id"));
				String title=cursor.getString(cursor.getColumnIndex("title"));
				String detail=cursor.getString(cursor.getColumnIndex("detail"));
				int imageId=cursor.getInt(cursor.getColumnIndex("image"));
				String time=cursor.getString(cursor.getColumnIndex("time"));
				if (imageId==R.drawable.none){
					imageId=0;
				}
				TDPitem item=new TDPitem(id,title,detail,imageId,time,TDPitem.SHOW);
				
				items.add(item);
				
			}while(cursor.moveToNext());
		}
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daily_list_menu, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        int id = item.getItemId();
        
    	if (id == R.id.daily_list_action_add) {
    		Intent intent=new Intent("com.des.butler.ACTION_START");
    		intent.addCategory("com.des.butler.ITEMEDIT");
    	    intent.putExtra("Day", Day);
    	    intent.putExtra("Month", Month);
    	    intent.putExtra("Year", Year);
    	    int zero=0;
    	    intent.putExtra("Id", zero);
    		startActivity(intent);
    		finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
