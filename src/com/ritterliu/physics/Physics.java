package com.ritterliu.physics;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;


public abstract class Physics {

	public static final float DEG_TO_RAD = 0.0174532925f;
	public static final float RAD_TO_DEG = 57.2957795f;
	protected Paint paint;
	protected Matrix matrix;
	protected Path path;
	protected FixtureDef fDef;
	protected Body body;
	protected BodyDef bodyDef;
	protected float[] vertices;
	protected int drawColor = 0x000000;

	protected Bitmap bitmap;

	protected Physics() {
		fDef=new FixtureDef();
		bodyDef = new BodyDef();
		matrix = new Matrix();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		setColor(drawColor);
	}

	public int getDrawColor() {
		return drawColor;
	}

	public void setDrawColor(int drawColor) {
		this.drawColor = drawColor;
	}

	public void setColor(int color) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		paint.setColor(Color.rgb(red, green, blue));
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public FixtureDef getFixtureDef() {
		return fDef;
	}

	public void setFixtureDef(FixtureDef fDef) {
		this.fDef = fDef;
	}

	public BodyDef getBodyDef() {
		return bodyDef;
	}

	public void setBodyDef(BodyDef bodyDef) {
		this.bodyDef = bodyDef;
	}
	
	
	public float getX()
	{
		return body.getPosition().x*30f;
	}
	
	public float getY()
	{
		return body.getPosition().y*30f;
	}
	
	public void  setX(float x)
	{
		body.getPosition().x=x/30f;	
	}
	
	public void setY(float y)
	{
		body.getPosition().y=y/30f;
	}

	
	public abstract void draw(Canvas canvas);
	
	public abstract void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom);


}