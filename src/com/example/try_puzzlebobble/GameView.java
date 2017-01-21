package com.example.try_puzzlebobble;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private boolean isGameRun = true;
	private Shooter shooter;
	private SurfaceHolder surfaceHolder;
	
	public static Bullet bullet;
	public List<Target> list = new ArrayList<Target>();
	public static LinkedList<Bullet> bullets;
	public static Bitmap bitmap_bullet;	
	public static Bitmap bitmap_onebullet = BitmapUtil.bobble;
	public static Bitmap[] normal_bullet;
	public static Bitmap[] frozen_bullet;
	public static Bitmap[] blind_bullet;
	public static Bitmap bitmap_win;
	public static Bitmap bitmap_loss;
	public static Target[][] targets;
	public static Random random;

	private List<Point> points ;
	private List<Point> added ;
	
	public static SoundPool sp;
	public static int[] spid;
	public static boolean soundFlag = true;
	private int firstX ,firstY;
	public static int n;
	public static int[][][]position;
	private boolean flag;
	
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		bullets = new LinkedList<Bullet>();
		points = new ArrayList<Point>();
		added = new ArrayList<Point>();
		normal_bullet = new Bitmap[8];
		frozen_bullet = new Bitmap[8];
		blind_bullet = new Bitmap[8];
		initBullet();
		random = new Random();
		bitmap_loss = BitmapFactory.decodeResource(getResources(), R.drawable.lose_panel);
		bitmap_win = BitmapFactory.decodeResource(getResources(), R.drawable.win_panel);
		bitmap_onebullet = BitmapFactory.decodeResource(getResources(),
				R.drawable.bubble_1);
		
		for(int line=0;;line++){
			if(CommonUtil.screenHeight*3/4.0 < CommonUtil.screenHeight/14.0+line*bitmap_onebullet.getHeight()*5/7.0){
				n = line-1;
				Log.e("n", n+"");
				break;
			}
		}
		
		firstX = CommonUtil.screenWidth/16;
		firstY = CommonUtil.screenHeight/14;
		position = new int[n][8][8];
		for(int i=0;i<position.length;i++){
			for(int j=0;j<position[0].length;j++){
				if(i%2 == 0) {
					position[i][j][0] = firstX + j*bitmap_onebullet.getWidth();
					position[i][j][1] =	firstY + i*bitmap_onebullet.getWidth()*5/6;
				}
				else if(i%2 ==1) {
					position[i][j][0] = firstX + j*bitmap_onebullet.getWidth() +bitmap_onebullet.getWidth()/2;
					position[i][j][1] =	firstY + i*bitmap_onebullet.getWidth()*5/6;
				}
//				System.out.println("position["+i+"]["+j+"]"+position[i][j][0]+","+position[i][j][1]);
			}
		}
//		bullet = new Bullet(bitmap_onebullet,1);
		init();
		
		int type1 = random.nextInt(100)%8+1;
		bullets.add(newBullet(type1));
		
//		bullet.setBitmap_bullet(bullets.get(0).getBitmap_bullet());
//		bullet.setType(bullets.get(0).getType());
		
		
		
		targets = new Target[position.length][position[0].length];
		for(int i=0;i< targets.length;i++){
			for(int j=0; j<targets[0].length;j++){
			targets[i][j] = new Target(null, 0, new Point(position[i][j][0], position[i][j][1]));
		}
		}

		for(int i=0;i<GameMap.map.length;i++){
			for(int j=0;j<GameMap.map[i].length;j++){
				targets[i][j].setBitmap_target(newBullet(GameMap.map[i][j]).getBitmap_bullet());
				targets[i][j].setType(newBullet(GameMap.map[i][j]).getType());
			}
		}
		int type2 = random.nextInt(100)%8+1;
		bullets.add(newBullet(type2));
		int type3 = random.nextInt(100)%8+1;
		bullets.add(newBullet(type3));
		System.out.println("bullet = "+bullet.getType());
		System.out.println(type1+"---"+type2+"---"+type3);
		for(int i=0;i<bullets.size();i++){
			System.out.println("bullets["+i+"]="+bullets.get(i).getType());
		}
		flag = true;

