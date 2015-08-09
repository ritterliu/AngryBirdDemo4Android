package com.ritterliu.angryBirdEleven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class PreView extends SurfaceView implements Callback,Runnable{

	SurfaceHolder sfh;
	Canvas canvas;
	Paint paint;
	
	Thread th;
	boolean flag;
	
	public PreView(Context context) {
		super(context);
		
		sfh=this.getHolder();
		sfh.addCallback(this);
		
		canvas =new Canvas();
		paint=new Paint();
		paint.setAntiAlias(true);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		initPreViewBitmap(getResources());
		
		PRE_VIEW_DRAW_THREAD_FLAG=true;
		
		if(th==null)
		{
			flag=true;
			th=new Thread(this);
			th.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag=false;
		th=null;
	}

	public void draw()
	{
		try
		{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				canvas.drawBitmap(bmpLogo,0,0, paint);
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
	
	
	public void run()
	{
		while(flag)
		{
			long start=System.currentTimeMillis();
			draw();
			long end=System.currentTimeMillis();
			try
			{
				if(end-start<50)
				{
					Thread.sleep(50-(end-start));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
