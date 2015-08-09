package com.ritterliu.angryBirdEleven;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class Map {

	float x,y;
	
	Type type;
	float angle;
	
	public Map(float x,float y,Type type,float angle)
	{
		this.x=x;
		this.y=y;
		this.type=type;
		this.angle=angle;
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

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
}
