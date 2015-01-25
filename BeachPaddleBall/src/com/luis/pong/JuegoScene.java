package com.luis.pong;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.align.HorizontalAlign;

import android.content.SharedPreferences;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.luis.pong.GestorEscenas.TipoEscena;

public class JuegoScene extends BaseScene implements IOnSceneTouchListener{
	private HUD gameHUD;
	private Text scoreText;
	private Text scoreText2;
	private int score = 0;
	private int score2 = 0;
	private int puntos = maxPuntos;
	private PhysicsWorld physicsWorld;
	public static int maxPuntos;
	//private Text gameOverText2;
	private Text ready;
	private Text level;
	//jugadores
	private Jugadores jugador1;
	private Jugadores jugador2;
	private Pelota bola;
	boolean resetearBola=true;
	boolean j1win=false;
	boolean j2win=false;
	boolean players2end=false;
	boolean tocado=false;
	public static boolean jugable;
	Sprite bolaSprite;
	Sprite loose;
	Sprite win;
	public static int numJugadores;
	//numero de jugadores

	@Override
	public void createScene() {
		this.clearEntityModifiers();
		System.gc();
		createBackground();
		createHUD();
	    createPhysics();
	    displayReadyText();
	    engine.setTouchController(new MultiTouchController());
	    
	    setOnSceneTouchListener(this);
	    
	}
	@Override
	public void onBackKeyPressed() {
		disposeScene();
		GestorEscenas.getInstance().loadMenuScene(engine,numJugadores);
	}
	@Override
	public void onHomeKeyPressed() {
		if(JuegoActivity.musica_menu.isPlaying()) {
				JuegoActivity.musica_menu.pause();
		}
		disposeScene();
		GestorEscenas.getInstance().loadMenuScene(engine,numJugadores);
		
	}
	@Override
	public TipoEscena getSceneType() {
		return TipoEscena.ESCENA_GAME;
	}
	@Override
	public void disposeScene() {
		
		camara.setHUD(null);
	    camara.setCenter(400, 240);
	}
	private void crearCuerpos(){
		
		loose=new Sprite(400, 240, recursos.loose, vbom);
		win=new Sprite(400, 240, recursos.win, vbom);
	    bola=new Pelota(400,240,vbom,camara,physicsWorld,recursos.bola);
	    jugador1=new Jugadores(80,240,vbom,camara,physicsWorld,recursos.pala1);
	    jugador2=new Jugadores(720,240,vbom,camara,physicsWorld,recursos.pala2);
	    PhysicsFactory.createLineBody(physicsWorld, 0, 0, 800, 0, PhysicsFactory.createFixtureDef(1, 1, 0) );
		PhysicsFactory.createLineBody(physicsWorld, 0, 480, 800, 480, PhysicsFactory.createFixtureDef(1, 1, 0));
		FixtureDef ladosFixture = PhysicsFactory.createFixtureDef(1, 1, 0);
		Body izquierda=PhysicsFactory.createLineBody(physicsWorld, 60, 0, 60, 480, ladosFixture);//60 es hasta donde llegan los botones
		izquierda.setUserData("izquierda");
		Body derecha=PhysicsFactory.createLineBody(physicsWorld, 740, 0, 740, 480, ladosFixture);
		derecha.setUserData("derecha");
	    this.attachChild(jugador1);
	    this.attachChild(jugador2);
	    this.attachChild(bola);
	}
	private void createHUD()
	{
	    gameHUD = new HUD();
	    if(numJugadores==1){
	    	scoreText = new Text(400, 400, recursos.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);	    
		    scoreText.setAnchorCenter(0, 0);    
		    scoreText.setText(String.valueOf(maxPuntos));
		    gameHUD.attachChild(scoreText);
	    }else if(numJugadores==2){
	    	scoreText = new Text(340, 400, recursos.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);	    
		    scoreText.setAnchorCenter(0, 0);    
		    scoreText.setText("0");
		    scoreText2 = new Text(440, 400, recursos.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);	    
		    scoreText2.setAnchorCenter(0, 0);    
		    scoreText2.setText("0");
		    gameHUD.attachChild(scoreText);
		    gameHUD.attachChild(scoreText2);
	    }
	    camara.setHUD(gameHUD);
	}
	private void addToScore(int i, int player)
	{
		if(numJugadores==2){
			if(player==1){
				score += i;
			    scoreText.setText(String.valueOf(score));
				if(score>=maxPuntos){
					bola.stop=true;
					j1win=true;
					if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	            		recursos.sonido_win.play();
	            		win.registerEntityModifier(new ScaleModifier(1, 0.1f, 1.3f));
						win.registerEntityModifier(new RotationModifier(0.6f, 0, 360));
	            		this.attachChild(win);
	            		
	            	
	            	}
				}
			}else if(player==2){
				score2+=i;
				scoreText2.setText(String.valueOf(score2));
				if(score2>=maxPuntos){
					bola.stop=true;
					j2win=true;
					if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	            		recursos.sonido_win.play();
	            		win.registerEntityModifier(new ScaleModifier(1, 0.1f, 1.3f));
						win.registerEntityModifier(new RotationModifier(0.6f, 0, 360));
	            		this.attachChild(win);//diferenciar que jugador gana
	            	}
				}
			}
		}else if(numJugadores==1){
			//sea cual sea gameOver si player1 o player 2 marcan
			if(player!=0){	
				loose.registerEntityModifier(new ScaleModifier(1, 0.1f, 1.3f));
				loose.registerEntityModifier(new RotationModifier(0.6f, 0, 360));
				this.attachChild(loose);
				players2end=true;
				bola.stop=true;

				jugable=false;
			}else{
				puntos -= i;
			    scoreText.setText(String.valueOf(puntos));
				if(puntos<=0){
					bola.stop=true;
					
						win.registerEntityModifier(new ScaleModifier(1, 0.1f, 1.3f));
						win.registerEntityModifier(new RotationModifier(0.6f, 0, 360));
						this.attachChild(win);
						if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
							recursos.sonido_win.play();
						}
	            		players2end=true;
	            		maxPuntos++;
	            		SharedPreferences.Editor editor = JuegoActivity.prefs.edit();
	            		editor.putString("nivel", String.valueOf(maxPuntos));
	    	    	   	editor.commit();
				}
			}
		}
	}
	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false); //fps, gravedad, si deja sleep
	    physicsWorld.setContactListener(createContactListener());
	    registerUpdateHandler(physicsWorld);
	}
	private ContactListener createContactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	        @Override
	        public void beginContact(Contact contact)
	        {
	        	final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            final boolean sonido = JuegoActivity.prefs.getString("sonido", "true").equals("true");

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("bola") && x1.getBody().getUserData().equals("izquierda")){
	                	bola.restart=true;
	                	bola.score2=true;
	                	if(sonido){
	                		recursos.sonido_gol.play();
	                	}
	                    addToScore(1,2);
	                }
	                
	                if(x2.getBody().getUserData().equals("bola") && x1.getBody().getUserData().equals("derecha")){	
	                	bola.restart=true;//asi vuelve a la velocidad inicial, aunq no cambie su posicion
	                	bola.score1=true;	                	
	                	if(sonido){
	                		recursos.sonido_gol.play();
	                	}
	                	addToScore(1,1);
	                }
	                if(x2.getBody().getUserData().equals("bola") && x1.getBody().getUserData().equals("pala")){
	                	//he tenido que añadir este sonido a un nuevo hilo ya que me creaba lag e influía en la física del juego
	                	if(sonido){
	                		new Thread(new Runnable() {

	                            @Override
	                            public void run() {
	                                if (recursos.sonido_toque != null) recursos.sonido_toque.play();
	                            }
	                        }).start();
	                	}
	                	if(numJugadores==1){
                			addToScore(1, 0);
                		}
	                }
	            }
	        }
	        @Override
	        public void endContact(Contact contact){}
	        @Override
	        public void preSolve(Contact contact, Manifold oldManifold){}
	        @Override
	        public void postSolve(Contact contact, ContactImpulse impulse){}
	    };
	    return contactListener;
	}
	private void createBackground()
	{	
		SpriteBackground bg;
		bg = new SpriteBackground(new Sprite(400, 240,recursos.juego_fondo_arena, vbom));
		setBackground(bg);
	}
	
	private void addBtn(final ITextureRegion btn, final int pX, final int pY) {
		final Sprite sprite = new Sprite(pX, pY, btn, vbom) {
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(jugable){
					
					if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_DOWN) {
						this.setScaleX(1.25f);
						if(btn.equals(recursos.player1_up)){
							jugador1.pdown=false;
							jugador1.pup=true;
						}
						if(btn.equals(recursos.player1_down)){
							jugador1.pup=false;
							jugador1.pdown=true;
							
						}
						if(btn.equals(recursos.player2_up)){
							jugador2.pdown=false;
							jugador2.pup=true;
						}
						if(btn.equals(recursos.player2_down)){
							jugador2.pup=false;
							jugador2.pdown=true;
							
						}
					}else if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_UP){
						this.setScale(1.0f);
						if(btn.equals(recursos.player1_up)){
							jugador1.pdown=false;
							jugador1.pup=false;
						}
						if(btn.equals(recursos.player1_down)){
							jugador1.pup=false;
							jugador1.pdown=false;
							
						}
						if(btn.equals(recursos.player2_up)){
							jugador2.pdown=false;
							jugador2.pup=false;
						}
						if(btn.equals(recursos.player2_down)){
							jugador2.pup=false;
							jugador2.pdown=false;
							
						}
					}
				}
				return true;
			}
		};
		this.attachChild(sprite);//añade los botones 
		if(jugable){	
			this.registerTouchArea(sprite);
		}
		
	}
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		if (pSceneTouchEvent.isActionDown())
	    {
			System.gc();
			if(numJugadores==2){
				if(!tocado){
					tocado=true;
					this.addBtn(recursos.player1_up,30,360);
				    this.addBtn(recursos.player1_down,30,120);
				    this.addBtn(recursos.player2_up,770, 360);
				    this.addBtn(recursos.player2_down,770, 120);
					crearCuerpos();
					ready.setVisible(false);
					if(level!=null){
						level.setVisible(false);
					}
				}
				if(j1win || j2win){
		        	disposeScene();
		        	GestorEscenas.getInstance().loadGameScene(engine,2,true);
		        	
		        }
			
			}else if(numJugadores==1){
				if(!tocado){
					tocado=true;
					jugable=true;
					
					    this.addBtn(recursos.player1_up,30,360);
					    this.addBtn(recursos.player1_down,30,120);
					    this.addBtn(recursos.player2_up,770, 360);
					    this.addBtn(recursos.player2_down,770, 120);
					
					crearCuerpos();
					ready.setVisible(false);
					if(level!=null){
						level.setVisible(false);
					}
				}
				if(players2end){
					disposeScene();
					GestorEscenas.getInstance().loadGameScene(engine, 1,true);	
					
				}
			}
			jugable=true;
	    }
		jugable=true;
	    return false;
	}

	private void displayReadyText(){
		if(numJugadores==1){
			level=new Text(0,0,recursos.font,"Level "+String.valueOf(maxPuntos)+"\nDon't miss any ball",vbom);
			level.setPosition(camara.getCenterX(), camara.getCenterY()+70);
			attachChild(level);
		}
		ready=new Text(0,0,recursos.font,"Tap to play!",vbom);
		ready.setPosition(camara.getCenterX(), camara.getCenterY()-40);
		attachChild(ready);
	}

}
