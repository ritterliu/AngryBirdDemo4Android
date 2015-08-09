package com.ritterliu.angryBirdEleven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**ÈÝÆ÷Àà*/
public class Container {
	private int x,y,width,height;
	Bitmap bmp,bmp2;
	Matrix matrix;
	
	public Container(int x,int y,int width,int height,Bitmap bmp)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.bmp=bmp;
		matrix=new Matrix();
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{

		canvas.drawBitmap(bmp, x, y, paint);
	}
	
	public void draw(Canvas canvas,Paint paint,Matrix matrix)
	{

		canvas.drawBitmap(bmp,matrix, paint);
	}
	
	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{
		matrix.setTranslate(translateX,translateY);
		matrix.postScale(zoom, zoom,scaleX,scaleY);
		canvas.drawBitmap(bmp,matrix, paint);
		
	}
	

	
}
