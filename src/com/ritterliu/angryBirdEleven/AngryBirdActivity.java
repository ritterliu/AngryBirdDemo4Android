package com.ritterliu.angryBirdEleven;

import com.ritterliu.angryBird11.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class AngryBirdActivity extends Activity {
	/**橡皮筋长度*/
	public static int RubberBandLength=0;
	/**标准横竖坐标*/
	public static int startX;
	public static int startY;
	/**小鸟点击作用有效范围*/
	public static final int touchDistance=80;
	/**activity实例*/
	public static AngryBirdActivity instance;
	
	WhichView currentView;
	
	StartView startView;
	LevelView levelView;
	MySurfaceView surfaceView;
	
	/**声音变量定义*/
	MediaPlayer mediaPlayer;
	AudioManager am;
	SoundPool soundPool;
	
	/**应用内部通信，切换界面*/
	Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
					gotoStartView();					
					break;
				case 1:
					gotoLevelView();
					break;
				case 2:
					gotoGameView();
					break;
				default:
					gotoStartView();
						
			}
		}
	};
	
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        instance = this;
        
        /**得到存储的声音开关信息*/
        sp=getPreferences(MODE_PRIVATE);
        int  savedVolume=sp.getInt("SAVE_SOUND", soundOn);
        if(savedVolume!=soundOn)
        {
        	currentSoundState=SoundState.SOUND_OFF;
        }
        else
        {
        	currentSoundState=SoundState.SOUND_ON;
        }
        
        /**获取屏幕尺寸*/
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
        
        /**定义橡皮筋最大长度*/
        RubberBandLength=(int) (SCREEN_WIDTH/9);
        
        /**得到屏幕相对于标准屏幕的长宽比*/
        changeRatio();

        /**初始化图片*/
        initStartViewBitmap(getResources());        
           
        initLevelViewBitmap(getResources());
    
        initGameViewBitmap(getResources());
        
        /**初始化声音*/
        initSound();

        /**切换至开始界面*/
		gotoStartView();
		
		runInBack=false;
    }
    
    public void initSound()
    {
		mediaPlayer=MediaPlayer.create(this, R.raw.title_theme);
		mediaPlayer.setLooping(true);
		
		soundPool=new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		
		soundBirdCollision=soundPool.load(this, R.raw.bird_collision, 1);
		soundBirdFlying=soundPool.load(this, R.raw.bird_flying, 1);
		soundBirdSinging1=soundPool.load(this, R.raw.bird_singing_1, 1);
		soundBirdSinging2=soundPool.load(this, R.raw.bird_singing_2, 1);
		soundLevelComplete=soundPool.load(this, R.raw.level_complete, 1);
		soundPigDestory=soundPool.load(this, R.raw.pig_destory, 1);
		soundPigSinging1=soundPool.load(this, R.raw.pig_singing_1, 1);
		soundPigSinging2=soundPool.load(this, R.raw.pig_singing_2, 1);
		soundPigSinging3=soundPool.load(this, R.raw.pig_singing_3, 1);
		soundSlingshotStreched=soundPool.load(this, R.raw.slingshot_streched, 1);
		soundWoodCollision=soundPool.load(this, R.raw.wood_collision, 1);
		soundWoodDestory=soundPool.load(this, R.raw.wood_destory, 1);
		
		am = (AudioManager) AngryBirdActivity.instance.getSystemService(Context.AUDIO_SERVICE);
		/**设置当前调整音量大小只是针对媒体音乐进行调整*/
		AngryBirdActivity.instance.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		currentVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2;
		maxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume ,AudioManager.FLAG_PLAY_SOUND);
    }
    
    /**开始界面*/
    public void gotoStartView()
    {
    	startView=new StartView(this);
        setContentView(startView);
        currentView=WhichView.START_VIEW;  
    }
    /**选关界面*/
    public void gotoLevelView()
    {
    	levelView=new LevelView(this);
    	setContentView(levelView);
    	currentView=WhichView.LEVEL_VIEW;
    }
    
    /**游戏界面*/
    public void gotoGameView()
    {
    	surfaceView=new MySurfaceView(this);
        setContentView(surfaceView);
        currentView=WhichView.GAME_VIEW;  
    }
    
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {    	
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{	
    		if(currentView==WhichView.START_VIEW)
    		{
        		showDialog();
    		}
    		else if(currentView==WhichView.LEVEL_VIEW)
    		{
    			GAME_VIEW_LOGIC_THREAD_FLAG=false;
    			GAME_VIEW_DRAW_THREAD_FLAG=false;
    			gotoStartView();
    		}
    		else if(currentView==WhichView.GAME_VIEW)
    		{
    			LEVEL_VIEW_DRAW_THREAD_FLAG=false;
    			gotoLevelView();
    		}
    	}    	
    	else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP)
    	{
    		if(currentVolume<am.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
    		{
    			currentVolume++;
    			am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume ,AudioManager.FLAG_PLAY_SOUND);
    		}
    	}
    	else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN)
    	{
    		if(currentVolume>0)
    		{
    			currentVolume--;
    			am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume ,AudioManager.FLAG_PLAY_SOUND);
    		}
    	}

    	return true;
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	Log.d("myLog","onResume");
    	
    	if(currentSoundState==SoundState.SOUND_ON)
    	{
    		if(currentView==WhichView.START_VIEW||currentView==WhichView.LEVEL_VIEW)
    		{
        		if(!mediaPlayer.isPlaying())
        		{
        			mediaPlayer.start();
        		}
    		}

    	}
    	
    	runInBack=false;
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	Log.d("myLog","onPause()");
    	if(mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
		}
    	runInBack=true;

    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
    	if(mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
		}
    	runInBack=true;

    }
    
    /**系统对话框*/
	private void showDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	
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