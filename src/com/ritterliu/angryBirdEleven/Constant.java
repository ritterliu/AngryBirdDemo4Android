package com.ritterliu.angryBirdEleven;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.ritterliu.angryBird11.R;
/**常量类*/
public class Constant {
	
	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;
	
	public static float SCREEN_WIDTH_STANDARD=854;  //屏幕标准宽度	
	public static float SCREEN_HEIGHT_STANDARD=480;  //屏幕标准高度	


	public static boolean PRE_VIEW_DRAW_THREAD_FLAG;
	public static int PRE_VIEW_DRAW_THREAD_SLEEPTIME=50;
	
	public static boolean START_VIEW_LOGIC_THREAD_FLAG;
	public static boolean START_VIEW_DRAW_THREAD_FLAG;
	public static int START_VIEW_LOGIC_THREAD_SLEEPTIME=50;
	public static int START_VIEW_DRAW_THREAD_SLEEPTIME=50;
	
	public static boolean LEVEL_VIEW_DRAW_THREAD_FLAG;
	public static int LEVEL_VIEW_DRAW_THREAD_SLEEPTIME=50;
	
	public static boolean GAME_VIEW_LOGIC_THREAD_FLAG;
	public static boolean GAME_VIEW_DRAW_THREAD_FLAG;
	public static int GAME_VIEW_LOGIC_THREAD_SLEEPTIME=20;
	public static int GAME_VIEW_DRAW_THREAD_SLEEPTIME=25;
	
	public static float xMainRatio;//X缩放比例	
	public static float yMainRatio;//Y缩放比例
	
	
	public enum WhichView{
		START_VIEW,
		LEVEL_VIEW,
		GAME_VIEW,
	};
	
	public enum GameState{
		GAME_GOING,
		GAME_PAUSE,
		GAME_WIN,
		GAME_LOSE,
	};
	
	public enum State{
		ON_GROUND,
		JUMP_TO_SLINGSHOT,
		ON_SLINGSHOT,
		LAUNCHED
	};
	
	public enum Type {
		nothing,
		redBird,
		yellowBird,
		blueBird,
		ground,
		pig,
		wood,
		glass,
		stone,
		longBox,
		longWood,
	}
	
	/**操作提示标志位*/
	public static boolean HintState=false;
	/**作者信息标志位*/
	public static boolean producerState=false;
	
	public static int currentLevel=0;
	
	/**存储声音开关信息*/
	public static SharedPreferences sp;
	
	/**声音信息*/
	public static int soundOn=0;
	public static int soundOff=1;
	public static int currentVolume=0;
	public static float maxVolume=0;
	public static SoundState currentSoundState=SoundState.SOUND_ON;
	
	public static boolean runInBack=false;
	
	public enum SoundState
	{
		SOUND_ON,
		SOUND_OFF
	};
	
	public static int soundBirdCollision=1;
	public static int soundBirdFlying=2;
	public static int soundBirdSinging1=3;
	public static int soundBirdSinging2=4;
	public static int soundLevelComplete=5;
	public static int soundPigDestory=6;
	public static int soundPigSinging1=7;
	public static int soundPigSinging2=8;
	public static int soundPigSinging3=9;
	public static int soundSlingshotStreched=10;
	public static int soundWoodCollision=11;
	public static int soundWoodDestory=12;
	

	//是否已经执行过changeRatio方法 
	public static boolean changeRatioOkFlag=false;
	//动态自适应屏幕的方法
	public static void changeRatio()
	{
		if(changeRatioOkFlag)
		{
			return;
		}		
		changeRatioOkFlag=true;
		
		xMainRatio=SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;
		yMainRatio=SCREEN_HEIGHT/SCREEN_HEIGHT_STANDARD;

	}
	
	
	public static Bitmap bmpLogo;
	
