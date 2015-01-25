package com.luis.pong;

import java.io.IOException;

import android.content.SharedPreferences; 

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.BaseGameActivity;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

/**
 * @author Luis Nevado
 * @version 1.0
 */
public class JuegoActivity extends BaseGameActivity {
	private static final int ANCHO=800;
	private static final int ALTO=480;
	private Camera camara;
	private GestorRecursos recursos;
	private  Menu menu;
	public static Music musica_menu;
	public static SharedPreferences prefs;

	/*
	 * Creo las opciones del engine: los ajustes de la camara, necesitaremos sonidos, y quitamos el bloqueo de pantalla
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {
		camara = new Camera(0, 0, 800, 480);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(800, 480), this.camara);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "MultiTouch detectado!!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "MultiTouch detectado, pero tu dispositivo tiene problemas distinguiendo entre dedos", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Los sentimos, tu dispositivo no soporta MultiTouch", Toast.LENGTH_LONG).show();
		}
	    return engineOptions;
	}
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)throws IOException {
		GestorRecursos.prepareManager(mEngine, this, camara, getVertexBufferObjectManager());
	    recursos = GestorRecursos.getInstance();
	    musica_menu=MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "sounds/Feelin_Good.ogg");
	    musica_menu.setVolume(0.2f, 0.2f);
	    musica_menu.setLooping(true);
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)throws IOException {
	    GestorEscenas.getInstance().createSplashScene(pOnCreateSceneCallback);	
    }

	@Override
	public void onPopulateScene(Scene pScene,OnPopulateSceneCallback pOnPopulateSceneCallback)throws IOException {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback(){
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
                GestorEscenas.getInstance().createMenuScene();
                if(prefs.getString("sonido", "true").equals("true")){
                	musica_menu.play();
                }
                cargarConfiguracion();
            }
		} ));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}


	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	    guardarConfiguracion();
	    System.exit(0);
	}

	@Override
	public void onPause(){
		super.onPause();
		if(JuegoActivity.musica_menu.isPlaying()) {
			JuegoActivity.musica_menu.pause();
		}
	}
	@Override
	public void onResumeGame(){
		super.onResumeGame();
		if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
			JuegoActivity.musica_menu.resume();
    	}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        GestorEscenas.getInstance().getEscenaActual().onBackKeyPressed();
	    }
	    return false; 
	} 
	//guardar configuración aplicación Android usando SharedPreferences
	 public void guardarConfiguracion()
	 {
	    this.prefs = getSharedPreferences("InformacionJuego", Context.MODE_PRIVATE);
	    
	    
	 }
	 //cargar configuración aplicación Android usando SharedPreferences
	  public void cargarConfiguracion(){    	
	    prefs = getSharedPreferences("InformacionJuego", Context.MODE_PRIVATE);	
	    Log.i("Prueba SONIDO",prefs.getString("sonido", "true"));
	    Log.i ("Prueba nivel!!!!",prefs.getString("nivel", "1"));
	    
	  }
    //en el evento "Abrir aplicación" leemos los datos de configuración del fichero xml
    @Override
    protected void onStart() {
      super.onStart();
      guardarConfiguracion();
    }  
    
}