//		System.out.println("hunter = "+bitmap_shooter.getWidth()+","+bitmap_shooter.getHeight());
//		System.out.println("bullet = "+bitmap_bullet.getWidth()+","+bitmap_bullet.getHeight());
		
		shooter = new Shooter();
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float touchX = event.getX();
		float touchY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			shooter.desginAngelByTouchXY(touchX, touchY);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

		}
		return true;
	}

	private void move() {

	}

	@SuppressLint("WrongCall")
	private void draw() {
		Canvas canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.WHITE);
		
		Paint paint = null;
		
//		gaming.myDraw(canvas);

		bullet.onDraw(canvas);
		shooter.onDraw(canvas);

		for (int i = 0; i < targets.length; i++) {
			for (int j = 0; j < targets[0].length; j++) {
				if (targets[i][j].getType() != 0) {
					targets[i][j].MyDraw(canvas, paint);
				}
			}
		}

		if (isLoss()) {
			if (soundFlag) {
				AudioUtil.playSound(R.raw.lose);
				AudioUtil.playSound(R.raw.noh);
			}
			for (int i = 0; i < targets.length; i++) {
				for (int j = 0; j < targets[0].length; j++) {
					if (targets[i][j].getType() != 0) {
						targets[i][j]
								.setBitmap_target(frozen_bullet[targets[i][j]
										.getType() - 1]);
						targets[i][j].MyDraw(canvas, paint);
					}
				}
			}
			for (int i = 0; i < bullets.size(); i++) {
//				bullets.get(i).setBitmap_bullet(
//						frozen_bullet[bullets.get(i).getType() - 1]);
			}
			canvas.drawBitmap(bitmap_loss, 0,
					CommonUtil.screenHeight / 2 - bitmap_loss.getHeight() / 2, paint);
			Bullet.isshoot = true;

		}

		if (isWin()) {
			if (soundFlag) {
				AudioUtil.playSound(R.raw.applause);
			}
			canvas.drawBitmap(bitmap_win, 0,
					CommonUtil.screenHeight / 2 - bitmap_win.getHeight() / 2, paint);
			Bullet.isshoot = true;
		}

		surfaceHolder.unlockCanvasAndPost(canvas);

	}

	Thread gameThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isGameRun) {
				move();
				draw();
			}
		}
	});
	
	private void init() {
		// TODO Auto-generated method stub
		
		shooter = new Shooter();
		
	}
	
	private void initBullet() {
		// TODO Auto-generated method stub
		for(int i=0;i<8;i++){
			normal_bullet[i] = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_1+i);
//			frozen_bullet[i] = BitmapFactory.decodeResource(getResources(), R.drawable.frozen_1+i);
//			blind_bullet[i] = BitmapFactory.decodeResource(getResources(), R.drawable.blind_1+i);
		}
		
	}
	
	public Bullet newBullet(int type){
		if(type >0){
			if(CommonUtil.isBlind == false){
		bullet = new Bullet(type);
			}
			else {
//		bullet = new Bullet(blind_bullet[type-1], type);
			}
		}
		
		return bullet;
	}

	private boolean isLoss(){
		boolean isLoss = false;
		for(int j=0;j<targets[0].length;j++){
			if(targets[position.length-1][j].getType() != 0){
				isLoss = true;
				break;
			}
		}
		return isLoss;
	}
	
	private boolean isWin(){
		boolean isWin = true;
		for(int i=0;i<targets.length;i++){
			for(int j=0;j<targets[0].length;j++){
				if(targets[i][j].getType() != 0){
					isWin = false;
					break;
				}
				else isWin = true;
			}
			if(isWin == false){
				break;
			}
		}
		return isWin;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