	public static void initPreViewBitmap(Resources e)
	{
		bmpLogo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.logo),xMainRatio,yMainRatio);
		
	}
	
	public static Bitmap bmpPlay,bmpBackground,bmpStartViewBird,bmpExit,bmpInfo,bmpProducer;
	
	public static void initStartViewBitmap(Resources e)
	{
		/**仅对背景图片完全按照x,y等比缩放，其余图片均只按照y缩放，以保持长宽比*/
		bmpPlay=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.start_view_play),yMainRatio,yMainRatio);
		bmpBackground=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.start_view_background),xMainRatio,yMainRatio);
		bmpStartViewBird=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.start_view_small_bird),yMainRatio,yMainRatio);
		bmpExit=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.exit),yMainRatio,yMainRatio);
		bmpInfo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.info),yMainRatio,yMainRatio);
		bmpProducer=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.producer),yMainRatio,yMainRatio);
	}

	public static Bitmap bmpLevelViewBackground,bmpLevelViewBack,
	bmpLevelOne,bmpLevelTwo,bmpLevelThree,bmpLevelFour,bmpLevelFive,bmpLevelSix;
	
	public static void initLevelViewBitmap(Resources e)
	{
		bmpLevelViewBackground=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_view),xMainRatio,yMainRatio);
		bmpLevelViewBack=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_view_back),yMainRatio,yMainRatio);
		bmpLevelOne=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_1),yMainRatio,yMainRatio);
		bmpLevelTwo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_2),yMainRatio,yMainRatio);
		bmpLevelThree=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_3),yMainRatio,yMainRatio);
		bmpLevelFour=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_4),yMainRatio,yMainRatio);
		bmpLevelFive=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_5),yMainRatio,yMainRatio);
		bmpLevelSix=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.level_6),yMainRatio,yMainRatio);
	}
	
	
	public static Bitmap bmpPlus,bmpMinus,
	bmpPause,bmpRestart,bmpMenu,bmpSoundOn,bmpSoundOff,bmpHowToPlay,bmpBackToGame,bmpMainMenu,
	bmpGuide,bmpNext,
	bmpWinOne,bmpWinTwo,bmpWinThree,bmpWinFour,bmpWinFive,bmpWinSix,
	bmpLoseOne,bmpLoseTwo,bmpLoseThree,bmpLoseFour,bmpLoseFive,bmpLoseSix,
	bmpRedBird,bmpYellowBird,
	bmpLongBoxHHigh,bmpLongBoxHMid,bmpLongBoxHLow,
	bmpLongBoxVHigh,bmpLongBoxVMid,bmpLongBoxVLow,
	bmpLongWoodHigh,bmpLongWoodMid,bmpLongWoodLow,
	bmpPig,
	sky,bmpGround,bmpSlingshot,bmpGrass,
	bmp500,bmp5000, 
	bmpNum1,bmpNum2,bmpNum3,bmpNum4,bmpNum5,bmpNum6,
	bmpImgOne,bmpImgTwo,bmpImgThree;
	
	
	public static void initGameViewBitmap(Resources e)
	{
	    bmpPlus=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.plus),yMainRatio,yMainRatio);
	    bmpMinus=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.minus),yMainRatio,yMainRatio);
	    
	    bmpPause=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.pause),yMainRatio,yMainRatio);
	    bmpRestart=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.restart),yMainRatio,yMainRatio);
	    bmpMenu=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.menu),yMainRatio,yMainRatio);
	    bmpSoundOn=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.big_sound_on),yMainRatio,yMainRatio);
	    bmpSoundOff=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.big_sound_off),yMainRatio,yMainRatio);
	    bmpHowToPlay=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.how_to_play),yMainRatio,yMainRatio);
	    bmpBackToGame=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.back_to_game),yMainRatio,yMainRatio);
	    bmpMainMenu=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.main_menu_4),yMainRatio,yMainRatio);
	    
	    bmpGuide=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.guide),yMainRatio,yMainRatio);
	    bmpNext=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.next),yMainRatio,yMainRatio);
	    
	    
	    bmpWinOne=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_1),yMainRatio,yMainRatio);
	    bmpWinTwo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_2),yMainRatio,yMainRatio);
	    bmpWinThree=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_3),yMainRatio,yMainRatio);
	    bmpWinFour=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_4),yMainRatio,yMainRatio);
	    bmpWinFive=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_5),yMainRatio,yMainRatio);
	    bmpWinSix=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.win_6),yMainRatio,yMainRatio);
	    
	    bmpLoseOne=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.lose_1),yMainRatio,yMainRatio);
	    bmpLoseTwo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.lose_2),yMainRatio,yMainRatio);
	    bmpLoseThree=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.lose_3),yMainRatio,yMainRatio);
    
	    
		bmpRedBird=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.bird_red),yMainRatio,yMainRatio);
		bmpYellowBird=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.bird_yellow),yMainRatio,yMainRatio);
			
		bmpLongBoxHHigh=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_h_high),yMainRatio,yMainRatio);
		bmpLongBoxHMid=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_h_mid),yMainRatio,yMainRatio);
		bmpLongBoxHLow=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_h_low),yMainRatio,yMainRatio);
	
		bmpLongBoxVHigh=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_v_high),yMainRatio,yMainRatio);
		bmpLongBoxVMid=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_v_mid),yMainRatio,yMainRatio);
		bmpLongBoxVLow=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_box_v_low),yMainRatio,yMainRatio);
	
		bmpLongWoodHigh=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_wood_high),yMainRatio,yMainRatio);
		bmpLongWoodMid=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_wood_mid),yMainRatio,yMainRatio);
		bmpLongWoodLow=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.long_wood_low),yMainRatio,yMainRatio);
		  
	    bmpPig=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.pig),yMainRatio,yMainRatio);
	    
	    sky=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.sky_4),xMainRatio,yMainRatio);
	    bmpGround=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.game_ground),xMainRatio,yMainRatio);
	    bmpSlingshot=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.slingshot_h),yMainRatio,yMainRatio);
	    bmpGrass=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.grass),xMainRatio,yMainRatio);
	    
	    bmp500=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.score_500),yMainRatio,yMainRatio);
	    bmp5000=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.score_5000),yMainRatio,yMainRatio);
	
	    bmpNum1=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_1),yMainRatio,yMainRatio);
	    bmpNum2=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_2),yMainRatio,yMainRatio);
	    bmpNum3=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_3),yMainRatio,yMainRatio);
	    bmpNum4=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_4),yMainRatio,yMainRatio);
	    bmpNum5=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_5),yMainRatio,yMainRatio);
	    bmpNum6=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.num_big_6),yMainRatio,yMainRatio);
		
	    bmpImgOne=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.img_1),yMainRatio,yMainRatio);
	    bmpImgTwo=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.img_2),yMainRatio,yMainRatio);
	    bmpImgThree=PicLoadUtil.scaleToFitXYRatio(PicLoadUtil.loadBM(e,R.drawable.img_3),yMainRatio,yMainRatio);
		
	}
	
}
