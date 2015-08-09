package com.ritterliu.physics;


import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.ritterliu.angryBirdEleven.Bird;
import com.ritterliu.angryBirdEleven.UserData;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


public class Polygon extends Physics {

	private PolygonShape polygonShape;
	
	private List<Vec2> list;
	
	Bird bird;
	
	int screenX,screenY;

	public Polygon(World world, float x, float y, float density,
			float friction, float restitution) {
		// TODO Auto-generated constructor stub
		
		polygonShape=new PolygonShape();
		
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		
		bodyDef.position.x = x/30f;
		bodyDef.position.y = y/30f;
		body = world.createBody(bodyDef);
		
		list=new ArrayList();
	}

	public Polygon(World world, float x, float y, float density,
			float friction, float restitution,UserData userData) {
		// TODO Auto-generated constructor stub
		
		polygonShape=new PolygonShape();
		
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		
		bodyDef.position.set(x/30f,y/30f );
	
		body = world.createBody(bodyDef);
		body.m_userData=userData;
		list=new ArrayList();

		this.bitmap=userData.getBmp();
		
		Vec2 vertices[] = new Vec2[8];
		vertices[0] = new Vec2(this.bitmap.getWidth()/3f,this.bitmap.getHeight());
		vertices[1] = new Vec2(0f, this.bitmap.getHeight()*2/3f);
		vertices[2] = new Vec2(0f, this.bitmap.getHeight()/3f);
		vertices[3] = new Vec2(this.bitmap.getWidth()/3f, 0f);
		vertices[4] = new Vec2(this.bitmap.getWidth()*2/3f,0f);
		vertices[5] = new Vec2(this.bitmap.getWidth(),this.bitmap.getHeight()/3f);
		vertices[6] = new Vec2(this.bitmap.getWidth(), this.bitmap.getHeight()*2/3f);
		vertices[7] = new Vec2(this.bitmap.getWidth()*2/3f,this.bitmap.getHeight());
		add(vertices[0], false);
		add(vertices[1], false);
		add(vertices[2], false);
		add(vertices[3], false);		
		add(vertices[4], false);
		add(vertices[5], false);
		add(vertices[6], false);
		add(vertices[7], true);		

	}
	
	public Polygon(World world, float x, float y, float density,
			float friction, float restitution,UserData userData,Bird bird) {
		// TODO Auto-generated constructor stub
		
		polygonShape=new PolygonShape();
		
		fDef.density = density;
		fDef.friction = friction;
		fDef.restitution = restitution;
		
		bodyDef.type=(fDef.density==0f)?BodyType.STATIC:BodyType.DYNAMIC;//new
		
		bodyDef.position.set(x/30f,y/30f );
	
		body = world.createBody(bodyDef);
		body.m_userData=userData;
		list=new ArrayList();

		
		this.bitmap=userData.getBmp();
		
		Vec2 vertices[] = new Vec2[8];
		vertices[0] = new Vec2(this.bitmap.getWidth()/3f,this.bitmap.getHeight());
		vertices[1] = new Vec2(0f, this.bitmap.getHeight()*2/3f);
		vertices[2] = new Vec2(0f, this.bitmap.getHeight()/3f);
		vertices[3] = new Vec2(this.bitmap.getWidth()/3f, 0f);
		vertices[4] = new Vec2(this.bitmap.getWidth()*2/3f,0f);
		vertices[5] = new Vec2(this.bitmap.getWidth(),this.bitmap.getHeight()/3f);
		vertices[6] = new Vec2(this.bitmap.getWidth(), this.bitmap.getHeight()*2/3f);
		vertices[7] = new Vec2(this.bitmap.getWidth()*2/3f,this.bitmap.getHeight());
		add(vertices[0], false);
		add(vertices[1], false);
		add(vertices[2], false);
		add(vertices[3], false);		
		add(vertices[4], false);
		add(vertices[5], false);
		add(vertices[6], false);
		add(vertices[7], true);		
		
		this.bird=bird;
		
	}
	
	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public Bird getBird() {
		return bird;
	}

	public void setBird(Bird bird) {
		this.bird = bird;
	}
	
	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	public void setPolygonShape(PolygonShape polygonDef) {
		this.polygonShape = polygonShape;
	}

	/**
	 * 严格按顺时针依次添加，否则出现质量为负数导致物体不动等非物理现象
	 * @param vec2 点向量
	 * @param last 是否最后一个向量
	 */
	public void add(Vec2 vec2, boolean last) {
		list.add(vec2);
		if (last) {
			Vec2[] a=new Vec2[list.size()];
			for(int i=0;i<list.size();i++)
			{
				a[i]=(Vec2)list.get(i);
				a[i].x/=30f;
				a[i].y/=30f;
			}
			
			polygonShape.set(a,a.length);
			fDef.shape=polygonShape;
			body.createFixture(fDef);

		}
	}

	public float getX()
	{
		return body.getPosition().x*30f;
			
	}
	
	public float getY()
	{
		return body.getPosition().y*30f;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Vec2 vec2 = body.getPosition();
		float degrees = (body.getAngle() * RAD_TO_DEG) % 360;
		path.reset();
		for (int i = 0, j = 0; i < polygonShape.getVertexCount(); i++) {
		//	vertices[j++] = polygonShape.vertices.get(i).x + vec2.x;
		//	vertices[j++] = polygonShape.vertices.get(i).y + vec2.y;
			if (i == 0)
				path.moveTo(vertices[j - 2], vertices[j - 1]);
			else
				path.lineTo(vertices[j - 2], vertices[j - 1]);
		}
		path.close();
		matrix.reset();
		canvas.save();
		if (degrees != 0)
			matrix.preRotate(degrees, vec2.x, vec2.y);
		canvas.setMatrix(matrix);
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, vec2.x, vec2.y, null);
		} else {
			setColor(drawColor);
			canvas.drawPath(path, paint);
		}
		canvas.restore();
	}

	public void draw(Canvas canvas,Paint paint,float translateX,float translateY,float scaleX,float scaleY,float zoom)
	{
		matrix.setTranslate(translateX,translateY);
	
		matrix.postRotate((body.getAngle() * RAD_TO_DEG) % 360,translateX,translateY);
		
		matrix.postScale(zoom, zoom,scaleX,scaleY);
		
		canvas.drawBitmap(bitmap,matrix, paint);

	}

}