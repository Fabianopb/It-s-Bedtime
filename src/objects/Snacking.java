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

public class Snacking extends Entity {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;
	
	public float progress = 0;
	private boolean complete = false;	
	private boolean touchable = false;	
	public boolean activityEnabled = false;
	private boolean getParticles = true;
	
	public Sprite cookie;
	private Crumbs crumbs;
	private float[] cookiePosition = {300, -100};
	CircleParticleEmitter particles;
	
	public Snacking(float pX, float pY) {
		super(pX, pY);
		createActivityElements();
		registerEntityModifier(new ScaleModifier(0, 1, 0));
	}
	
	private void createActivityElements() {
		
		cookie = new Sprite(cookiePosition[0], cookiePosition[1], gm.cookie, vbom) {
			float x0 = gm.activity.centerX; float y0 = gm.activity.centerY; private boolean chewing = false;
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(touchable) {
		        	switch(pTouchEvent.getAction()) {			
					
		        	case TouchEvent.ACTION_MOVE:
		        		if(getParticles) {
			        		getParticles = false;
			        		particles = new CircleParticleEmitter(gm.gameScene.sleepy.getX(), gm.gameScene.sleepy.getY(), 80, 20);
			        	}
			        	this.setPosition(pTouchEvent.getX() - x0, pTouchEvent.getY() - y0);
			        	//Starts or stops bubbling
			        	if(!chewing && Math.abs(this.getX()) <= 80 && Math.abs(this.getY()) <= 10) {
			        		chewing = true;
			        		crumbs = new Crumbs(particles, 30, 50, 75, gm.crumb, vbom);
			        		gm.gameScene.attachChild(crumbs);	        		
			        	}
			        	else if(chewing && (Math.abs(this.getX()) > 80 || Math.abs(this.getY()) > 80)){
			        		chewing = false;
			        		crumbs.stopCrumbs();
			        	}
			        	//Counts progress while bubbling
			        	if(chewing && progress < 100.0f) {
			        		progress += 0.2f;
			        		if(progress > 100.0f) progress = 100.0f;
			        		
			        		gm.gameScene.gameHud.setProgressBarLevel(progress);
			        		
			        	}
			        	if(!complete && progress >= 100.0f) {
			        		complete = true;
			        		gm.gameScene.gameHud.completeRitual(1);
			        		gm.gameScene.gameHud.increaseSleepiness(1);
			        	}
						return true;		        	
					
		        	case TouchEvent.ACTION_UP:
		        		if(chewing) {
							chewing = false;
			        		crumbs.stopCrumbs();
						}
		        		return true;
		        	}
		        	
		        }
    			return false;
		    }
		};
		attachChild(cookie);
		
	}
	
	public void showActivity() {
		cookie.setPosition(cookiePosition[0], cookiePosition[1]);
		registerEntityModifier(new ScaleModifier(.5f, 0, 1, EaseBackOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				touchable = true;
				activityEnabled = true;
				cookie.registerEntityModifier(
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
		cookie.clearEntityModifiers();
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
