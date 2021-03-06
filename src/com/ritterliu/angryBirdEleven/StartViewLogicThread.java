package com.ritterliu.angryBirdEleven;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class StartViewLogicThread extends Thread{
	
	StartView startView;
	
	public StartViewLogicThread(StartView startView)
	{
		this.startView=startView;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(START_VIEW_LOGIC_THREAD_FLAG)
		{
			long start=System.currentTimeMillis();
			startView.logic();
			long end=System.currentTimeMillis();
			try
			{
				if(end-start<START_VIEW_LOGIC_THREAD_SLEEPTIME)
				{
					Thread.sleep(START_VIEW_LOGIC_THREAD_SLEEPTIME-(end-start));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		}
	}
	
	
}
