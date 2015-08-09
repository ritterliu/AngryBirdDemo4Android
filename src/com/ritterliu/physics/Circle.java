package com.ritterliu.physics;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.ritterliu.angryBirdEleven.UserData;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;


public class Circle extends Physics{

	private  CircleShape circleShape;

	public Circle(World world, float x, float y, float radius, float density,
			float friction, float restitution) {
		// TODO Auto-generated constructor stub
		circleShape=new CircleShape ();
		circleShape.m_radius = radius/30f;
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		
		fDef.shape=circleShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		
		bodyDef.position.set(x/30f,y/30f );
		Log.d("myLog","CreateCircle");
		body = world.createBody(bodyDef);
		
		body.createFixture(fDef);  
		
	}

	public Circle(World world, float x, float y, float radius, float density,
			float friction, float restitution,UserData userData) {
		// TODO Auto-generated constructor stub
		circleShape=new CircleShape ();
		circleShape.m_radius = radius/30f;
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		
		fDef.shape=circleShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;
		bodyDef.allowSleep=true;

		bodyDef.position.set(x/30f,y/30f );
		Log.d("myLog","CreateCircle");
		body = world.createBody(bodyDef);
		
		body.m_userData=userData;
		
		body.createFixture(fDef);  
		this.bitmap=userData.getBmp();
	
	}
	
	public float getX()
	{
		return body.getPosition().x*30f;
	}
	
	public float getY()
	{
		return body.getPosition().y*30f;
	}
	
	public float getR()
	{
		return circleShape.m_radius*30f;
	}

	public Vec2 getPosition() {
		return body.getPosition();
	}

	public CircleShape getCircleShape() {
		return circleShape;
	}

	public void setCircleShape(CircleShape circleShape) {
		this.circleShape = circleShape;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Vec2 vec2 = getPosition();
		float degrees = (body.getAngle() * RAD_TO_DEG) % 360;
		matrix.reset();
		canvas.save();
		if (degrees != 0)
			matrix.preRotate(degrees, vec2.x, vec2.y);
		
		canvas.setMatrix(matrix);

		if(bitmap != null){
			canvas.drawBitmap(bitmap, vec2.x - circleShape.m_radius, vec2.y
					- circleShape.m_radius, paint);
		} else {
			setColor(drawColor);
			canvas.drawCircle(vec2.x, vec2.y, circleShape.m_radius, paint);
		}
		
		canvas.restore();
	}
	
	
	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{
		matrix.setTranslate(translateX,translateY);
	
		matrix.postTranslate(-getR(),-getR());
		
		matrix.postRotate((body.getAngle() * RAD_TO_DEG) % 360,translateX,translateY);
		
		matrix.postScale(zoom, zoom,scaleX,scaleY);
		
		canvas.drawBitmap(bitmap,matrix, paint);

	}


}
