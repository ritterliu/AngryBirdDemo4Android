package com.ritterliu.angryBirdEleven;

import android.graphics.Bitmap;
import static com.ritterliu.angryBirdEleven.Constant.*;

/**物体生命值类*/
public class UserData {
	Bitmap bmpHigh,bmpMid,bmpLow;
	
	Type type;
	
	float initLife;
	
	float currentLife; 

	public UserData(Bitmap bmp,Type type)
	{
		this.bmpHigh=bmp;
		this.type=type;
	}

	public UserData(Bitmap bmpHigh,Bitmap bmpMid,Bitmap bmpLow,float life,Type type)
	{
		this.bmpHigh=bmpHigh;
		this.bmpMid=bmpMid;
		this.bmpLow=bmpLow;
		
		this.initLife=life;
		this.currentLife=life;
		
		this.type=type;
	}
	
	public Bitmap getBmp() {
		return bmpHigh;
	}

	public void setBmp(Bitmap bmp) {
		this.bmpHigh = bmp;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Bitmap getBmpHigh() {
		return bmpHigh;
	}

	public void setBmpHigh(Bitmap bmpHigh) {
		this.bmpHigh = bmpHigh;
	}

	public Bitmap getBmpMid() {
		return bmpMid;
	}

	public void setBmpMid(Bitmap bmpMid) {
		this.bmpMid = bmpMid;
	}

	public Bitmap getBmpLow() {
		return bmpLow;
	}

	public void setBmpLow(Bitmap bmpLow) {
		this.bmpLow = bmpLow;
	}
	
	public float getInitLife() {
		return initLife;
	}

	public void setInitLife(float life) {
		this.initLife = life;
	}
	
	public float getCurrentLife() {
		return currentLife;
	}

	public void setCurrentLife(float currentLife) {
		this.currentLife = currentLife;
	}
	
}
