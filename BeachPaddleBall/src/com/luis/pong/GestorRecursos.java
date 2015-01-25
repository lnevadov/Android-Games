package com.luis.pong;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
/**
 * @author Luis Nevado
 * @version 1.0
 */
public class GestorRecursos {
	/*
	 * Esta será una clase SINGLETON lo que nos permitirá tener acceso global a ella
	 * ésta clase sólo tendrá una instancia
	 */
	//VARIABLES
	private static GestorRecursos INSTANCE;
	public Engine engine;
	public JuegoActivity activity;
	public Camera camara;
	public VertexBufferObjectManager vbom;
	//variable para pantalla splashScreen
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	public Font font;
	//variables de la panalla creditos
	public ITextureRegion credits_region;
	private BuildableBitmapTextureAtlas creditsTextureAtlas;
	//variables escena de menu
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion play2_region;
	public ITextureRegion exit_region;
	public ITextureRegion info_region;	
	public ITiledTextureRegion sonido;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public Sound sonido_toque;
	public Sound sonido_menu;
	//variables para Escena Juego
	public ITextureRegion juego_background_region;
	public ITextureRegion juego_fondo_arena;
	private BuildableBitmapTextureAtlas juegoTextureAtlas;
	private BuildableBitmapTextureAtlas juego2TextureAtlas;
	public ITextureRegion player1_up;
	public ITextureRegion player1_down;
	public ITextureRegion player2_up;
	public ITextureRegion player2_down;
	public ITextureRegion loose;
	public ITextureRegion win;
	public Sound sonido_toque_juego;
	public Sound sonido_gol;
	public Sound sonido_win;
	//recursos escena Spinner
	private BuildableBitmapTextureAtlas maxPointAtlas;
	public ITextureRegion max;//alomejor lo hago animado con tiled
	public ITextureRegion max5;
	public ITextureRegion max10;
	public ITextureRegion max20;
	public ITextureRegion max50;
	public ITextureRegion maxp_background_region;
	public Sound sonido_drop_up;
	public Sound sonido_drop_down;
	public Sound sonido_drop_toque;
	//recursos jugadores
	public ITextureRegion pala1;
	public ITextureRegion pala2;
	public ITextureRegion bola;
	//Para asegurar que la clase no puede ser instanciada nuevamente ponemos el contructor privado
	private GestorRecursos(){
	}
	
	//***********RECURSOS DE LA SPLASHSCREEN
	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "beachSplash.png", 0, 0);
		splashTextureAtlas.load();
	}
	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}
	//***********RECURSOS DE LOS CREDItOS
	public void loadCreditsScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/creditos/");
		creditsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		credits_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditsTextureAtlas, activity, "creditos.png");
		try //mirar lo del BlackPawnTextureAtlasBuiler
		{
		    this.creditsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.creditsTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
		        Debug.e(e);
		}
	}
	public void unloadCreditsScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}		
	//***********RECURSOS DEL MENU
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
	    loadMenuFonts();
	}
	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "fondomenu2.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "arcade.png");
		play2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "2players.png");
		exit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "exit.png");
		info_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "info.png");
		sonido=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "altavoz_tiled.png",2,1);       

		try //mirar lo del BlackPawnTextureAtlasBuiler
		{
		    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.menuTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
		        Debug.e(e);
		}
	}
	private void loadMenuFonts()
	{
	    FontFactory.setAssetBasePath("fonts/");
	    final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	    font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "street_soul.ttf", 70f, true, Color.WHITE_ARGB_PACKED_INT, 3f, Color.BLACK_ABGR_PACKED_INT); 
	    font.load();
	}
	public void loadMenuTextures()
	{
	    menuTextureAtlas.load();
	}
	public void unloadMenuTextures()
	{
	    menuTextureAtlas.unload();
	}
	
	private void loadMenuAudio() {
		
	    try 
	    {
	        this.sonido_toque=SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/toque.ogg");
	        
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	//***********RECURSOS DE COMBOMAXPOINTS
	
	public void loadComboResources(){
		loadComboGraphics();
		loadComboFonts();
		loadComboAudio();
	}
	private void loadComboGraphics(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/maxpoint/");
		maxPointAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR);     
		max=BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "MaxPoints.png");
		max5=BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "MaxPoints5.png");
		max10=BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "MaxPoints10.png");
		max20=BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "MaxPoints20.png");
		max50=BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "MaxPoints50.png");
		maxp_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(maxPointAtlas, activity, "fondo_arena_combo.png"); 
		try 
		{
		    this.maxPointAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.maxPointAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
		        Debug.e(e);
		}
		
	}
	private void loadComboFonts(){
		
	}
	private void loadComboAudio(){
		try 
	    {
	        sonido_drop_up= SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/maxpoint/drop_up.ogg");
	        sonido_drop_down= SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/maxpoint/drop_down.ogg");
	        sonido_drop_toque= SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/toque.ogg");
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	public void loadComboTextures(){
		maxPointAtlas.load();
	}
	public void unloadComboTextures()
	{
		maxPointAtlas.unload();
	}
	
	//***********RECURSOS DE LA JUEGO SCENE
	
	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		juegoTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR);
		//juego_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "fondo_juego1.png");
		juego_fondo_arena = BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "fondoArcade.png");
		player1_up=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "1btnup.png");
		player1_down=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "1btndown.png");
		player2_up=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "2btnup.png");
		player2_down=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "2btndown.png");
		pala1=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "pala1.png");
		pala2=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "pala2.png");
		bola=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juegoTextureAtlas, activity, "bola.png");
		
		juego2TextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR);
		loose=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juego2TextureAtlas, activity, "lose.png");
		win=BitmapTextureAtlasTextureRegionFactory.createFromAsset(juego2TextureAtlas, activity, "win.png");
		try 
		{
		    this.juegoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.juegoTextureAtlas.load();
		    this.juego2TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		    this.juego2TextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
		        Debug.e(e);
		}
	}
	public void loadGameTextures(){
		juegoTextureAtlas.load();
	}
	public void unloadGameTextures()
	{
	    juegoTextureAtlas.unload();
	    juego2TextureAtlas.unload();
	}
	private void loadGameFonts() {
		
	}

	public void loadGameAudio() {
		try 
	    {
	        sonido_gol= SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/score.ogg");
	        sonido_toque_juego= SoundFactory.createSoundFromAsset(engine.getSoundManager(),this.activity, "sounds/toque.ogg");
	        sonido_win=SoundFactory.createSoundFromAsset(engine.getSoundManager(),activity, "sounds/win.ogg");
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	
	//GETTERS Y SETTERS
	public static GestorRecursos getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GestorRecursos();
		}
		return INSTANCE;
	}
	/*
	 usamos este método al principio de la pantalla "cargando..." para preparar los recuros correctamente, 
	 estableciendo todos los parámetros, asi podremos acceder a ellos desde cualquier clase
	 */
	public static void prepareManager(Engine engine, JuegoActivity activity, Camera camara, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camara = camara;
		getInstance().vbom = vbom;
	}

}
