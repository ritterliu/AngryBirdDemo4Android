package com.ritterliu.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.ritterliu.angryBirdEleven.UserData;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Rectangle extends Physics {
	private PolygonShape polygonShape;
	private float halfW;
	private float halfH;

	UserData userData;

	public Rectangle(World world, float x, float y, float halfWidth,
			float halfHeight, float density, float friction, float restitution) {
		// TODO Auto-generated constructor stub
		polygonShape = new PolygonShape();
		
		halfW = halfWidth/30f;
		halfH = halfHeight/30f;
		polygonShape.setAsBox(halfW, halfH);

		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		fDef.shape=polygonShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		bodyDef.position.set((x+halfWidth)/30f,(y+halfHeight)/30f );
		
		body = world.createBody(bodyDef);
		body.createFixture(fDef);

	}
	
	public Rectangle(World world, float x, float y, float halfWidth,
			float halfHeight, float density, float friction, float restitution,UserData userData) {
		// TODO Auto-generated constructor stub
		polygonShape = new PolygonShape();

		halfW = halfWidth/30f;
		halfH = halfHeight/30f;
		polygonShape.setAsBox(halfW, halfH);

		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		fDef.shape=polygonShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		bodyDef.position.set((x+halfWidth)/30f,(y+halfHeight)/30f );
		
		body = world.createBody(bodyDef);
		body.m_userData=userData;
		body.createFixture(fDef);
		
		this.bitmap=userData.getBmp();
		this.userData=userData;

	}

	public Rectangle(World world, float x, float y, float halfWidth,
			float halfHeight ,float rotate, float density, float friction, float restitution) {
		// TODO Auto-generated constructor stub
		polygonShape = new PolygonShape();
		
		halfW = halfWidth/30f;
		halfH = halfHeight/30f;
		polygonShape.setAsBox(halfW, halfH);
		
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		fDef.shape=polygonShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		bodyDef.angle=(rotate* DEG_TO_RAD) % 360;
		bodyDef.position.set((x+halfWidth)/30f,(y+halfHeight)/30f );
		
		body = world.createBody(bodyDef);
		body.createFixture(fDef);
	}
	
	public Rectangle(World world, float x, float y, float halfWidth,
			float halfHeight ,float rotate, float density, float friction, float restitution,UserData userData) {
		// TODO Auto-generated constructor stub
		polygonShape = new PolygonShape();
		
		halfW = halfWidth/30f;
		halfH = halfHeight/30f;
		polygonShape.setAsBox(halfW, halfH);
	
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		fDef.shape=polygonShape;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		bodyDef.angle=(rotate* DEG_TO_RAD) % 360;
		bodyDef.position.set((x+halfWidth)/30f,(y+halfHeight)/30f );
		
		body = world.createBody(bodyDef);
		body.m_userData=userData;
		body.createFixture(fDef);

		this.bitmap=userData.getBmp();
		this.userData=userData;

	}
	
	public float getX()
	{
		return body.getPosition().x*30f;
	}
	
	public float getY()
	{
		return body.getPosition().y*30f;
	}
	
	public float getHalfW()
	{
		return halfW*30f;
	}
	
	public float getHalfH()
	{
		return halfH*30f;
	}
	
	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	public void setPolygonShape(PolygonShape polygonShape) {
		this.polygonShape = polygonShape;
	}

	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{
		matrix.setTranslate(translateX-getHalfW(),translateY-getHalfH());
		matrix.postRotate((body.getAngle() * RAD_TO_DEG) % 360,translateX,translateY);
		matrix.postScale(zoom, zoom,scaleX,scaleY);

		if(userData.getBmpLow()!=null&&userData.getBmpMid()!=null)
		{
			if(userData.getCurrentLife()>userData.getInitLife()*2f/3f)
			{
				canvas.drawBitmap(userData.getBmpHigh(),matrix, paint);
			}
			else if(userData.getCurrentLife()>userData.getInitLife()/3f)
			{
				canvas.drawBitmap(userData.getBmpMid(),matrix, paint);	
			}
			else
			{
				canvas.drawBitmap(userData.getBmpLow(),matrix, paint);
			}
		}
		else
		{
			canvas.drawBitmap(userData.getBmpHigh(),matrix, paint);
		}
				
	}
	
	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	

}