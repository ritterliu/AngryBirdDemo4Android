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
	/**��Ƥ���*/
	public static int RubberBandLength=0;
	/**��׼��������*/
	public static int startX;
	public static int startY;
	/**С����������Ч��Χ*/
	public static final int touchDistance=80;
	/**activityʵ��*/
	public static AngryBirdActivity instance;
	
	WhichView currentView;
	
	StartView startView;
	LevelView levelView;
	MySurfaceView surfaceView;
	
	/**������������*/
	MediaPlayer mediaPlayer;
	AudioManager am;
	SoundPool soundPool;
	
	/**Ӧ���ڲ�ͨ�ţ��л�����*/
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
        
        /**�õ��洢������������Ϣ*/
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
        
        /**��ȡ��Ļ�ߴ�*/
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
        
        /**������Ƥ����󳤶�*/
        RubberBandLength=(int) (SCREEN_WIDTH/9);
        
        /**�õ���Ļ����ڱ�׼��Ļ�ĳ����*/
        changeRatio();

        /**��ʼ��ͼƬ*/
        initStartViewBitmap(getResources());        
           
        initLevelViewBitmap(getResources());
    
        initGameViewBitmap(getResources());
        
        /**��ʼ������*/
        initSound();

        /**�л�����ʼ����*/
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
		/**���õ�ǰ����������Сֻ�����ý�����ֽ��е���*/
		AngryBirdActivity.instance.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		currentVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2;
		maxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume ,AudioManager.FLAG_PLAY_SOUND);
    }
    
    /**��ʼ����*/
    public void gotoStartView()
    {
    	startView=new StartView(this);
        setContentView(startView);
        currentView=WhichView.START_VIEW;  
    }
    /**ѡ�ؽ���*/
    public void gotoLevelView()
    {
    	levelView=new LevelView(this);
    	setContentView(levelView);
    	currentView=WhichView.LEVEL_VIEW;
    }
    
    /**��Ϸ����*/
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
    
    /**ϵͳ�Ի���*/
	private void showDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	
							builder.setIcon(R.drawable.icon);
				            builder.setTitle("��ȷ��Ҫ�뿪��");
				            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int whichButton) {
				                	System.exit(0);
				                }
				            });
				            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int whichButton) {
				                }
				            });
				            
				            builder.create().show();
	   }
    
}