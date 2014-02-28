package com.comdosoft.ExerciseBook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ceshi extends Activity 
{
	private View stu_item;
	private View stu_cj;
	private boolean isStu_cj=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classstu_item);
		stu_item=findViewById(R.id.stu_item);
		stu_cj=findViewById(R.id.stu_cj);
		stu_item.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isStu_cj)
				{
					stu_cj.setVisibility(View.VISIBLE);
					isStu_cj=false;
				}
				else
				{
					stu_cj.setVisibility(View.GONE);
					isStu_cj=true;
				}
			}
		});
	}
}
