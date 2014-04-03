package com.comdosoft.ExerciseBook.pojo;

import android.widget.ImageView;

public class MoveLRPojo {

	private ImageView leftMove;
	private ImageView rightMove;
	
	public MoveLRPojo(){}
	
	public MoveLRPojo(ImageView leftMove, ImageView rightMove) {
		super();
		this.leftMove = leftMove;
		this.rightMove = rightMove;
	}

	public ImageView getLeftMove() {
		return leftMove;
	}

	public void setLeftMove(ImageView leftMove) {
		this.leftMove = leftMove;
	}

	public ImageView getRightMove() {
		return rightMove;
	}

	public void setRightMove(ImageView rightMove) {
		this.rightMove = rightMove;
	}
}
