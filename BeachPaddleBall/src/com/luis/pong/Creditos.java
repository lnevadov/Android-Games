package com.luis.pong;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import com.luis.pong.GestorEscenas.TipoEscena;

public class Creditos extends BaseScene {
	
	@Override
	public void createScene() {
		createBackground();
	}

	@Override
	public void onBackKeyPressed() {
		disposeScene();
		GestorEscenas.getInstance().loadMenuScene(engine,0);//0 jugadores=origen creditos 
	}

	@Override
	public TipoEscena getSceneType() {
		return TipoEscena.ESCENA_CREDITS;
	}

	@Override
	public void disposeScene() {
		GestorRecursos.getInstance().unloadCreditsScreen();
	}
	private void createBackground()
	{	
		SpriteBackground bg = new SpriteBackground(new Sprite(400, 240,recursos.credits_region, vbom));
		setBackground(bg);
	}
		@Override
	public void onHomeKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
