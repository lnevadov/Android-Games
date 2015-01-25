package com.luis.pong;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.luis.pong.GestorEscenas.TipoEscena;

import android.app.Activity;

public abstract class BaseScene extends Scene{
	
	protected Engine engine;
    protected Activity activity;
    protected GestorRecursos recursos;
    protected VertexBufferObjectManager vbom;
    protected Camera camara;
    //constructor
    public BaseScene()
    {
        this.recursos = GestorRecursos.getInstance();
        this.engine = recursos.engine;
        this.activity = recursos.activity;
        this.vbom = recursos.vbom;
        this.camara = recursos.camara;
        createScene();
    }
    //metodos abstractos
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    
    public abstract TipoEscena getSceneType();
    
    public abstract void disposeScene();
    
    public abstract void onHomeKeyPressed();
    //hacer el onresume
}
