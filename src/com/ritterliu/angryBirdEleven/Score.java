package com.ritterliu.angryBirdEleven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Score {
	
	float x,y;

	Bitmap bmp;

	float score;
	boolean hasBeenDrawed;
	
	Matrix matrix;
	
	float localZoom=0f;
	boolean zoomToTop=false;
	
	public Score(float f,float g,Bitmap bmp,float score)
	{
		this.x=f;
		this.y=g;
		
		this.bmp=bmp;
		this.score=score;
		
		hasBeenDrawed=false;
		
		this.matrix =new Matrix();
		
	}
	
	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{

		matrix.setTranslate(translateX-bmp.getWidth()/2,translateY-bmp.getHeight());
		
		if(zoomToTop==false)
		{
			localZoom+=0.04;
			if(localZoom>1f)
			{
				zoomToTop=true;
				localZoom-=0.04;
			}
		}
		else
		{
			localZoom-=0.04;	
		}
		
		if(localZoom<0.01)
		{
			setHasBeenDrawed(true);
		}

		matrix.postScale(localZoom,localZoom,translateX,translateY-bmp.getHeight()/2);
		matrix.postScale(2f-zoom,2f-zoom,translateX,translateY-bmp.getHeight()/2);
		matrix.postScale(zoom,zoom,scaleX,scaleY);
	
		canvas.drawBitmap(bmp,matrix, paint);

	}
	
	
	
	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}
	
	
	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}
	

	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public boolean isHasBeenDrawed() {
		return hasBeenDrawed;
	}
	public void setHasBeenDrawed(boolean hasBeenDrawed) {
		this.hasBeenDrawed = hasBeenDrawed;
	}
	
	
}
