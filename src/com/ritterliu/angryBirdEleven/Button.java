package com.ritterliu.angryBirdEleven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

/**ButtonÀà
 * 
 * */
public class Button {
	private float x,y;
	private float width,height;
	Bitmap bmp;
	
	Matrix matrix;
	
	public Button(float x,float y,float width,float height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public Button(float x,float y,float width,float height,Bitmap bmp)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.bmp=bmp;
		this.matrix=new Matrix();
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



	public float getWidth() {
		return width;
	}



	public void setWidth(float width) {
		this.width = width;
	}



	public float getHeight() {
		return height;
	}



	public void setHeight(float height) {
		this.height = height;
	}

	public void setBmp(Bitmap bmp)
	{
		this.bmp=bmp;
	}
	

	public boolean isPressed(MotionEvent event)
	{
		boolean res=false;
		
		if(event.getX()>=this.x&&event.getX()<=(this.x+this.width)&&event.getY()>=this.y&&event.getY()<=(this.y+this.height))
		{
			res=true;
		}
				
		return res;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(bmp, x, y, paint);
	}
	
	
	public void draw(Canvas canvas,Paint paint,Matrix m)
	{
		canvas.drawBitmap(bmp,m, paint);
	}
	
	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{
		matrix.setTranslate(translateX,translateY);
		matrix.postScale(zoom, zoom,scaleX,scaleY);
		canvas.drawBitmap(bmp,matrix, paint);
	}

}
