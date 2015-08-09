package com.ritterliu.angryBirdEleven;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;

import com.ritterliu.angryBirdEleven.Constant.SoundState;
import com.ritterliu.physics.Physics;
import com.ritterliu.physics.Polygon;
import com.ritterliu.physics.Rectangle;

import static com.ritterliu.angryBirdEleven.Constant.*;

public class MySurfaceView extends SurfaceView implements Callback,ContactListener,OnGestureListener,OnTouchListener{

	AngryBirdActivity activity;
	
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	
	private int screenW,screenH;
	
	byte[] lock = new byte[0];
	private final int timePause=25;
	
	
	World world;
	Vec2 gravity;
	private final float RATE=30.0f;
	float timeStep=1f/60f;
	float longTimeStep=1f/40f;
	int velocityIterations = 10;
	int positionIterations = 8;

    
    Button btnPlus,btnMinus,btnPause,btnRestart,btnMenu,btnSound,
    btnHowToPlay,btnBackToGame,btnNext;
    
    private int offsetX,offsetY;
    
    
    private GestureDetector gesture;
    
    int pointX,pointY;
    int startX,startY;
    
    float zoom=1f;
    
    Container con,ground,slingshot,grass;
       
	private float oldRate=1.0f;
	private float oldDistance,newDistance;
	
	private boolean isFirstMultiTouch=true;
	
	boolean moveToLeft,moveToTop;
	
    
	Vec2 touchPoint=new Vec2();;
	Vec2 newTouchPoint=new Vec2();

	float forceRate=0;
	
	long startTime=System.nanoTime();
	int count;
	double fps;
	
	GameViewDrawThread drawThread;
	GameViewLogicThread logicThread;

	GameState currentGameState=GameState.GAME_GOING;
		
	Map[][] map;
	
	Rectangle rrground;
	
	List<Physics> physicsList=new ArrayList();
	List<Polygon> birdList=new ArrayList();
	List<Score> scoreList=new ArrayList();
	
	int currentIncrement=15;
	int oldIncrement;

	float oldZoom;
	boolean oldZoonInit=false;
	
	int pigSingingCount=0;
	int leftPigNumber;
	
	float deltaXa=0;
	float deltaYa=0;
	float angle=0;
	
	public MySurfaceView(AngryBirdActivity activity) {
		super(activity);
		this.activity=activity;
		
		sfh=this.getHolder();
		sfh.addCallback(this);
		
		canvas =new Canvas();
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1.0f);
		
		gravity=new Vec2(0,10f);
		world=new World(gravity, true);
		world.setContactListener(this);
		
		gesture=new GestureDetector(this);
		this.setOnTouchListener(this);

	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.screenW=this.getWidth();
		this.screenH=this.getHeight();
		
		if(screenW*screenH>=800*400)
		{
			forceRate=25;
		}
		else 
		{
			forceRate=50;
		}
		
		offsetX=sky.getWidth()-screenW-50;
		offsetY=sky.getHeight()-screenH*3/4-bmpGround.getHeight();
		
		btnPlus=new Button(20,50,bmpPlus.getWidth(),bmpPlus.getHeight(),bmpPlus);
		btnMinus=new Button(120,50,bmpMinus.getWidth(),bmpMinus.getHeight(),bmpMinus);
		
		btnPause=new Button(0,0,bmpPause.getWidth(),bmpPause.getHeight(),bmpPause);
		
		btnMenu=new Button(screenW/2-bmpMainMenu.getWidth()/4f-bmpMenu.getWidth(),
				screenH/2f+bmpMainMenu.getHeight()/2f-bmpMenu.getHeight()/2f,
				bmpMenu.getWidth(),
				bmpMenu.getHeight(),
				bmpMenu
				);
		
		btnRestart=new Button(screenW/2-bmpRestart.getWidth()/2,
				screenH/2f+bmpMainMenu.getHeight()/2f-bmpRestart.getHeight()/2f,
				bmpRestart.getWidth(),
				bmpRestart.getHeight(),
				bmpRestart
				);
		
		btnBackToGame=new Button(screenW/2+bmpMainMenu.getWidth()/4f,
				screenH/2f+bmpMainMenu.getHeight()/2f-bmpBackToGame.getHeight()/2f,
				bmpBackToGame.getWidth(),
				bmpBackToGame.getHeight(),
				bmpBackToGame
				);
		
		btnSound=new Button(screenW/2,
				screenH/2f-bmpSoundOn.getHeight()/2f,
				bmpSoundOn.getWidth(),
				bmpSoundOn.getHeight(),
				bmpSoundOn
				);
		
		btnHowToPlay=new Button(screenW/2+bmpMainMenu.getWidth()/4f,
				screenH/2f-bmpHowToPlay.getHeight()/2f,
				bmpHowToPlay.getWidth(),
				bmpHowToPlay.getHeight(),
				bmpHowToPlay
				);
		
		btnNext=new Button(screenW/2+bmpMainMenu.getWidth()/4f,
				screenH/2f+bmpMainMenu.getHeight()/2f-bmpNext.getHeight()/2f,
				bmpNext.getWidth(),
				bmpNext.getHeight(),
				bmpNext
				);
		
		if(currentSoundState==SoundState.SOUND_OFF)
		{
			btnSound.setBmp(bmpSoundOff);
		}
		else if(currentSoundState==SoundState.SOUND_ON)
		{
			btnSound.setBmp(bmpSoundOn);
		}
		
		con=new Container(-offsetX,-offsetY , sky.getWidth(), sky.getHeight(), sky);
		ground=new Container(con.getX()+offsetX,con.getY()+con.getHeight()*3/4+offsetY , bmpGround.getWidth(), bmpGround.getHeight(), bmpGround);
		grass=new Container(con.getX()+offsetX,con.getY()+con.getHeight()*3/4+offsetY-bmpGrass.getHeight(),bmpGrass.getWidth(),bmpGrass.getHeight(),bmpGrass);
		slingshot=new Container(con.getX()+offsetX+con.getWidth()/6,con.getY()+con.getHeight()*3/4+offsetY-bmpSlingshot.getHeight() , bmpSlingshot.getWidth(), bmpSlingshot.getHeight(), bmpSlingshot);
		
		oldRate=zoom;
		
		AngryBirdActivity.startX=slingshot.getX()+slingshot.getWidth()/2;
		AngryBirdActivity.startY=ground.getY()-slingshot.getHeight()+bmpRedBird.getHeight()*3/4;
		
		rrground=new Rectangle(world,0,ground.getY(),ground.getWidth()>>1, 5>>1,0f,1f,0.3f,new UserData(bmpGround,Type.ground));

