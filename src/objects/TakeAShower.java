package objects;

import managers.GameManager;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseBackIn;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseCircularOut;

public class TakeAShower extends Entity {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;
	
	public float progress = 0;
	private boolean complete = false;	
	private boolean touchable = false;	
	public boolean activityEnabled = false;
	private boolean getParticles = true;
	
	private Sprite bathTub, bathHat;
	public Sprite bathSponge;
	private Bubbles bubbles;
	private float[] bathSpongePosition = {300, -100};
	CircleParticleEmitter particles;
	
	public TakeAShower(float pX, float pY) {
		super(pX, pY);
		createActivityElements();
		registerEntityModifier(new ScaleModifier(0, 1, 0));
	}
	
	private void createActivityElements() {
		
		bathTub = new Sprite(0, -70, gm.bathTub, vbom);
		attachChild(bathTub);
		
		bathHat = new Sprite(0, 130, gm.bathHat, vbom);
		attachChild(bathHat);
		
		bathSponge = new Sprite(bathSpongePosition[0], bathSpongePosition[1], gm.bathSponge, vbom) {
			float x0 = gm.activity.centerX; float y0 = gm.activity.centerY; private boolean bubbling = false;
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(touchable) {
		        	switch(pTouchEvent.getAction()) {			
					
		        	case TouchEvent.ACTION_MOVE:
						if(getParticles) {
			        		getParticles = false;
			        		particles = new CircleParticleEmitter(gm.gameScene.sleepy.getX(), gm.gameScene.sleepy.getY(), 100, 100);
			        	}
			        	this.setPosition(pTouchEvent.getX() - x0, pTouchEvent.getY() - y0);
			        	//Starts or stops bubbling
			        	if(!bubbling && Math.abs(this.getX()) <= 80 && Math.abs(this.getY()) <= 10) {
			        		bubbling = true;
			        		bubbles = new Bubbles(particles, 10, 20, 20, gm.bubble, vbom);
			        		gm.gameScene.attachChild(bubbles);	        		
			        	}
			        	else if(bubbling && (Math.abs(this.getX()) > 80 || Math.abs(this.getY()) > 80)){
			        		bubbling = false;
			        		bubbles.stopBubles();
			        	}
			        	//Counts progress while bubbling
			        	if(bubbling && progress < 100.0f) {
			        		progress += 0.2f;
			        		if(progress > 100.0f) progress = 100.0f;
			        		
			        		gm.gameScene.gameHud.setProgressBarLevel(progress);
			        		
			        	}
			        	if(!complete && progress >= 100.0f) {
			        		complete = true;
			        		gm.gameScene.gameHud.completeRitual(0);
			        		gm.gameScene.gameHud.increaseSleepiness(1);
			        	}
						return true;
					
					case TouchEvent.ACTION_UP:
						if(bubbling) {
							bubbling = false;
			        		bubbles.stopBubles();
						}
		        		return true;
		        	}
		        	
		        }
    			return false;
		    }
		};
		attachChild(bathSponge);
	}
	
	public void showActivity() {
		bathSponge.setPosition(bathSpongePosition[0], bathSpongePosition[1]);
		registerEntityModifier(new ScaleModifier(.5f, 0, 1, EaseBackOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				touchable = true;
				activityEnabled = true;
				bathSponge.registerEntityModifier(
						new LoopEntityModifier(
								new SequenceEntityModifier(
										new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f), 
										new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1)
								)
						)
				);
			}
		});
	}
	
	public void hideActivity(final boolean end) {
		touchable = false;
		activityEnabled = false;
		bathSponge.clearEntityModifiers();
		registerEntityModifier(new ScaleModifier(.5f, 1, 0, EaseBackIn.getInstance()){
			@Override protected void onModifierFinished(IEntity pItem) {
				if(!end) {
					gm.gameScene.bringSelectorBack();
				}
				else {
					gm.gameScene.gameHud.progressBar.registerEntityModifier(new MoveModifier(1, gm.activity.left + 50, gm.activity.centerY + 20, gm.activity.left - 100, gm.activity.centerY + 20, EaseCircularOut.getInstance()));
					gm.gameScene.goingToBed.showActivity();
				}
			}
		});
	}

}
