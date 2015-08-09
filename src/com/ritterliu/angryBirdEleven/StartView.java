package com.ritterliu.angryBirdEleven;



import com.ritterliu.angryBird11.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class StartView extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	
	StartViewLogicThread logicThread;
	StartViewDrawThread drawThread;
	
	AngryBirdActivity activity;
	
	Button btnPlay,btnExit,btnSound,btnInfo;
	Container con;
	Bird bird;
	
	int birdTurnAngle=0;
	
	public StartView(AngryBirdActivity activity) {
		super(activity);
		this.activity=activity;
		
		sfh=this.getHolder();
		sfh.addCallback(this);
		
		canvas =new Canvas();
		paint=new Paint();
		paint.setAntiAlias(true);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		btnPlay=new Button(SCREEN_WIDTH/2f-Constant.bmpPlay.getWidth()/2,SCREEN_HEIGHT/2f-Constant.bmpPlay.getHeight()/2,Constant.bmpPlay.getWidth(),Constant.bmpPlay.getHeight(),Constant.bmpPlay);
		con=new Container(0, 0,Constant.bmpBackground.getWidth(),Constant.bmpBackground.getHeight(), Constant.bmpBackground);
		bird=new Bird(100,100,Constant.bmpStartViewBird.getHeight()/2, Constant.bmpStartViewBird,Type.redBird);
		
		btnExit=new Button(SCREEN_WIDTH-bmpExit.getWidth(),0,SCREEN_HEIGHT-bmpExit.getWidth(),bmpExit.getHeight(),bmpExit);
		
		btnSound=new Button(0,SCREEN_HEIGHT-(int)(1.5*bmpSoundOn.getHeight()),bmpSoundOn.getWidth(),bmpSoundOn.getHeight(),bmpSoundOn);
		btnInfo=new Button(0,SCREEN_HEIGHT-2*bmpSoundOn.getHeight()-bmpInfo.getHeight(),bmpInfo.getWidth(),bmpInfo.getHeight(),bmpInfo);
		

		START_VIEW_LOGIC_THREAD_FLAG=true;
		START_VIEW_DRAW_THREAD_FLAG=true;
		
		if(logicThread==null)
		{
			logicThread=new StartViewLogicThread(this);
			logicThread.start();
		}
		
		if(drawThread==null)
		{
			drawThread=new StartViewDrawThread(this);
			drawThread.start();
		}
		
	

		if(currentSoundState==SoundState.SOUND_OFF)
		{
			btnSound.setBmp(bmpSoundOff);
			if(activity.mediaPlayer.isPlaying())
			{
				activity.mediaPlayer.pause();
			}
		}
		else if(currentSoundState==SoundState.SOUND_ON)
		{

			btnSound.setBmp(bmpSoundOn);
			if(!activity.mediaPlayer.isPlaying())
			{
				activity.mediaPlayer.start();
			}
		}
	}

	
	public void logic()
	{

		if(currentSoundState==SoundState.SOUND_ON)
		{
			bird.setAngle(birdTurnAngle++);
		}
		
		if(birdTurnAngle>360)
		{
			birdTurnAngle=0;
		}
	}
	
	public void draw()
	{
		try
		{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				con.draw(canvas, paint);
				btnPlay.draw(canvas, paint);
				bird.draw(canvas, paint);
				btnExit.draw(canvas, paint);
				btnSound.draw(canvas, paint);
				btnInfo.draw(canvas, paint);
				
				if(producerState)
				{
					Paint paintA=new Paint();
					paintA.setAlpha(0x77);
					canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paintA);
					canvas.drawBitmap(bmpProducer,SCREEN_WIDTH/2-bmpProducer.getWidth()/2,SCREEN_HEIGHT/2-bmpProducer.getHeight()/2, paint);
				}
				
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
			
			if(producerState)
			{
				producerState=!producerState;
			}
			else
			{
				if(btnPlay.isPressed(event))
				{
					activity.handler.sendEmptyMessage(1);
					START_VIEW_LOGIC_THREAD_FLAG=false;
					START_VIEW_DRAW_THREAD_FLAG=false; 
				}
				else if(btnExit.isPressed(event))
				{
					showDialog();
				}
				else if(btnSound.isPressed(event))
				{
					if(currentSoundState==SoundState.SOUND_OFF)
					{
						currentSoundState=SoundState.SOUND_ON;
						sp.edit().putInt("SAVE_SOUND", soundOn).commit();
						btnSound.setBmp(bmpSoundOn);
						
						if(!activity.mediaPlayer.isPlaying())
						{
							activity.mediaPlayer.start();
						}
						
					}
					else if(currentSoundState==SoundState.SOUND_ON)
					{
						currentSoundState=SoundState.SOUND_OFF;
						sp.edit().putInt("SAVE_SOUND", soundOff).commit();
						btnSound.setBmp(bmpSoundOff);
						
						if(activity.mediaPlayer.isPlaying())
						{
							activity.mediaPlayer.pause();
						}
					}
					
					
				}
				else if(btnInfo.isPressed(event))
				{
					if(!producerState)
					{
						producerState=!producerState;
					}
				}
			}
		}
		
		return true;
	}
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		START_VIEW_LOGIC_THREAD_FLAG=false;
		START_VIEW_DRAW_THREAD_FLAG=false;
		logicThread=null;
		drawThread=null;
		
		
	}
	
	
	 private void showDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);	
							builder.setIcon(R.drawable.icon);
				            builder.setTitle("你确定要离开吗？");
				            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int whichButton) {
				                	System.exit(0);
				                }
				            });
				            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int whichButton) {
				                }
				            });
				            
				            builder.create().show();
	   }
}
