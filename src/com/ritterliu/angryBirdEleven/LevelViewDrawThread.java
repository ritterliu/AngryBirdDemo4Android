package com.ritterliu.angryBirdEleven;


import static com.ritterliu.angryBirdEleven.Constant.*;
/**玄关界面绘画线程*/
public class LevelViewDrawThread extends Thread{
	LevelView levelView;
	
	public LevelViewDrawThread(LevelView levelView)
	{
		this.levelView=levelView;
	}
	
	public void run()
	{
		while(LEVEL_VIEW_DRAW_THREAD_FLAG)
		{
			long start=System.currentTimeMillis();
			levelView.draw();
			long end=System.currentTimeMillis();
			try
			{
				if(end-start<LEVEL_VIEW_DRAW_THREAD_SLEEPTIME)
				{
					Thread.sleep(LEVEL_VIEW_DRAW_THREAD_SLEEPTIME-(end-start));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	

}