		map=new Map[][]
		{
			//1
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3-bmpLongWoodHigh.getWidth()/2+bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()*1,ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()*2,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3-bmpLongWoodHigh.getWidth()/2+bmpLongBoxVHigh.getWidth()/2+bmpLongWoodHigh.getWidth()*2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()*2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpPig.getHeight(),Type.pig,0f),

			},
			//2
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+(int)(1.2*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+(int)(1.1*bmpLongWoodHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+2*bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+2*bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth()+2*bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2+2*bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+(int)(1.2*bmpLongBoxVHigh.getWidth())+2*bmpLongWoodHigh.getWidth(),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				
			},
			//3
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3-bmpLongWoodHigh.getWidth()/2+bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpPig.getHeight(),Type.pig,0f),
								
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()*2+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()*2+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()+(int)(1.2*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2+(int)(2*bmpLongBoxHHigh.getWidth()),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2+(int)(2*bmpLongBoxHHigh.getWidth())-bmpLongWoodHigh.getWidth()/2+bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()*2-bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth()/2-bmpLongBoxVHigh.getWidth()/2+(int)(2*bmpLongBoxHHigh.getWidth()),ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpPig.getHeight(),Type.pig,0f),
				
				
			},
			//4
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*3,AngryBirdActivity.startY,Type.redBird,0f),
								
				new Map(con.getWidth()*2/3-bmpLongBoxHHigh.getWidth()-2,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
	
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth()*2,ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+(int)(1.2*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()*2-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()+bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+(int)(1.2*bmpLongBoxVHigh.getWidth())+bmpLongWoodHigh.getWidth(),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth()*2,ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongWoodHigh.getHeight()*2,Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth()*2+(int)(1.2*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.pig,0f),
								
				new Map(con.getWidth()*2/3+bmpLongWoodHigh.getWidth()*2+2,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),	
			},
			//5
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				
				new Map(con.getWidth()*2/3-bmpLongBoxHHigh.getWidth()-2,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3-bmpLongBoxVHigh.getWidth()-2,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight()-1,Type.longBox,0f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongBoxHHigh.getHeight()-2,Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()*2+(int)(1.2*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()*2+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight()-1,Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()*2+bmpLongWoodHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()*2-bmpLongBoxHHigh.getHeight()-2,Type.longBox,90f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongBoxVHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()*1-bmpLongBoxHHigh.getHeight()-bmpLongWoodHigh.getHeight()-2,Type.longWood,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight()*1-bmpLongBoxHHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpPig.getHeight()-3,Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()*3+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()*3+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight(),Type.longBox,90f),
			},
			//6
			{
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth(),AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2,AngryBirdActivity.startY,Type.redBird,0f),
				new Map(AngryBirdActivity.startX-bmpRedBird.getWidth()*2*2,AngryBirdActivity.startY,Type.redBird,0f),
				
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxHHigh.getHeight()*1-bmpLongBoxVHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxHHigh.getHeight()*1-bmpLongBoxVHigh.getHeight()*2,Type.longBox,90f),

				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxHHigh.getHeight()*1-bmpLongBoxVHigh.getHeight()*2,Type.longBox,90f),
				new Map(con.getWidth()*2/3,ground.getY()-bmpLongBoxHHigh.getHeight()*2-bmpLongBoxVHigh.getHeight()*2,Type.longBox,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxHHigh.getHeight()*2-bmpLongBoxVHigh.getHeight(),Type.longBox,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxVHigh.getWidth()+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxHHigh.getHeight()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()+2,ground.getY()-bmpLongBoxHHigh.getHeight()*2-bmpLongBoxVHigh.getHeight(),Type.longBox,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()-bmpLongBoxVHigh.getWidth(),ground.getY()-bmpLongBoxVHigh.getHeight(),Type.longBox,90f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight(),Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()/2-bmpLongBoxHHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongWoodHigh.getHeight()-bmpLongBoxHHigh.getHeight(),Type.longBox,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+(int)(1.1*bmpLongBoxVHigh.getWidth()),ground.getY()-bmpPig.getHeight(),Type.pig,0f),
				
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1-bmpLongWoodHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight()*2-bmpLongWoodHigh.getHeight()+1,Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1+bmpLongWoodHigh.getWidth()/2,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight()*2-bmpLongWoodHigh.getHeight()+1,Type.longWood,0f),
				new Map(con.getWidth()*2/3+bmpLongBoxHHigh.getWidth()+1+bmpLongBoxHHigh.getWidth()+1,ground.getY()-bmpLongBoxVHigh.getHeight()-bmpLongBoxHHigh.getHeight()*2-bmpLongWoodHigh.getHeight()*2+2,Type.longWood,0f),

			}
			
		};
			
		loadMap(currentLevel);

		if(birdList.size()>0)
		{
			birdList.get(0).getBird().setState(State.ON_SLINGSHOT);
			
			Vec2 position=new Vec2((int)AngryBirdActivity.startX,(int)(AngryBirdActivity.startY));
			
			birdList.get(0).getBody().setTransform(new Vec2(position.x/RATE,position.y/RATE), 0);
			
			birdList.get(0).getBody().setActive(false);
		}

		GAME_VIEW_LOGIC_THREAD_FLAG=true;
		GAME_VIEW_DRAW_THREAD_FLAG=true;
		
		if(logicThread==null)
		{
			logicThread=new GameViewLogicThread(this);
			logicThread.start();
		}
		
		if(drawThread==null)
		{
			drawThread=new GameViewDrawThread(this);
			drawThread.start();
		}

		if(currentSoundState==SoundState.SOUND_ON)
		{
			activity.soundPool.play(soundBirdSinging1,currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
			activity.soundPool.play(soundBirdSinging2,currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
		}

	}
	
	public void loadMap(int level)
	{
		level=level-1;
		
		if(level>=0&&level<map.length)
		{

			for(int i=0;i<map[level].length;i++)
			{
				if(map[level][i].getType()==Type.redBird)
				{
					birdList.add(new Polygon(world,map[level][i].getX(),map[level][i].getY(),
							1f,1f,0.3f,new UserData(bmpRedBird,map[level][i].getType()),
							new Bird(
									(int)AngryBirdActivity.startX,
									(int)AngryBirdActivity.startY,
									(int)(bmpRedBird.getHeight()/2f),
									bmpRedBird,
									map[level][i].getType()))
					);
				}
				else if(map[level][i].getType()==Type.yellowBird)
				{
					birdList.add(new Polygon(world,map[level][i].getX(),map[level][i].getY(),
							1f,1f,0.3f,new UserData(bmpYellowBird,map[level][i].getType()),
							new Bird(
									(int)AngryBirdActivity.startX,
									(int)AngryBirdActivity.startY,
									(int)(bmpYellowBird.getHeight()/2f),
									bmpYellowBird,
									map[level][i].getType()))
					);
				}
				else if(map[level][i].getType()==Type.longBox)
				{
					if(map[level][i].getAngle()==0)
					{
						physicsList.add(new Rectangle(world,map[level][i].getX(),
								map[level][i].getY(),
								bmpLongBoxHHigh.getWidth()>>1,
								bmpLongBoxHHigh.getHeight()>>1,
								map[level][i].angle,0.5f, 1f, 0.0f,
								new UserData(bmpLongBoxHHigh,bmpLongBoxHMid,bmpLongBoxHLow,180f,Type.longBox)
								));
					}
					else //if(map[level][i].getAngle()==90f)
					{
						physicsList.add(new Rectangle(world,map[level][i].getX(),
								map[level][i].getY(),
								bmpLongBoxVHigh.getWidth()>>1,
								bmpLongBoxVHigh.getHeight()>>1,
								0f,0.5f, 1f, 0.0f,
								new UserData(bmpLongBoxVHigh,bmpLongBoxVMid,bmpLongBoxVLow,180f,Type.longBox)
								));
					}
					
				}
				else if(map[level][i].getType()==Type.longWood)
				{
					physicsList.add(new Rectangle(world,map[level][i].getX(),
							map[level][i].getY(),
							bmpLongWoodHigh.getWidth()>>1,
							bmpLongWoodHigh.getHeight()>>1,
							map[level][i].angle,0.5f, 1f, 0.0f,
							new UserData(bmpLongWoodHigh,bmpLongWoodMid,bmpLongWoodLow,180f,Type.longWood)
							));
				}
				else if(map[level][i].getType()==Type.pig)
				{
					leftPigNumber++;
					physicsList.add(new Polygon(world,map[level][i].getX(),
							map[level][i].getY(),
							0.5f,1f,0.3f,new UserData(bmpPig,bmpPig,bmpPig,10f,Type.pig))
					);
				}
			}
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		GAME_VIEW_LOGIC_THREAD_FLAG=false;
		GAME_VIEW_DRAW_THREAD_FLAG=false;
		
		logicThread=null;
		drawThread=null;
		
	}

	public void screenToWorld(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE)
		{
			float []trans = {0,0,0,1,1,1,2,2,2};
			Matrix matrix=new Matrix();
			matrix.setTranslate(-offsetX,-offsetY);
			matrix.postScale(zoom, zoom,screenW/2,screenH/2);
			matrix.getValues(trans);
					
			float [][]mm={
					{trans[0],trans[1],trans[2]},
					{trans[3],trans[4],trans[5]},
					{trans[6],trans[7],trans[8]},
			};
			
			float [][]yy=MyMatrix.getN(mm);
			
			newTouchPoint.x=yy[0][0]*touchPoint.x+yy[0][1]*touchPoint.y+yy[0][2];
			newTouchPoint.y=yy[1][0]*touchPoint.x+yy[1][1]*touchPoint.y+yy[1][2];
		}
	}
	
	
	public void worldToScreen(Polygon polygon)
	{

			float []trans = {0,0,0,1,1,1,2,2,2};
			Matrix matrix=new Matrix();
			matrix.setTranslate(-offsetX,-offsetY);
			matrix.postScale(zoom, zoom,screenW/2,screenH/2);
			matrix.getValues(trans);
					
			float [][]mm={
					{trans[0],trans[1],trans[2]},
					{trans[3],trans[4],trans[5]},
					{trans[6],trans[7],trans[8]},
			};
			
			polygon.setScreenX((int)(trans[0]*polygon.getX()+trans[1]*polygon.getY()+trans[2]));
			polygon.setScreenY((int)(trans[3]*polygon.getX()+trans[4]*polygon.getY()+trans[5]));


	}	

	public Vec2 worldToScreen(Vec2 vector)
	{
			Vec2 res=new Vec2();
			float []trans = {0,0,0,1,1,1,2,2,2};
			Matrix matrix=new Matrix();
			matrix.setTranslate(-offsetX,-offsetY);
			matrix.postScale(zoom, zoom,screenW/2,screenH/2);
			matrix.getValues(trans);
					
			float [][]mm={
					{trans[0],trans[1],trans[2]},
					{trans[3],trans[4],trans[5]},
					{trans[6],trans[7],trans[8]},
			};

			res.set((int)(trans[0]*vector.x+trans[1]*vector.y+trans[2]),(int)(trans[3]*vector.x+trans[4]*vector.y+trans[5]));
			return res;
	}	
	
	
	public boolean onTouchEvent(MotionEvent event)
	{	
		touchPoint.set((float)event.getX(),(float)event.getY());

		screenToWorld(event);

		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{	
			if(currentGameState==GameState.GAME_GOING)
			{
				
				oldIncrement=currentIncrement;
				currentIncrement=0;
				
				for(int i=0;i<birdList.size();i++)
				{
					if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
					{
						if(birdList.get(i).getBird().isPressed(newTouchPoint)&&!birdList.get(i).getBird().getIsReleased())
						{
							birdList.get(i).getBird().setIsPressed(true);
							if(currentSoundState==SoundState.SOUND_ON)
							{
								activity.soundPool.play(soundSlingshotStreched, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
							}
							Log.d("myLog","birdList.get(i).getBird().setIsPressed(true) i:"+i+" State:"+birdList.get(i).getBird().getState());
						}
					}
					
				}
				
				if(btnPlus.isPressed(event))
				{
					if(zoom<1f)
					{
					//		zoom+=0.01;
					}
				}
				else if(btnMinus.isPressed(event))
				{
						if((int)(sky.getWidth()*zoom)>screenW
								&&(int)(sky.getHeight()*zoom)>screenH
								)
						{
						//	zoom-=0.01;
						}
				}
				else
				{
					pointX=(int)(event.getX());
					pointY=(int)(event.getY());
	
					startX=-offsetX;
					startY=-offsetY;
	
					
				}
				

				if(btnPause.isPressed(event))
				{
					currentGameState=GameState.GAME_PAUSE;
				}
			}
			else if(currentGameState==GameState.GAME_PAUSE)
			{

				if(HintState==true)
				{
					HintState=false;
				}
				
				if(btnBackToGame.isPressed(event))
				{
					currentGameState=GameState.GAME_GOING;
				}
				else if(btnMenu.isPressed(event))
				{
					activity.handler.sendEmptyMessage(1);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false; 

				}
				else if(btnRestart.isPressed(event))
				{
					activity.handler.sendEmptyMessage(2);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false;

				}
				else if(btnHowToPlay.isPressed(event))
				{
					HintState=true;
				}
				else if(btnSound.isPressed(event))
				{
					
					if(currentSoundState==SoundState.SOUND_OFF)
					{
						currentSoundState=SoundState.SOUND_ON;
						sp.edit().putInt("SAVE_SOUND", soundOn).commit();
						btnSound.setBmp(bmpSoundOn);
					}
					else if(currentSoundState==SoundState.SOUND_ON)
					{
						currentSoundState=SoundState.SOUND_OFF;
						sp.edit().putInt("SAVE_SOUND", soundOff).commit();
						btnSound.setBmp(bmpSoundOff);
					}
				}
			}
			else if(currentGameState==GameState.GAME_WIN)
			{

				if(btnMenu.isPressed(event))
				{
					activity.handler.sendEmptyMessage(1);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false; 

				}
				else if(btnRestart.isPressed(event))
				{
					activity.handler.sendEmptyMessage(2);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false;

				}
				else if(btnNext.isPressed(event))
				{
					if(currentLevel!=6)
					{
						currentGameState=GameState.GAME_GOING;
						activity.handler.sendEmptyMessage(2);
						GAME_VIEW_LOGIC_THREAD_FLAG=false;
						GAME_VIEW_DRAW_THREAD_FLAG=false;
						currentLevel++;
					}
				}
			}
			else if(currentGameState==GameState.GAME_LOSE)
			{
				if(btnMenu.isPressed(event))
				{
					activity.handler.sendEmptyMessage(1);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false; 

				}
				else if(btnRestart.isPressed(event))
				{
					activity.handler.sendEmptyMessage(2);
					GAME_VIEW_LOGIC_THREAD_FLAG=false;
					GAME_VIEW_DRAW_THREAD_FLAG=false;

				}
			}
		}
		else if(event.getAction()==MotionEvent.ACTION_MOVE)
		{
			if(currentGameState==GameState.GAME_GOING)
			{
				boolean isBirdMove=false;
				
				for(int i=0;i<birdList.size();i++)
				{
					if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
					{
						if(birdList.get(i).getBird().getIsPressed()&&birdList.get(i).getBird().getIsReleased()==false)
						{
							birdList.get(i).getBird().move(newTouchPoint);
					
							birdList.get(i).getBody().setTransform(new Vec2((birdList.get(i).getBird().getX()-birdList.get(i).getBird().getR())/RATE,
									(birdList.get(i).getBird().getY()-birdList.get(i).getBird().getR())/RATE), 0);
							
							isBirdMove=true;
						}
					}	
				}
				
				if(isBirdMove==false)
				{
					int moveX=(int)(event.getX())-pointX;
					int moveY=(int)(event.getY())-pointY;

					moveY=0;
	
					if( (int)(screenW/2*(1-zoom)-(-(startX+moveX))*zoom)<0 && (int)(screenW/2*(1-zoom)-(-(startX+moveX))*zoom)+con.getWidth()*zoom>screenW)
					{	// (con.getWidth()-offsetX-sW/2)*zoom>sw/2
							offsetX=-(startX+moveX);
					}
					
					if((int)(screenH/2*(1-zoom)-(-(startY+moveY))*zoom)<0 && (int)(screenH/2*(1-zoom)-(-(startY+moveY))*zoom)+con.getHeight()>screenH)
					{
							offsetY=-(startY+moveY);
					}
				}
			}//game_going
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			
			currentIncrement=oldIncrement;

			for(int i=0;i<birdList.size();i++)
			{
				if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
				{
					if(birdList.get(i).getBird().getIsPressed())
					{
						birdList.get(i).getBird().setIsReleased(true);
					}
				}
			}
			
			isFirstMultiTouch=true;
			oldRate=zoom;

		}

		if(event.getPointerCount()==2)
		{
			if(isFirstMultiTouch)
			{
					oldDistance=(float) (Math.pow(event.getX(0)-event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
					isFirstMultiTouch=false;
			}
			else
			{
				newDistance=(float) (Math.pow(event.getX(0)-event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
				
				float preZoom=oldRate*newDistance/oldDistance;
				
				if((int)(sky.getWidth()*preZoom)>screenW
						&&(int)(sky.getHeight()*preZoom)>screenH
						&&preZoom<1f
						)
				{
					zoom=preZoom;
				}		
			}
		}
	
		if((int)(screenW/2*(1-zoom)-offsetX*zoom)>0-1)
		{
			offsetX=(int) (screenW/2*(1-zoom)/zoom);
		}
		else if((int)(screenW/2*(1-zoom)-offsetX*zoom+sky.getWidth()*zoom)<screenW+1)
		{
			offsetX=(int)((screenW/2*(1-zoom)+sky.getWidth()*zoom-screenW)/zoom);
		}
		
		offsetY=(int) (con.getHeight()*3/4-screenH/2-screenH/4/zoom);
	
		synchronized(lock)
		{
			try
			{
				lock.wait(timePause);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return true;
	}

	
	public void logic()
	{
		if(currentGameState==GameState.GAME_GOING)
		{
			world.step(timeStep, velocityIterations,positionIterations);
		
			for(int i=0;i<scoreList.size();i++)
			{
				if(scoreList.get(i).isHasBeenDrawed())
				{
					scoreList.remove(i);
				}
			}
			
			pigSingingCount++;
			if(currentSoundState==SoundState.SOUND_ON&&runInBack==false)
			{
				if(leftPigNumber>0)
				{
					if(pigSingingCount==1000/GAME_VIEW_LOGIC_THREAD_SLEEPTIME)
					{
						activity.soundPool.play(soundPigSinging1, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
						
					}
					else if(pigSingingCount==2000/GAME_VIEW_LOGIC_THREAD_SLEEPTIME)
					{
						activity.soundPool.play(soundPigSinging2, currentVolume/maxVolume, currentVolume/maxVolume,1,0,1);
						
					}
					else if(pigSingingCount==3000/GAME_VIEW_LOGIC_THREAD_SLEEPTIME)
					{
						activity.soundPool.play(soundPigSinging3, currentVolume/maxVolume, currentVolume/maxVolume,2,0,1);
					}
					else if(pigSingingCount>5000/GAME_VIEW_LOGIC_THREAD_SLEEPTIME)
					{
						pigSingingCount=0;
					}
				}
				
				
			}
			
			for(int j=0;j<physicsList.size();j++)
			{
			
				if(physicsList.get(j).getBody().m_userData instanceof UserData)
				{
					UserData userData=(UserData)(physicsList.get(j).getBody().m_userData);
					
			//		////Log.d("myLog","Life:["+j+"]:"+userData.getCurrentLife());
					
					if(userData.getType()==Type.nothing)
					{
						
						if(userData.getBmp()==bmpPig)
						{
							leftPigNumber--;
							scoreList.add(new Score(physicsList.get(j).getX(),physicsList.get(j).getY(),bmp5000,5000f));
						}
						else
						{
							scoreList.add(new Score(physicsList.get(j).getX(),physicsList.get(j).getY(),bmp500,500f));
						}
		
						////Log.d("myLog","world.destroyBodyAAA");
						
						if(currentSoundState==SoundState.SOUND_ON)
						{
							if(userData.getBmp()==bmpPig)
							{
								activity.soundPool.play(soundPigDestory, currentVolume/maxVolume, currentVolume/maxVolume, 3,0,1);
							}
							else 
							{
								activity.soundPool.play(soundWoodDestory, currentVolume/maxVolume, currentVolume/maxVolume, 2,0,1);
							}
						}
						
						
						world.destroyBody(physicsList.get(j).getBody());
						physicsList.remove(j);
		
					}	
					else
					{
						if(physicsList.get(j).getX()<ground.getX()||physicsList.get(j).getX()>ground.getX()+ground.getWidth())
						{
							//////Log.d("myLog","world.destroyBodyCCC");
							UserData userData1=(UserData)(physicsList.get(j).getBody().m_userData);
							userData1.setType(Type.nothing);
							
						//	world.destroyBody(physicsList.get(j).getBody());
						//	physicsList.remove(j);
						}
					}
					
				}
			}
		
			
			for(int i=0;i<birdList.size();i++)
			{

	
				if(birdList.get(i).getBird().getState()==State.JUMP_TO_SLINGSHOT)
				{
	
					
					
					if(birdList.get(i).getBird().isInitAnimation()==false)
					{
						deltaXa=(birdList.get(i).getBird().getX()-birdList.get(i).getBird().getR()-birdList.get(i).getX())/50f;
						deltaYa=(birdList.get(i).getY()-(birdList.get(i).getBird().getY()-birdList.get(i).getBird().getR()))/50f;
						angle=360f/(1000f/GAME_VIEW_LOGIC_THREAD_SLEEPTIME);
						birdList.get(i).getBird().setInitAnimation(true);
						birdList.get(i).getBody().setActive(false);
					}
					
					if(birdList.get(i).getX()<birdList.get(i).getBird().getX()-birdList.get(i).getBird().getR()||
							birdList.get(i).getY()>birdList.get(i).getBird().getY()-birdList.get(i).getBird().getR())
					{
					
						if(birdList.get(i).getY()>birdList.get(i).getBird().getY()-birdList.get(i).getBird().getR())
						{
							
							birdList.get(i).getBody().setTransform(new Vec2(birdList.get(i).getX()/RATE,
									(birdList.get(i).getY()-deltaYa)/RATE),0);
						}
						
						
						if(birdList.get(i).getX()<birdList.get(i).getBird().getX()-birdList.get(i).getBird().getR())
						{
							birdList.get(i).getBird().setAnimationAngle(birdList.get(i).getBird().getAnimationAngle()+angle);
					//		//////Log.d("myLog","birdList.get(i).getBird().getAnimationAngle():"+birdList.get(i).getBird().getAnimationAngle());
							
							birdList.get(i).getBody().setTransform(new Vec2( (birdList.get(i).getX()+deltaXa)/RATE,
									birdList.get(i).getY()/RATE ),(float) (birdList.get(i).getBird().getAnimationAngle()/180*Math.PI));
						}
	
					}
					else
					{
						birdList.get(i).getBird().setState(State.ON_SLINGSHOT);
					}
					
				}
				else if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
				{
	
					if(!birdList.get(i).getBird().getIsPressed())
					{
						birdList.get(i).setX(birdList.get(i).getBird().getX()-birdList.get(i).getBird().getR());
						birdList.get(i).setY(birdList.get(i).getBird().getY()-birdList.get(i).getBird().getR());
					}
					
					if(birdList.get(i).getBird().getIsReleased())
					{
	
						birdList.get(i).getBody().setActive(true);
	
						birdList.get(i).getBody().setBullet(true);
						
						float birdToSlingshotAngle=(float) Math.atan2(birdList.get(i).getBird().getY()-AngryBirdActivity.startY,
								birdList.get(i).getBird().getX()-AngryBirdActivity.startX);
						
						
						float forceX=-(float) (Math.sqrt(Math.pow(birdList.get(i).getBird().getX()-AngryBirdActivity.startX, 2))*Math.cos(birdToSlingshotAngle));
						float forceY=-(float) (Math.sqrt(Math.pow(birdList.get(i).getBird().getY()-AngryBirdActivity.startY, 2))*Math.sin(birdToSlingshotAngle));
					
						Vec2 force=new Vec2(forceX*forceRate,forceY*forceRate);
	
						birdList.get(i).getBody().applyForce(force, birdList.get(i).getBody().getWorldCenter());
						
						moveToLeft=false;
						
						currentIncrement=13;
						
				//		startFlyTime=System.currentTimeMillis();	
						
						birdList.get(i).getBird().setState(State.LAUNCHED);
						
						if(currentSoundState==SoundState.SOUND_ON)
						{
							activity.soundPool.play(soundBirdFlying, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
						}
						
					}
				}
				else if(birdList.get(i).getBird().getState()==State.LAUNCHED)
				{
						worldToScreen(birdList.get(i));
						
						
					//	Vec2 vertices[]=birdList.get(0).getPolygonShape().getVertices();
					//	List<Vec2> list=birdList.get(0).getPolygonShape().getVertices();
						
					//	//////Log.d("myLog","vertices[0]:("+birdList.get(0).getPolygonShape().getVertex(0).x+","+birdList.get(0).getPolygonShape().getVertex(0).y+")");
						
						if(birdList.get(i).getScreenY()<screenH/5)
						{
							//////Log.d("myLog","birdList.get(i).getScreenY()<10 zoom:"+zoom);
							if(!oldZoonInit)
							{
								oldZoom=zoom;
								oldZoonInit=true;
							}
							
							float preZoom=zoom-0.005f;
							if((int)(sky.getWidth()*preZoom)>screenW
									&&(int)(sky.getHeight()*preZoom)>screenH
									)
							{
								//////Log.d("myLog","zoom-=0.1:"+zoom);
								zoom=preZoom;
							}
						}
						else
						{
							if(oldZoonInit)
							{
								if(zoom<oldZoom)
								{
									zoom+=0.005;
									
								}
								else
								{
									oldZoonInit=false;
								}
							}
	
						}
	
						if(birdList.get(i).getBird().isFirstContact()==true&&birdList.get(i).getBird().isContacted()==false)
						{
							if(i+1<birdList.size())
							{
								birdList.get(i+1).getBird().setState(State.JUMP_TO_SLINGSHOT);
								birdList.get(i).getBird().setContacted(true);
							}
						}
						
						if(birdList.get(i).getX()<ground.getX()||birdList.get(i).getX()>ground.getX()+ground.getWidth())
						{
							birdList.get(i).getBody().setAwake(false);
						}
	
						if(birdList.get(i).getBody().isAwake()==false)
						{
							if(birdList.get(i).getBird().isContacted()==false)
							{
								if(i+1<birdList.size())
								{
									birdList.get(i+1).getBird().setState(State.JUMP_TO_SLINGSHOT);
								}
							}
							world.destroyBody(birdList.get(i).getBody());
							birdList.remove(i);										
						}
				//	}
				}//else if(birdList.get(i).getBird().getState()==State.LAUNCHED)
		
			}//for
			
			
			boolean everyThingStop=true;
	
			for(int i=0;i<physicsList.size();i++)
			{
				if(physicsList.get(i).getBody().isAwake()==true)
				{
					everyThingStop=false;
					//////Log.d("myLog","everyThingStop=false 1");
				}
			}
			
			for(int i=0;i<birdList.size();i++)
			{
				if(birdList.get(i).getBody().isAwake()==true&&birdList.get(i).getBird().getState()==State.LAUNCHED)
				{
					everyThingStop=false;
					//////Log.d("myLog","everyThingStop=false 2");
				}
			}
			
			if(everyThingStop==true)
			{
				if(leftPigNumber==0)
				{
					currentGameState=GameState.GAME_WIN;
					if(currentSoundState==SoundState.SOUND_ON)
					{
						activity.soundPool.play(soundLevelComplete, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
					}
				}
				else
				{	
					if(birdList.size()==0)
					{
						currentGameState=GameState.GAME_LOSE;
					}
				}
			}

			if(moveToLeft)
			{
				if( (int)(screenW/2*(1-zoom)-(offsetX-currentIncrement)*zoom)<0 &&
						(int)(screenW/2*(1-zoom)-(offsetX-currentIncrement)*zoom)+con.getWidth()*zoom>screenW)
				{
						offsetX-=currentIncrement;
				}
			}
			else if(!moveToLeft)
			{
				if( (int)(screenW/2*(1-zoom)-(offsetX+currentIncrement)*zoom)<0 &&
						(int)(screenW/2*(1-zoom)-(offsetX+currentIncrement)*zoom)+con.getWidth()*zoom>screenW)
				{

						offsetX+=currentIncrement;
				}
			}
		}//game_going

		if((int)(screenW/2*(1-zoom)-offsetX*zoom)>0-1)
		{
			offsetX=(int) (screenW/2*(1-zoom)/zoom);
		}
		else if((int)(screenW/2*(1-zoom)-offsetX*zoom+ground.getWidth()*zoom)<screenW+1)
		{
			offsetX=(int)((screenW/2*(1-zoom)+ground.getWidth()*zoom-screenW)/zoom);
		}
			
		offsetY=(int) (con.getHeight()*3/4-screenH/2-screenH/4/zoom);
		
	}
	

	public void draw()
	{
		try
		{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				canvas.save();
				canvas.clipRect(new RectF(0,0,screenW,screenH));

				con.draw(canvas, paint, -offsetX/3f,-offsetY/3f, screenW/2,screenH/2,(zoom-1)/3f+1);

				grass.draw(canvas, paint, -offsetX+grass.getX(),-offsetY+grass.getY(), screenW/2,screenH/2, zoom);

			//	canvas.drawRect(rrground.getX(),rrground.getY()-initOffsetY,rrground.getX()+rrground.getHalfW()*2,rrground.getY()-initOffsetY+rrground.getHalfH()*2, paint);
				
				
				Vec2 Vector=new Vec2();
				Vector=worldToScreen(new Vec2(slingshot.getX(),slingshot.getY()));
				for(int i=0;i<birdList.size();i++)
				{
					worldToScreen(birdList.get(i));

					if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
					{
						Paint paint=new Paint();
						paint.setColor(Color.BLACK);
						paint.setStrokeWidth(zoom*5);
						paint.setStyle(Style.STROKE);
						canvas.drawLine(birdList.get(i).getScreenX(),birdList.get(i).getScreenY()+bmpRedBird.getHeight()/2*zoom,Vector.x+39*zoom,Vector.y+30*zoom, paint);
					}
				}
				
				slingshot.draw(canvas, paint, -offsetX+slingshot.getX(),-offsetY+slingshot.getY(), screenW/2,screenH/2, zoom);			

				for(int i=0;i<birdList.size();i++)
				{
					birdList.get(i).draw(canvas, paint, -offsetX+birdList.get(i).getX(),-offsetY+birdList.get(i).getY(),screenW/2,screenH/2, zoom);	
					if(birdList.get(i).getBird().getState()==State.ON_SLINGSHOT)
					{
						Paint paint=new Paint();
						paint.setColor(Color.BLACK);
						paint.setStrokeWidth(zoom*5);
						paint.setStyle(Style.STROKE);
						canvas.drawLine(birdList.get(i).getScreenX(),birdList.get(i).getScreenY()+bmpRedBird.getHeight()/2*zoom, Vector.x+4*zoom,Vector.y+30*zoom, paint);
						
					}
				}
				
				
				for(int j=0;j<physicsList.size();j++)
				{
					physicsList.get(j).draw(canvas, paint,-offsetX+physicsList.get(j).getX(),-offsetY+physicsList.get(j).getY(),screenW/2,screenH/2, zoom);
			
				}
				
				for(int i=0;i<scoreList.size();i++)
				{
					if(currentGameState==GameState.GAME_GOING)
					scoreList.get(i).draw(canvas, paint,-offsetX+scoreList.get(i).getX(),-offsetY+scoreList.get(i).getY(),screenW/2,screenH/2, zoom);
				}
				
				
				rrground.draw(canvas, paint,-offsetX+rrground.getX(),-offsetY+rrground.getY(),screenW/2,screenH/2, zoom);
				
				canvas.restore();

			//	canvas.drawCircle(touchPoint.x,touchPoint.y,20, paint);
				
			//	canvas.drawCircle(newTouchPoint.x,newTouchPoint.y, 10, paint);
	
				//按钮在画布最上层。。。。不受缩放影响
			//	btnPlus.draw(canvas, paint);
			//	btnMinus.draw(canvas, paint);
				
				btnPause.draw(canvas, paint);
				
			//	paint.setTextSize(20);
			//	canvas.drawText("Fps:"+fps+" Zoom:"+zoom, 0, 50, paint);

				
				if(currentGameState==GameState.GAME_PAUSE
				||currentGameState==GameState.GAME_WIN
				||currentGameState==GameState.GAME_LOSE)
				{
					Paint paintA=new Paint();
					paintA.setAlpha(0x77);
					canvas.drawRect(0, 0, screenW, screenH, paintA);
				}
				
				
				if(currentGameState==GameState.GAME_PAUSE)
				{
					canvas.drawBitmap(bmpMainMenu,screenW/2-bmpMainMenu.getWidth()/2,screenH/2-bmpMainMenu.getHeight()/2, paint);
					canvas.drawBitmap(bmpImgOne,screenW/2-bmpImgOne.getWidth(),screenH/2-bmpImgOne.getHeight()/2, paint);
					
					switch(currentLevel)
					{
					case 1:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
						break;
					case 2:
						canvas.drawBitmap(bmpNum2,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum2.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum2.getHeight()), paint);
						break;
					case 3:
						canvas.drawBitmap(bmpNum3,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum3.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum3.getHeight()), paint);
						break;
					case 4:
						canvas.drawBitmap(bmpNum4,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum4.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum4.getHeight()), paint);
						break;
					case 5:
						canvas.drawBitmap(bmpNum5,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum5.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum5.getHeight()), paint);
						break;
					case 6:
						canvas.drawBitmap(bmpNum6,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum6.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum6.getHeight()), paint);
						break;
					default:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
					}
		
					btnMenu.draw(canvas, paint);
					btnRestart.draw(canvas, paint);
					btnBackToGame.draw(canvas, paint);

					btnSound.draw(canvas, paint);
					btnHowToPlay.draw(canvas, paint);
					
					if(HintState==true)
					{
						canvas.drawBitmap(bmpGuide,screenW/2-bmpGuide.getWidth()/2,screenH/2-bmpGuide.getHeight()/2, paint);
					}
					
				}
				else if(currentGameState==GameState.GAME_WIN)
				{
					canvas.drawBitmap(bmpMainMenu,screenW/2-bmpMainMenu.getWidth()/2,screenH/2-bmpMainMenu.getHeight()/2, paint);
					
					switch(currentLevel)
					{
					case 1:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
						break;
					case 2:
						canvas.drawBitmap(bmpNum2,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum2.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum2.getHeight()), paint);
						break;
					case 3:
						canvas.drawBitmap(bmpNum3,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum3.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum3.getHeight()), paint);
						break;
					case 4:
						canvas.drawBitmap(bmpNum4,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum4.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum4.getHeight()), paint);
						break;
					case 5:
						canvas.drawBitmap(bmpNum5,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum5.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum5.getHeight()), paint);
						break;
					case 6:
						canvas.drawBitmap(bmpNum6,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum6.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum6.getHeight()), paint);
						break;
					default:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
					}
					
					switch(currentLevel)
					{
					case 1:
						canvas.drawBitmap(bmpWinOne,screenW/2-bmpWinOne.getWidth()/2,screenH/2-bmpWinOne.getHeight()/2, paint);
						break;
					case 2:
						canvas.drawBitmap(bmpWinTwo,screenW/2-bmpWinTwo.getWidth()/2,screenH/2-bmpWinTwo.getHeight()/2, paint);
						break;
					case 3:
						canvas.drawBitmap(bmpWinThree,screenW/2-bmpWinThree.getWidth()/2,screenH/2-bmpWinThree.getHeight()/2, paint);
						break;
					case 4:
						canvas.drawBitmap(bmpWinFour,screenW/2-bmpWinFour.getWidth()/2,screenH/2-bmpWinFour.getHeight()/2, paint);
						break;
					case 5:
						canvas.drawBitmap(bmpWinFive,screenW/2-bmpWinFive.getWidth()/2,screenH/2-bmpWinFive.getHeight()/2, paint);
						break;
					case 6:
						canvas.drawBitmap(bmpWinSix,screenW/2-bmpWinSix.getWidth()/2,screenH/2-bmpWinSix.getHeight()/2, paint);
						break;
					default:
						canvas.drawBitmap(bmpWinOne,screenW/2-bmpWinOne.getWidth()/2,screenH/2-bmpWinOne.getHeight()/2, paint);
						
					}
					
					btnMenu.draw(canvas, paint);
					btnRestart.draw(canvas, paint);
					
					if(currentLevel!=6)
					{
						btnNext.draw(canvas, paint);
					}
					
			
				}
				else if(currentGameState==GameState.GAME_LOSE)
				{
					canvas.drawBitmap(bmpMainMenu,screenW/2-bmpMainMenu.getWidth()/2,screenH/2-bmpMainMenu.getHeight()/2, paint);
					canvas.drawBitmap(bmpImgThree,screenW/2-bmpImgThree.getWidth(),screenH/2-bmpImgThree.getHeight()/2, paint);
					
					switch(currentLevel)
					{
					case 1:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
						break;
					case 2:
						canvas.drawBitmap(bmpNum2,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum2.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum2.getHeight()), paint);
						break;
					case 3:
						canvas.drawBitmap(bmpNum3,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum3.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum3.getHeight()), paint);
						break;
					case 4:
						canvas.drawBitmap(bmpNum4,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum4.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum4.getHeight()), paint);
						break;
					case 5:
						canvas.drawBitmap(bmpNum5,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum5.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum5.getHeight()), paint);
						break;
					case 6:
						canvas.drawBitmap(bmpNum6,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum6.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum6.getHeight()), paint);
						break;
					default:
						canvas.drawBitmap(bmpNum1,screenW/2+bmpMainMenu.getWidth()/2-(int)(1.5*bmpNum1.getWidth()),screenH/2-bmpMainMenu.getHeight()/2+(int)(0.5*bmpNum1.getHeight()), paint);
					}
					
					switch(currentLevel)
					{
					case 1:
					case 2:
						canvas.drawBitmap(bmpLoseOne,screenW/2,screenH/2-bmpLoseOne.getHeight()/2, paint);
						break;
					case 3:
					case 4:
						canvas.drawBitmap(bmpLoseTwo,screenW/2,screenH/2-bmpLoseTwo.getHeight()/2, paint);
						break;
					case 5:
					case 6:
						canvas.drawBitmap(bmpLoseThree,screenW/2,screenH/2-bmpLoseThree.getHeight()/2, paint);
						break;
					default:
						canvas.drawBitmap(bmpLoseOne,screenW/2,screenH/2-bmpLoseOne.getHeight()/2, paint);
						
					}
					
					btnMenu.draw(canvas, paint);
					btnRestart.draw(canvas, paint);
		
				}

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
			
			count++;
			if(count==20)
			{
				count=0;
				long tempTime=System.nanoTime();
				long span=tempTime-startTime;
				startTime=tempTime;
				fps=Math.round(100000000000.0/span*20)/100.0;
			}
			
		}
		
	}
	
	
	public void drawTrajectory(ArrayList list,Canvas canvas,Paint paint)
	{
		for(int i=0;i< list.size() ;i++)
		{

				int []m=(int[]) list.get(i);
				if(m[0]!=0||m[1]!=0)
				canvas.drawCircle(m[0],m[1], 5, paint);
			
			//////Log.d("myLog","=Coor["+i+"]:("+m[0]+","+m[1]+")");
		}
	}

	public void fakeRelease(float x,float y,float r,Canvas canvas,Paint paint){
	    CircleShape circle = new CircleShape();
	    circle.m_radius = r/RATE;

		FixtureDef fDef=new FixtureDef();
		fDef.density=1;
		fDef.friction=1.0f;
		fDef.restitution=0.3f;
		fDef.shape=circle;
	
		BodyDef bodyDef=new BodyDef();
		bodyDef.type=BodyType.DYNAMIC;
		
		bodyDef.position.set((x)/RATE, (y)/RATE);
		
		Body body=world.createBody(bodyDef);
//		body.m_userData=bird;
//		body.createFixture(fDef);  
//
//		float angle=(float) Math.atan2(bird.getY()-AngryBirdActivity.startY,bird.getX()-AngryBirdActivity.startX);
//		float forceX=-(float) (Math.sqrt(Math.pow(bird.getX()-AngryBirdActivity.startX, 2))*Math.cos(angle));
//		float forceY=-(float) (Math.sqrt(Math.pow(bird.getY()-AngryBirdActivity.startY, 2))*Math.sin(angle));
//		Vec2 force=new Vec2(forceX*forceRate,forceY*forceRate);
		
		
//		body.setLinearVelocity(force);
		for (int i=0; i<=10; i++) {
			world.step(1f/60f,10,8);
			canvas.drawCircle(body.getPosition().x*RATE,body.getPosition().y*RATE,5,paint);
			world.clearForces();
		}
		
		world.destroyBody(body);

	}
	
	@Override
	public void beginContact(Contact arg0) {
		// TODO Auto-generated method stub
		UserData userDataA=(UserData)(arg0.getFixtureA().getBody().getUserData());
		UserData userDataB=(UserData)(arg0.getFixtureB().getBody().getUserData());
		
		for(int i=0;i<birdList.size();i++)
		{
			if(birdList.get(i).getBird().getState()==State.LAUNCHED)
			{
				if(userDataA.getType()==Type.redBird||userDataA.getType()==Type.yellowBird)
				{
					birdList.get(i).getBird().setFirstContact(true);
				}
				else if(userDataB.getType()==Type.redBird||userDataB.getType()==Type.yellowBird)
				{
					birdList.get(i).getBird().setFirstContact(true);
				}
			}
		}
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		//Log.d("myLog","endContact");
	}


	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		int count = arg0.getManifold().pointCount;

		if(arg1.normalImpulses[0]+arg1.normalImpulses[1]>1.5)
		{
			if ( (arg0.getFixtureA().getBody().getUserData())instanceof UserData)
			{

				UserData rect=(UserData)(arg0.getFixtureA().getBody().getUserData());

				if(rect.getType()==Type.stone
				||rect.getType()==Type.wood
				||rect.getType()==Type.pig
				||rect.getType()==Type.longWood
				||rect.getType()==Type.longBox
				||rect.getType()==Type.glass)
				{
				
					rect.setCurrentLife(rect.getCurrentLife()-arg1.normalImpulses[0]-arg1.normalImpulses[1]);
					
					arg0.getFixtureA().getBody().m_userData=rect;
					if(rect.getCurrentLife()<0)
					{
						rect.setType(Type.nothing);
					}
					
					if(rect.getType()==Type.longWood||rect.getType()==Type.longBox)
					{
						if(arg1.normalImpulses[0]+arg1.normalImpulses[1]>5)
						{
							if(currentSoundState==SoundState.SOUND_ON)
							{
								activity.soundPool.play(soundWoodCollision, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
							}
						}
					}
				}
			}
			
			if ( (arg0.getFixtureB().getBody().getUserData())instanceof UserData)
			{

				UserData rect=(UserData)(arg0.getFixtureB().getBody().getUserData());

				if(rect.getType()==Type.stone
				||rect.getType()==Type.wood
				||rect.getType()==Type.pig
				||rect.getType()==Type.longWood
				||rect.getType()==Type.longBox
				||rect.getType()==Type.glass)
				{
	
					rect.setCurrentLife(rect.getCurrentLife()-arg1.normalImpulses[0]-arg1.normalImpulses[1]);
					
					arg0.getFixtureB().getBody().m_userData=rect;
					if(rect.getCurrentLife()<0)
					{
						rect.setType(Type.nothing);
					}
					
					if(rect.getType()==Type.longWood||rect.getType()==Type.longBox)
					{
						if(arg1.normalImpulses[0]+arg1.normalImpulses[1]>5)
						{
							if(currentSoundState==SoundState.SOUND_ON)
							{
								activity.soundPool.play(soundWoodCollision, currentVolume/maxVolume, currentVolume/maxVolume,0,0,1);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		//Log.d("myLog","preSolve");
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if(velocityX<0)
		{
			moveToLeft=false;
		}
		else if(velocityX>0)
		{
			moveToLeft=true;
		}
		
		currentIncrement=(int) Math.abs(velocityX/100f);
		
		if(velocityY<0)
		{
			moveToTop=false;
		}
		else if(velocityY>0)
		{
			moveToTop=true;
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		gesture.onTouchEvent(event);		
		return false;
	}

}
