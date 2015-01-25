package com.luis.pong;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Jugadores extends Sprite{
	
	private Body pala;
	//public Sprite pala1sprite;

	public boolean pup=false;
	public boolean pdown=false;
	
	//private final PhysicsWorld mPhysicsWorld;
	public Jugadores(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, ITextureRegion textura){
		super(pX, pY, textura, vbo);
		createPhysics(camera, physicsWorld);
		
	}
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {   
		FixtureDef fixture = PhysicsFactory.createFixtureDef(1, 1, 0);
		//fixture.isSensor=true;
    	pala=PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, fixture);
    	pala.setUserData("pala");
        pala.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, pala, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
            	super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
                if(pup){
            		pala.setLinearVelocity(new Vector2(0,20));
            		if(getY()>440){//altura-mitad de altura plataforma(480-32) ya que la plataforma es de 30 px de alto
                		pup=false;
                		pala.setLinearVelocity(new Vector2(0,0));
                    }
        		}else if(pdown){	
        			pala.setLinearVelocity(new Vector2(0,-20));
        			if(getY()<40 ){//0+mitad de altura de la platadorma(altura plataforma=30px)
                		pdown=false;
                		pala.setLinearVelocity(new Vector2(0,0));
                    }
        		}else{
        			pala.setLinearVelocity(0, 0);
        		}
                
            }
        });
		
	}
        
    

}
