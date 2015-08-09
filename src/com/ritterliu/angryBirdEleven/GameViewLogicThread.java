package com.ritterliu.angryBirdEleven;


import static com.ritterliu.angryBirdEleven.Constant.*;
/**游戏界面逻辑线程*/
public class GameViewLogicThread extends Thread{
	MySurfaceView mySurfaceView;
	
	public GameViewLogicThread(MySurfaceView mySurfaceView)
	{
		this.mySurfaceView=mySurfaceView;
	}
	
	@Override
	public void run() {
		while(GAME_VIEW_LOGIC_THREAD_FLAG)
		{
			long start=System.currentTimeMillis();
			mySurfaceView.logic();
			long end=System.currentTimeMillis();
			
			try
			{
				if(end-start<GAME_VIEW_LOGIC_THREAD_SLEEPTIME)
				{
					Thread.sleep(GAME_VIEW_LOGIC_THREAD_SLEEPTIME-(end-start));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	
	}

}
