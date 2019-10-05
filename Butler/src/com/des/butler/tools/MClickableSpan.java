package com.des.butler.tools;

import java.util.List;

import com.des.butler.tools.TdpAdapter.MyClickListener;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;

public class MClickableSpan extends ClickableSpan {
	private int id;
	public MClickableSpan(int picId){
		id=picId;
	}
	
	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub

	}

}
