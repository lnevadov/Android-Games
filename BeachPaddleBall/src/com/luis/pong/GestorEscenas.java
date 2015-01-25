package com.luis.pong;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class GestorEscenas {
	//posibles escenas
	private BaseScene menuScene;
	private BaseScene splashScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene spinnerScene;
    private BaseScene creditsScene;
    //variables
    private static GestorEscenas INSTANCE;
    private TipoEscena tipoEscenaActual = TipoEscena.ESCENA_SPLASH;
    private BaseScene escenaActual;
    private Engine engine = GestorRecursos.getInstance().engine;
    //aqui declaramos el tipo SceneType
    public enum TipoEscena
    {
        ESCENA_SPLASH,
        ESCENA_MENU,
        ESCENA_GAME,
        ESCENA_LOADING,
        ESCENA_SPINNER,
        ESCENA_CREDITS,
    }
    private GestorEscenas(){
    }
    public void setEscena(BaseScene scene)
    {
        engine.setScene(scene);
        escenaActual = scene;
        tipoEscenaActual = scene.getSceneType();
    }
    
    //CON ESTE METODO NOS ASEGURAMOS DE MOSTRAR LA ESCENA CORRECTA
    public void setEscena(TipoEscena tipoEscena)
    {
        switch (tipoEscena)
        {
	        case ESCENA_MENU:
	        	setEscena(menuScene);
	        	break;
            case ESCENA_GAME:
            	setEscena(gameScene);
                break;
            case ESCENA_SPLASH:
            	setEscena(splashScene);
                break;
            case ESCENA_LOADING:
                setEscena(loadingScene);
                break;
            case ESCENA_SPINNER:
            	setEscena(spinnerScene);
            	break;
            case ESCENA_CREDITS:
            	setEscena(creditsScene);
            	break;	
            default:
                break;
        }
    }
    //******ESCENA SPLASH
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    { 
        GestorRecursos.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        escenaActual = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    public void disposeSplashScene()
    {
        GestorRecursos.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    //*******ESCENA MENU
    public void createMenuScene()
    {
        GestorRecursos.getInstance().loadMenuResources();
        menuScene = new MenuPrincipalScene();
        setEscena(menuScene);
        loadingScene = new LoadingScene();
        GestorEscenas.getInstance().setEscena(menuScene);
        disposeSplashScene();
    }
    
    public void loadMenuScene(final Engine mEngine, int player)
    {
        setEscena(loadingScene);
        if(player==0){
        	creditsScene.dispose();
        	GestorRecursos.getInstance().unloadCreditsScreen();
        }
        if(player==2){
        	spinnerScene.disposeScene();
        	GestorRecursos.getInstance().unloadComboTextures();
        }
        if(player==1){
        	gameScene.disposeScene();
            GestorRecursos.getInstance().unloadGameTextures();
        }
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                GestorRecursos.getInstance().loadMenuTextures();
                setEscena(menuScene);
                if(JuegoActivity.prefs.getString("sonido", "true").equals("true")){
                	if(!JuegoActivity.musica_menu.isPlaying()){//para cuando le doy desde el juego a backkey
                		JuegoActivity.musica_menu.seekTo(0);
                		JuegoActivity.musica_menu.play();
                	}
        		}
            }
        }));
    }
    //*******ESCENA SPINNER
    public void loadSpinnerScene(final Engine mEngine)
    {	
    	setEscena(loadingScene);
        GestorRecursos.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                GestorRecursos.getInstance().loadComboResources();
                spinnerScene = new ComboMaxPoints();
                setEscena(spinnerScene);
            }
        }));
    }
  //*******ESCENA CREDITOS
    public void loadCreditsScene(final Engine mEngine)
    {	
    	setEscena(loadingScene);
        GestorRecursos.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                GestorRecursos.getInstance().loadCreditsScreen();
                creditsScene = new Creditos();
                setEscena(creditsScene);
            }
        }));
    }
    //*******ESCENA JUEGO
    public void loadGameScene(final Engine mEngine, final int players,final boolean jugable)
    {	
    	setEscena(loadingScene);
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                GestorRecursos.getInstance().loadGameResources();
                JuegoScene.numJugadores=players;
                JuegoScene.jugable=jugable;
                gameScene = new JuegoScene();                
                setEscena(gameScene);
                
            }
        }));
        JuegoScene.jugable=true;
    }
    
    public static GestorEscenas getInstance()
    {
    	if(INSTANCE == null){
			INSTANCE = new GestorEscenas();
		}
		return INSTANCE;
    }
    
    public TipoEscena getTipoEscenaActual()
    {
        return tipoEscenaActual;
    }
    
    public BaseScene getEscenaActual()
    {
        return escenaActual;
    }
}