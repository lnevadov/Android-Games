package com.luis.pong;

import java.io.IOException;
import android.content.SharedPreferences; 

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.input.touch.TouchEvent;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.luis.pong.GestorEscenas.TipoEscena;

public class MenuPrincipalScene extends BaseScene implements IOnMenuItemClickListener{
	private MenuScene menuChildScene;
	private final int MENU_1PLAYER = 0;
	private final int MENU_2PLAYER = 1;
	private final int MENU_EXIT = 2;
	private final int MENU_SONIDO = 3;
	private final int MENU_CREDITS = 4; 
	
	private Engine engine = GestorRecursos.getInstance().engine;
	Sound menu_sound;

	private void loadSound(){
		try {
			Sound menu_sound=SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/menu.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();	
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public TipoEscena getSceneType() {
		return TipoEscena.ESCENA_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
	}
	
	private void createBackground() {
		attachChild(new Sprite(400, 240,recursos.menu_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camara);
		menuChildScene.setPosition(0, 0);
		if(JuegoActivity.prefs.getString("sonido", "true").equals("false")){
			recursos.sonido.setCurrentTileIndex(1);
		}else{
			recursos.sonido.setCurrentTileIndex(0);
		}
		final IMenuItem play1MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_1PLAYER, recursos.play_region,vbom), 1.2f, 1);
		final IMenuItem play2MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_2PLAYER, recursos.play2_region,vbom), 1.2f, 1);
		final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXIT,recursos.exit_region, vbom), 1.2f, 1);
		final IMenuItem soundMenuItem=new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SONIDO,recursos.sonido, vbom), 1.2f, 1);
		final IMenuItem creditsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_CREDITS, recursos.info_region,vbom), 1.2f, 1);
		menuChildScene.addMenuItem(play1MenuItem);
		menuChildScene.addMenuItem(play2MenuItem);
		menuChildScene.addMenuItem(exitMenuItem);
		menuChildScene.addMenuItem(soundMenuItem);
		menuChildScene.addMenuItem(creditsMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		//en este caso nos pone las coordenadas con respecto al centro de la pantalla
		play1MenuItem.setPosition(400, 250);
		play2MenuItem.setPosition(400, 120);
		exitMenuItem.setPosition(700,50);
		soundMenuItem.setPosition(50, 440);
		creditsMenuItem.setPosition(50, 50);
		
		menuChildScene.setOnMenuItemClickListener(this);//este escuchador dispara a OnMenuItemClicked
		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		SharedPreferences.Editor editor = JuegoActivity.prefs.edit();
	    switch(pMenuItem.getID())
	    {
	        case MENU_2PLAYER:
	            //cargamos escena d juego
	        	if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	        		recursos.sonido_toque.play();
	        	}
	            GestorEscenas.getInstance().loadSpinnerScene(engine);	
	            return true;
	        case MENU_1PLAYER:
	        	if(JuegoActivity.prefs.getString("nivel", "1").equals("1")){
    				editor.putString("nivel", "1");
    	   		}
	    	   	editor.commit();
	        	if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	        		recursos.sonido_toque.play();
	        	}
	        	//JuegoActivity.musica_menu.pause();
	        	JuegoScene.maxPuntos=Integer.parseInt(JuegoActivity.prefs.getString("nivel", "1"));//mas tarde cargar el nivel desde prefs
	        	GestorRecursos.getInstance().unloadMenuTextures();
	            GestorEscenas.getInstance().loadGameScene(engine, 1,true);
	        	return true;
	        case MENU_EXIT:
	        	if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	        		recursos.sonido_toque.play();
	        	}
	        	System.exit(0);
	            return true;
	        case MENU_CREDITS:
	        	if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
	        		recursos.sonido_toque.play();
	        	}
	        	GestorRecursos.getInstance().unloadMenuTextures();
	            GestorEscenas.getInstance().loadCreditsScene(engine);
	        	return true;
	        case MENU_SONIDO:
	    	   if(JuegoActivity.prefs.getString("sonido", "true").equals("false")){
	   				editor.putString("sonido", "true");
	   				JuegoActivity.musica_menu.play();
		   		}else{
		   			editor.putString("sonido", "false");
		   			if(JuegoActivity.musica_menu.isPlaying()) {
	   					JuegoActivity.musica_menu.pause();
	   				}
		   		}
		   	    editor.commit();
		   	    detachChild(menuChildScene);
		   	    createMenuChildScene();
	        default:
	            return false;
	    }
	}


	@Override
	public void onHomeKeyPressed() {
		System.exit(0);
	}

	

}
