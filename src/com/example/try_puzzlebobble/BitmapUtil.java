package com.example.try_puzzlebobble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	static Context context;
	
	public static Bitmap shooter;
	public static Bitmap gameBg;
	public static Bitmap bobble;
	
	public static void initBitmap(Context context){
		BitmapUtil.context = context;
		initBitmap();
	}
	
	private static void initBitmap(){
		shooter = BitmapFactory.decodeResource(context.getResources(), R.drawable.shooter, null);
		gameBg = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.background);
		gameBg = Bitmap.createBitmap(gameBg, gameBg.getWidth()/4, 0, gameBg.getWidth()/2,gameBg.getHeight());
		bobble = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_1);
	}
	
}
