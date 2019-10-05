package com.des.butler;


import java.text.DecimalFormat;
import java.util.Date;

import com.des.butler.tools.ButlerActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


public class Calendar extends ButlerActivity {
	private int selectedDay;
	private int selectedMonth;
	private int selectedYear;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        getWindow().setStatusBarColor(com.des.butler.MainActivity.themeColor);
        Drawable drawable = getResources().getDrawable(R.drawable.bar);
        getActionBar().setBackgroundDrawable(drawable);
        
        java.util.Calendar now =java.util.Calendar.getInstance();
		selectedDay=now.get(java.util.Calendar.DAY_OF_MONTH);
		selectedMonth=now.get(java.util.Calendar.MONTH)+1;
		selectedYear=now.get(java.util.Calendar.YEAR);
        
        final CalendarView calendar1=(CalendarView)findViewById(R.id.calendarView_1);
        
        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
            	month=month+1;
            	Toast.makeText(Calendar.this,year + "Äê" + month + "ÔÂ" + dayOfMonth + "ÈÕ",Toast.LENGTH_SHORT).show();
            	selectedDay=dayOfMonth;
            	selectedMonth=month;
            	selectedYear=year;
            }
        });
        Button select=(Button)findViewById(R.id.select_date);
        select.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent=new Intent("com.des.butler.ACTION_START");
				intent.addCategory("com.des.butler.DAYLIST");
			    intent.putExtra("Day", selectedDay);
			    intent.putExtra("Month", selectedMonth);
			    intent.putExtra("Year", selectedYear);
				startActivity(intent);
				
			}
		});

    }
}
