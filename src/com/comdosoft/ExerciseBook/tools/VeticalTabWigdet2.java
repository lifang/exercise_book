package com.comdosoft.ExerciseBook.tools;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class VeticalTabWigdet2 extends TabWidget {


	Resources res;
	public VeticalTabWigdet2(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = context.getResources();
		setOrientation(LinearLayout.VERTICAL);
	}

	
	public void addView(View child) {
		LinearLayout.LayoutParams lp = new LayoutParams(314, 141);
//		LinearLayout.LayoutParams lp = new LayoutParams(114, 101);
		lp.setMargins(0, 0, 0, 0);
		child.setLayoutParams(lp);
		super.addView(child);
	}

}
