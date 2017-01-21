package com.example.try_puzzlebobble;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class Shooter {
	private int x, y;
	public static int shooterH, shooterW;
	public static float angel;

	public Shooter() {
		shooterW = BitmapUtil.shooter.getWidth();
		shooterH = BitmapUtil.shooter.getHeight();
		x = CommonUtil.screenWidth / 2;
		y = CommonUtil.screenHeight - shooterH / 3 * 2;

		angel = 0;
	}

	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(angel, x, y);
		canvas.drawBitmap(BitmapUtil.shooter, x - shooterW / 2, y - shooterH / 2, null);
		canvas.restore();
	}

	public void desginAngelByTouchXY(float touchX, float touchY) {
		if (touchY < y) {

			// angel = (float) ((float)
			// Math.atan((pressX-x)/(y-pressY))*(180/3.14));
			angel = (float) (Math.atan2((touchX - x), (y - touchY)) * 180 / Math.PI);
			// System.out.println(x+","+
			// y+"   "+pressX+","+pressY+"   "+angel);

		}
	}
}
