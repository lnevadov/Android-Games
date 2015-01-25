package com.luis.pong;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.luis.pong.GestorEscenas.TipoEscena;

public class LoadingScene extends BaseScene{
	@Override
	public void createScene() {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(400, 240, recursos.font, "Loading...", vbom));
        this.setTouchAreaBindingOnActionDownEnabled(false);
	}

	@Override
	public void onBackKeyPressed() {
		return;
		
	}

	@Override
	public TipoEscena getSceneType() {
        return TipoEscena.ESCENA_LOADING;
	}

	@Override
	public void disposeScene() {
		
	}

	@Override
	public void onHomeKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
