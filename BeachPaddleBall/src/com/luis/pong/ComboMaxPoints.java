package com.luis.pong;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import com.luis.pong.GestorEscenas.TipoEscena;

public class ComboMaxPoints extends BaseScene{
	boolean pulsado=false;
	boolean pulsadoJuego=false;
	boolean primerToque=true;
	private Sprite sp;
	private Sprite sp5;
	private Sprite sp10;
	private Sprite sp20;
	private Sprite sp50;
	
	@Override
	public void createScene() {
		createBackground();
		sp=addBtn(recursos.max, 400, 240);
		sp.setZIndex(4);
	}

	@Override
	public void onBackKeyPressed() {
		disposeScene();
		GestorEscenas.getInstance().loadMenuScene(engine,2);
	}

	@Override
	public TipoEscena getSceneType() {
		return TipoEscena.ESCENA_SPINNER;
	}

	@Override
	public void disposeScene() {
		GestorRecursos.getInstance().unloadComboTextures();
	}
	private void createBackground()
	{	
		SpriteBackground bg = new SpriteBackground(new Sprite(400, 240,recursos.maxp_background_region, vbom));
		setBackground(bg);
	}
	private Sprite addBtn(final ITextureRegion btn, final int pX, int pY) {
		
		Sprite sprite = new Sprite(pX, pY, btn, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_DOWN) {
					this.registerEntityModifier(new RotationModifier(0.6f, 0, 360));
					this.setScale(1.25f);
					if(btn.equals(recursos.max)){
						if(primerToque){
							sp5=addBtn(recursos.max5, 400, 240);
							sp10=addBtn(recursos.max10, 400, 240);
							sp20=addBtn(recursos.max20, 400, 240);
							sp50=addBtn(recursos.max50, 400, 240);
							sp5.setZIndex(0);
							sp10.setZIndex(1);
							sp20.setZIndex(2);
							sp50.setZIndex(3);
							primerToque=false;
						}
						if(!pulsado){
							pulsado=true;
							if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
		                		recursos.sonido_drop_up.play();
		                	}
						}else if(pulsado){
							pulsado=false;
							if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
		                		recursos.sonido_drop_down.play();
		                	}
							
						}
						animar(sp5,recursos.max5);
						animar(sp10,recursos.max10);
						animar(sp20,recursos.max20);
						animar(sp50,recursos.max50);
					}
					if(btn.equals(recursos.max5)){
						pulsadoJuego=true;
						JuegoScene.maxPuntos=5;
					}
					if(btn.equals(recursos.max10)){
						pulsadoJuego=true;
						JuegoScene.maxPuntos=10;
					}
					if(btn.equals(recursos.max20)){
						pulsadoJuego=true;
						JuegoScene.maxPuntos=20;
					}
					if(btn.equals(recursos.max50)){
						pulsadoJuego=true;
						JuegoScene.maxPuntos=50;
					}
				}else if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_UP){
					this.setScale(1.0f);
				}
				
				if(pulsadoJuego){
					if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
                		recursos.sonido_drop_toque.play();
                	}
					//JuegoActivity.musica_menu.pause();
					pulsadoJuego=false;
					disposeScene();
					GestorRecursos.getInstance().unloadComboTextures();
					GestorEscenas.getInstance().loadGameScene(engine,2, true);
				}
				return true;
			}
		};
		this.attachChild(sprite);
		this.registerTouchArea(sprite);
		return sprite;
	}
	private void animar(Sprite sprite, final ITextureRegion btn){
		if(pulsado){
			sprite.setVisible(true);
			if(btn.equals(recursos.max5)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,400,240,220,340));
			}
			if(btn.equals(recursos.max10)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,400,240,550,340));
			}
			if(btn.equals(recursos.max20)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,400,240,220,140));
			}
			if(btn.equals(recursos.max50)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,400,240,550,140));
			}
		}else if(pulsado==false){
			if(btn.equals(recursos.max5)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,240,340,400,240){

			        @Override
			        protected void onModifierFinished(IEntity pItem)
			        {
			                super.onModifierFinished(pItem);
			                pItem.setVisible(false);
			        }
				});
			}
			if(btn.equals(recursos.max10)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,550,340,400,240){
					@Override
			        protected void onModifierFinished(IEntity pItem)
			        {
			                super.onModifierFinished(pItem);
			                pItem.setVisible(false);
			        }
				});
			}
			if(btn.equals(recursos.max20)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,240,140,400,240){
					@Override
			        protected void onModifierFinished(IEntity pItem)
			        {
			                super.onModifierFinished(pItem);
			                pItem.setVisible(false);
			        }
				});
			}
			if(btn.equals(recursos.max50)){
				sprite.registerEntityModifier(new MoveModifier(0.3f,550,140,400,240){
					@Override
			        protected void onModifierFinished(IEntity pItem)
			        {
			                super.onModifierFinished(pItem);
			                pItem.setVisible(false);
			        }
				});
			}
			sortChildren();//ordena las entidades hijas segun su Zindex
			
		}
	}

	@Override
	public void onHomeKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
