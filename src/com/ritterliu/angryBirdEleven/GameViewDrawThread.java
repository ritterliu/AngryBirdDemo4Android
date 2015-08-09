package com.ritterliu.angryBirdEleven;


import static com.ritterliu.angryBirdEleven.Constant.*;
/**游戏界面绘画线程*/
public class GameViewDrawThread extends Thread{
	MySurfaceView mySurfaceView;
	
	public GameViewDrawThread(MySurfaceView mySurfaceView)
	{
		this.mySurfaceView=mySurfaceView;
	}
	
	@Override
	public void run() {
		while(GAME_VIEW_DRAW_THREAD_FLAG)
		{
			long start=System.currentTimeMillis();
			mySurfaceView.draw();
			long end=System.currentTimeMillis();
			
			try
			{
				if(end-start<GAME_VIEW_DRAW_THREAD_SLEEPTIME)
				{
					Thread.sleep(GAME_VIEW_DRAW_THREAD_SLEEPTIME-(end-start));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	
	}

}
