package com.ritterliu.angryBirdEleven;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import static com.ritterliu.angryBirdEleven.Constant.*;

/** bird类，用于响应拖拽小鸟等事件
 * 
 * */
public class Bird {
	private int x, y, r;
	private int angle;
	Bitmap bmp;

	private boolean isPressed;
	private boolean isReleased;
	private boolean applyForce;

	Type type;

	Matrix matrix;

	boolean firstContact = false;

	boolean isInitAnimation = false;
	float animationAngle = 0;

	boolean isContacted = false;

	private int drawX, drawY;

	State state;

	public Bird(int x, int y, int r, Bitmap bmp, Type type) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.bmp = bmp;

		isReleased = false;

		applyForce = false;

		this.type = type;

		this.matrix = new Matrix();

		this.state = State.ON_GROUND;

	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isFirstContact() {
		return firstContact;
	}

	public void setFirstContact(boolean firstContact) {
		this.firstContact = firstContact;
	}

	public int getDrawX() {
		return drawX;
	}

	public void setDrawX(int drawX) {
		this.drawX = drawX;
	}

	public int getDrawY() {
		return drawY;
	}

	public void setDrawY(int drawY) {
		this.drawY = drawY;
	}

	public boolean isContacted() {
		return isContacted;
	}

	public void setContacted(boolean isContacted) {
		this.isContacted = isContacted;
	}

	public float getAnimationAngle() {
		return animationAngle;
	}

	public void setAnimationAngle(float animationAngle) {
		this.animationAngle = animationAngle;
	}

	public boolean isInitAnimation() {
		return isInitAnimation;
	}

	public void setInitAnimation(boolean isInitAnimation) {
		this.isInitAnimation = isInitAnimation;
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean getApplyForce() {
		return this.applyForce;
	}

	public void setApplyForce(boolean isApplyForce) {
		this.applyForce = isApplyForce;
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

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public Type getType() {
		return this.type;
	}

	public void draw(Canvas canvas, Paint paint) {

		canvas.save();
		canvas.rotate(angle, this.x, this.y);

		canvas.drawBitmap(this.bmp, this.x - this.r, this.y - this.r, paint);
		canvas.restore();

	}

	
	public void draw(Canvas canvas, Paint paint, float translateX,
			float translateY, float scaleX, float scaleY, float zoom) {
		matrix.setTranslate(translateX, translateY);

		matrix.postTranslate(-this.r, -this.r);

		matrix.postRotate(angle, translateX, translateY);

		matrix.postScale(zoom, zoom, scaleX, scaleY);

		canvas.drawBitmap(bmp, matrix, paint);

	}

	public boolean getIsReleased() {
		return this.isReleased;
	}

	public void setIsReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	public boolean getIsPressed() {
		return this.isPressed;
	}

	public void setIsPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public boolean isPressed(MotionEvent event) {
		boolean res = false;
		if (Math.pow((event.getX() - this.x), 2)
				+ Math.pow((event.getY() - this.y), 2) < Math.pow(
				AngryBirdActivity.touchDistance, 2)) {
			res = true;
		}
		return res;
	}

	public boolean isPressed(Vec2 event) {
		boolean res = false;
		if (Math.pow((event.x - this.x), 2) + Math.pow((event.y - this.y), 2) < Math
				.pow(AngryBirdActivity.touchDistance, 2)) {
			res = true;
		}
		return res;
	}

	public void move(MotionEvent event) {
		if (Math.pow((event.getX() - AngryBirdActivity.startX), 2)
				+ Math.pow((event.getY() - AngryBirdActivity.startY), 2) <= Math
				.pow(AngryBirdActivity.RubberBandLength, 2)) {
			this.x = (int) event.getX();
			this.y = (int) event.getY();
		} else {
			float angle = (float) Math.atan2(event.getY()
					- AngryBirdActivity.startY, event.getX()
					- AngryBirdActivity.startX);

			this.x = (int) (AngryBirdActivity.startX + AngryBirdActivity.RubberBandLength
					* Math.cos(angle));
			this.y = (int) (AngryBirdActivity.startY + AngryBirdActivity.RubberBandLength
					* Math.sin(angle));

		}
	}

	public void move(Vec2 event) {

		if (Math.pow((event.x - AngryBirdActivity.startX), 2)
				+ Math.pow((event.y - AngryBirdActivity.startY), 2) <= Math
				.pow(AngryBirdActivity.RubberBandLength, 2)) {
			this.x = (int) event.x;
			this.y = (int) event.y;
		} else {
			float angle = (float) Math.atan2(
					event.y - AngryBirdActivity.startY, event.x
							- AngryBirdActivity.startX);

			this.x = (int) (AngryBirdActivity.startX + AngryBirdActivity.RubberBandLength
					* Math.cos(angle));
			this.y = (int) (AngryBirdActivity.startY + AngryBirdActivity.RubberBandLength
					* Math.sin(angle));

		}


	}
}
