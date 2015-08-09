package com.ritterliu.angryBirdEleven;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil 
{
	public static Bitmap loadBM(Resources res,int id)
	{
		return BitmapFactory.decodeResource(res,id);
	}
	
	//����ͼƬ�ķ���
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//����ͼƬ�ķ���
	{
	   	float width = bm.getWidth(); //ͼƬ���
	   	float height = bm.getHeight();//ͼƬ�߶�
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(xRatio, yRatio);   
	   	
	   	Bitmap bmResult = null ;
	   	try
	   	{
	   		bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ 
	   	}
	   	catch(Exception ex)
	   	{
	   		ex.printStackTrace();
	   	}
	   	 
	   	return bmResult;
	}
}
