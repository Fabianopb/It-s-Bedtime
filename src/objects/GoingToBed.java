package objects;

import managers.GameManager;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.visivaemobile.itsbedtime.R;

public class GoingToBed extends Entity {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;
	
	private boolean touchable = false;
	private boolean touchableRestart = false;
	public boolean activityEnabled = false;
	
	public Sprite bed;
	public Sprite buttonRestart;
	private Text restartText;
	
	public GoingToBed(float pX, float pY) {
		super(pX, pY);
		createActivityElements();
		registerEntityModifier(new ScaleModifier(0, 1, 0));
	}
	
	private void createActivityElements() {
		bed = new Sprite(150, - 20, gm.bed, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(touchable) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						touchable = false;
						//gm.buttonSound.play();
						clearEntityModifiers();
						gm.gameScene.gameHud.displayMessage(gm.activity.getResources().getString(R.string.msg_time_to_sleep), 1);
						break;
				    }
		        }
    			return false;
		    }
		};
		bed.setZIndex(1);
		attachChild(bed);
		
		/*buttonRestart = new Sprite(-200, 0, gm.button, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(touchable) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						touchable = false;
						//gm.buttonSound.play();
						clearEntityModifiers();
						gm.gameScene.gameHud.displayMessage(gm.activity.getResources().getString(R.string.msg_time_to_sleep), 1);
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(buttonRestart);
		
		restartText = new Text(0, 0, gm.baseFont, gm.activity.getResources().getString(R.string.restart), vbom);
		buttonRestart.attachChild(restartText);*/
		
		
		
		
	}
	
	public void showActivity() {
		gm.gameScene.sleepy.setZIndex(10);
		gm.gameScene.sortChildren();
		registerEntityModifier(new ScaleModifier(.5f, 0, 1, EaseBackOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				touchable = true;
				touchableRestart = true;
				activityEnabled = true;
				bed.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f),	new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1))));
			}
		});
	}
	
	public void finalAnimation() {
		gm.gameScene.sleepy.registerEntityModifier(new MoveModifier(1.0f, gm.activity.centerX, gm.activity.centerY, gm.activity.centerX + 170, gm.activity.centerY + 10));
		gm.gameScene.sleepy.registerEntityModifier(new ScaleModifier(1, 1, 0.9f));
		gm.gameScene.sleepy.registerEntityModifier(new RotationModifier(1.0f, 0, 50){
			@Override protected void onModifierFinished(IEntity pItem) {
				gm.gameScene.gameHud.blanket.registerEntityModifier(new MoveModifier(0.5f, gm.activity.centerX + 130, gm.activity.top + 200, gm.activity.centerX + 130, gm.activity.centerY -20));
				PointParticleEmitter particles = new PointParticleEmitter(160, 50);
				Zeds zeds = new Zeds(particles, 1, 3, 6, gm.zed, vbom);
				attachChild(zeds);
			}
		}); 
	}

}
