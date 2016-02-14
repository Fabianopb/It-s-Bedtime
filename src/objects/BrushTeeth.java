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

public class BrushTeeth extends Entity {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;
	
	public float progress = 0;
	private boolean complete = false;	
	private boolean touchable = false;	
	public boolean activityEnabled = false;
	private boolean getParticles = true;
	
	public Sprite brushTeeth;
	private Foam foam;
	private float[] brushTeethPosition = {300, -100};
	CircleParticleEmitter particles;
	
	public BrushTeeth(float pX, float pY) {
		super(pX, pY);
		createActivityElements();
		registerEntityModifier(new ScaleModifier(0, 1, 0));
	}
	
	private void createActivityElements() {
		
		brushTeeth = new Sprite(brushTeethPosition[0], brushTeethPosition[1], gm.toothbrush, vbom) {
			float x0 = gm.activity.centerX; float y0 = gm.activity.centerY; private boolean foaming = false;
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
			        	if(!foaming && Math.abs(this.getX()) <= 80 && Math.abs(this.getY()) <= 10) {
			        		foaming = true;
			        		foam = new Foam(particles, 5, 10, 5, gm.foam, vbom);
			        		gm.gameScene.attachChild(foam);	        		
			        	}
			        	else if(foaming && (Math.abs(this.getX()) > 80 || Math.abs(this.getY()) > 80)){
			        		foaming = false;
			        		foam.stopFoam();
			        	}
			        	//Counts progress while bubbling
			        	if(foaming && progress < 100.0f) {
			        		progress += 0.2f;
			        		if(progress > 100.0f) progress = 100.0f;
			        		
			        		gm.gameScene.gameHud.setProgressBarLevel(progress);
			        		
			        	}
			        	if(!complete && progress >= 100.0f) {
			        		complete = true;
			        		gm.gameScene.gameHud.completeRitual(2);
			        		gm.gameScene.gameHud.increaseSleepiness(1);
			        	}
						return true;		        	
					
		        	case TouchEvent.ACTION_UP:
		        		if(foaming) {
							foaming = false;
			        		foam.stopFoam();
						}
		        		return true;
		        	}
		        	
		        }
    			return false;
		    }
		};
		attachChild(brushTeeth);
		
	}
	
	public void showActivity() {
		brushTeeth.setPosition(brushTeethPosition[0], brushTeethPosition[1]);
		registerEntityModifier(new ScaleModifier(.5f, 0, 1, EaseBackOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				touchable = true;
				activityEnabled = true;
				brushTeeth.registerEntityModifier(
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
		brushTeeth.clearEntityModifiers();
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
