package com.luis.pong;


import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.luis.pong.GestorEscenas.TipoEscena;
import org.andengine.engine.camera.Camera;


public class SplashScene extends BaseScene {
	private Sprite splash;
	@Override
	public void createScene() {
		splash = new Sprite(0, 0, recursos.splash_region, vbom)
		{
		    @Override
		    protected void preDraw(GLState pGLState, Camera pCamera)//para habilitar el dither, mejores texturas 
		    {
		       super.preDraw(pGLState, pCamera);
		       pGLState.enableDither();
		    }
		};
		        
		splash.setScale(1.0f);//escala la imagen a x1.5
		splash.setPosition(400, 240);//centrado 
		attachChild(splash);
	}
	@Override
	public void onBackKeyPressed() {
	
	}
	@Override
	public TipoEscena getSceneType() {
		return TipoEscena.ESCENA_SPLASH;
	}
	@Override
	public void disposeScene() {
		splash.detachSelf();
	    splash.dispose();
	    this.detachSelf();
	    this.dispose();
	}
	@Override
	public void onHomeKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
