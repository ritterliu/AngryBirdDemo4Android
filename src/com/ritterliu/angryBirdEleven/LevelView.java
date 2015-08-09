package com.ritterliu.angryBirdEleven;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.ritterliu.angryBirdEleven.Constant.*;
/**选关界面*/
public class LevelView extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	
	LevelViewDrawThread levelViewDrawThread;
	
	AngryBirdActivity activity;
	
	Button btnBack,
	btnLevelOne,btnLevelTwo,btnLevelThree,btnLevelFour,
	btnLevelFive,btnLevelSix;
	

	public LevelView(AngryBirdActivity activity) {
		super(activity);
		
		this.activity=activity;
		
		sfh=this.getHolder();
		sfh.addCallback(this);
		
		canvas =new Canvas();
		paint=new Paint();
		paint.setAntiAlias(true);
		
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		btnBack=new Button(0,SCREEN_HEIGHT-bmpLevelViewBack.getHeight(),bmpLevelViewBack.getWidth(),bmpLevelViewBack.getHeight(), bmpLevelViewBack);
		
		btnLevelOne=new Button(SCREEN_WIDTH/2-3*bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelOne);
		btnLevelTwo=new Button(SCREEN_WIDTH/2-(int)(0.5*bmpLevelOne.getWidth()),bmpLevelOne.getHeight(),bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelTwo);
		btnLevelThree=new Button(SCREEN_WIDTH/2+2*bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelThree);
		btnLevelFour=new Button(SCREEN_WIDTH/2-3*bmpLevelOne.getWidth(),bmpLevelOne.getHeight()*3,bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelFour);
		btnLevelFive=new Button(SCREEN_WIDTH/2-(int)(0.5*bmpLevelOne.getWidth()),bmpLevelOne.getHeight()*3,bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelFive);
		btnLevelSix=new Button(SCREEN_WIDTH/2+2*bmpLevelOne.getWidth(),bmpLevelOne.getHeight()*3,bmpLevelOne.getWidth(),bmpLevelOne.getHeight(),bmpLevelSix);
		
		
		LEVEL_VIEW_DRAW_THREAD_FLAG=true;
		
		if(levelViewDrawThread==null)
		{
			levelViewDrawThread=new LevelViewDrawThread(this);
			levelViewDrawThread.start();
		}
		
		if(currentSoundState==SoundState.SOUND_ON)
		{
			if(!activity.mediaPlayer.isPlaying())
			{
				activity.mediaPlayer.start();
			}
		}
		
	}
	
	public void draw()
	{
		try
		{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				canvas.drawBitmap(bmpLevelViewBackground,0,0, paint);
				btnBack.draw(canvas, paint);
				btnLevelOne.draw(canvas, paint);
				btnLevelTwo.draw(canvas, paint);
				btnLevelThree.draw(canvas, paint);
				btnLevelFour.draw(canvas, paint);
				btnLevelFive.draw(canvas, paint);
				btnLevelSix.draw(canvas, paint);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			if(btnBack.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				activity.handler.sendEmptyMessage(0);
				
			}
			else if(btnLevelOne.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=1;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
			else if(btnLevelTwo.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=2;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
			else if(btnLevelThree.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=3;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
			else if(btnLevelFour.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=4;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
			else if(btnLevelFive.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=5;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
			else if(btnLevelSix.isPressed(event))
			{
				LEVEL_VIEW_DRAW_THREAD_FLAG=false;
				currentLevel=6;
				activity.handler.sendEmptyMessage(2);
				if(activity.mediaPlayer.isPlaying())
				{
					activity.mediaPlayer.pause();
				}
				
			}
		}
		return true;
	}
	
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		LEVEL_VIEW_DRAW_THREAD_FLAG=false;
		levelViewDrawThread=null;
		
	}

}
