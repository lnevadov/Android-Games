package com.luis.pong;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Pelota extends Sprite{
	private Body bola;
	public boolean start=true;
	boolean restart=false;
	boolean score1=false;
	boolean score2=false;
	boolean stop=false;
	public Pelota(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, ITextureRegion textura){
		super(pX, pY, textura, vbo);
		createPhysics(camera, physicsWorld);
		
	}
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {   
		FixtureDef fixture = PhysicsFactory.createFixtureDef(1, 1, 0);
    	bola=PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, fixture);
    	bola.setUserData("bola");
        bola.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bola, true, true)
        {
    		
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
            	super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
                if(start){
                	start=false;
                	final Vector2 vector2 = Vector2Pool.obtain(0,0);
        			vector2.set(MathUtils.randomSign() * MathUtils.random(6, 7), MathUtils.randomSign() * MathUtils.random(6, 7));
        			bola.setLinearVelocity(vector2);
        			Vector2Pool.recycle(vector2);
                }
                if(restart){//para que el signo en la X siempre sea el correcto segun quien marque, porque en start es aleatorio
        			restart=false;
        			final Vector2 vector2 = Vector2Pool.obtain(0,0);
        			if(score1){
        				score1=false;
        				vector2.set(-1*MathUtils.random(6, 7), MathUtils.randomSign() * MathUtils.random(6, 7));
        			}else if(score2){
        				score2=false;
        				vector2.set(MathUtils.random(6, 7), MathUtils.randomSign() * MathUtils.random(6, 7));
        			}
        			bola.setLinearVelocity(vector2);
        			Vector2Pool.recycle(vector2);
        		}
                //asi me aseguro de que nunca se quede parada la bola con respecto a la X
                if(bola.getLinearVelocity().x==0){
                	if(bola.getLinearVelocity().x==0 && getX()>720){
                    	bola.applyLinearImpulse(-3,bola.getLinearVelocity().y,0,getY());
                    }
                	else if(bola.getLinearVelocity().x==0 && getX()<80){
                    	bola.applyLinearImpulse(3,bola.getLinearVelocity().y,800,getY());
                    }else{
                    	bola.setLinearVelocity(MathUtils.randomSign() * MathUtils.random(3, 3), MathUtils.randomSign() * MathUtils.random(3, 3));
                    }
                }
                if(stop){
                	start=false;
                	bola.setLinearVelocity(0, 0);
                }
                
            }
        });
    	
	}
}
