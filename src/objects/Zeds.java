package objects;

import managers.GameManager;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

public class Zeds extends BatchedPseudoSpriteParticleSystem {
	
	private GameManager gm = GameManager.getInstance();
	private BatchedPseudoSpriteParticleSystem mParticleSystem;

	public Zeds(IParticleEmitter pParticleEmitter, float pRateMinimum, float pRateMaximum, int pParticlesMaximum, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
		
		mParticleSystem = this;
		
		setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		
		addParticleInitializer(new AlphaParticleInitializer<Entity>(1));
		addParticleInitializer(new VelocityParticleInitializer<Entity>(-50, 50, 20, 70));
		addParticleInitializer(new ExpireParticleInitializer<Entity>(4.0f));
		
		addParticleModifier(new AlphaParticleModifier<Entity>(3.7f, 4.0f, 1, 0));
	}
	
	public void stopZeds() {
		
		mParticleSystem.setParticlesSpawnEnabled(false);
		
		gm.engine.registerUpdateHandler(new TimerHandler(1, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                gm.engine.unregisterUpdateHandler(pTimerHandler);
                mParticleSystem.setVisible(false);
            	mParticleSystem.setIgnoreUpdate(true);
            	mParticleSystem.clearEntityModifiers();
            	mParticleSystem.clearUpdateHandlers();
        		gm.activity.runOnUpdateThread(new Runnable() {
        		    @Override
        		    public void run() {
        		    	gm.gameScene.detachChild(mParticleSystem);
        		    }
        		});
        		mParticleSystem.dispose();
            }
		})); 
		
	}

}