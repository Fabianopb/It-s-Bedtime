package objects;

import managers.Colors;
import managers.GameManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;

import com.visivaemobile.itsbedtime.R;

public class GameHud extends HUD {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;
	
	private TiledSprite[] rituals = new TiledSprite[4];
	public Sprite progressBar;
	public Rectangle progressBarFill;
	public Text progressValue;
	private TickerText zzz;
	public Sprite button;
	public Sprite backButton;
	private Text message;
	public Sprite blanket;
	private int completedRituals = 0;
	
	public boolean touchable = false;
	public boolean backTouchable = false;
	
	private float centerX, centerY, top, right, bottom, left;
	
	public GameHud() {
		super();
		
		centerX = gm.activity.centerX; centerY = gm.activity.centerY;
		top = gm.activity.top; right = gm.activity.right; bottom = gm.activity.bottom; left = gm.activity.left;
		
		Gradient g = new Gradient(centerX, bottom + 30, gm.activity.CAMERA_WIDTH, 60, vbom) {
			@Override
			protected void preDraw(final GLState pGLState, final Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		g.setGradient(Color.parseColor("#4Dffffff"), Color.parseColor("#00ffffff"), 1, 0);
		attachChild(g);
		
		Text ritualText = new Text(left + 30, bottom + 30, gm.baseFont, gm.activity.getResources().getString(R.string.ritual), vbom);
		ritualText.setAnchorCenterX(0);
		attachChild(ritualText);
		
		for(int i = 0; i < 4; i++) {
			rituals[i] = new TiledSprite(left + 130 + 33*i, bottom + 30, gm.rituals, vbom);
			attachChild(rituals[i]);
		}
		
		progressBar = new Sprite(left - 100, centerY + 20, gm.progressBar, vbom);
		attachChild(progressBar);
		
		progressBarFill = new Rectangle(17, 6, 22, 355, vbom);
		progressBarFill.setAnchorCenterY(0);
		progressBarFill.setColor(Color.parseColor(Colors.white));
		progressBarFill.setScaleY(.0f);
		progressBar.attachChild(progressBarFill);
		
		progressValue = new Text(30, 11 + 355 * progressBarFill.getScaleY(), gm.baseFont, "_%", 6, vbom);
		progressValue.setAnchorCenterX(0);
		progressBar.attachChild(progressValue);
		
		Text sleepinessText = new Text(centerX - 50, bottom + 30, gm.baseFont, gm.activity.getResources().getString(R.string.sleepiness), vbom);
		attachChild(sleepinessText);
		
		zzz = new TickerText(centerX - 50 + sleepinessText.getWidth() / 2 + 10, 30, gm.boldFont, "ZZZZZZZZZZ", new TickerTextOptions(1), vbom);
		zzz.setAnchorCenterX(0);
		zzz.setText("");
		attachChild(zzz);
		
		button = new Sprite(right - 90, bottom + 30, gm.button, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(touchable) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						touchable = false;
						for(int i = 0; i < 4; i++) {
							if(rituals[i].getCurrentTileIndex() == 0) {
								displayMessage(gm.activity.getResources().getString(R.string.msg_missing_rituals), 0);
								break;
							}
							if(i == 3) {
								button.clearEntityModifiers();
								backButton.registerEntityModifier(new MoveYModifier(1, top - 30, top + 30));
								if(gm.gameScene.takeAShower.activityEnabled) {
						    		gm.gameScene.takeAShower.hideActivity(true);
						    	}
						    	else if(gm.gameScene.snacking.activityEnabled) {
						    		gm.gameScene.snacking.hideActivity(true);
						    	}
						    	else if(gm.gameScene.brushTeeth.activityEnabled) {
						    		gm.gameScene.brushTeeth.hideActivity(true);
						    	}
						    	else if(gm.gameScene.reading.activityEnabled) {
						    		gm.gameScene.reading.hideActivity(true);
						    	}
						    	else {
						    		gm.gameScene.hideSelectorAndPlayActivity(10);
						    	}
							}
						}
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(button);
		
		backButton = new Sprite(right - 90, top + 30, gm.button, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(backTouchable) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						backTouchable = false;
						if(gm.gameScene.takeAShower.activityEnabled) {
				    		gm.gameScene.takeAShower.hideActivity(false);
				    	}
				    	else if(gm.gameScene.snacking.activityEnabled) {
				    		gm.gameScene.snacking.hideActivity(false);
				    	}
				    	else if(gm.gameScene.brushTeeth.activityEnabled) {
				    		gm.gameScene.brushTeeth.hideActivity(false);
				    	}
				    	else if(gm.gameScene.reading.activityEnabled) {
				    		gm.gameScene.reading.hideActivity(false);
				    	}
				    }
		        }
    			return false;
		    }
		};
		attachChild(backButton);
		
		Text backText = new Text(backButton.getWidth() / 2, backButton.getHeight() / 2, gm.baseFont, gm.activity.getResources().getString(R.string.back), vbom);
		backButton.attachChild(backText);
		
		Text goToBedText = new Text(right - 90, bottom + 30, gm.baseFont, gm.activity.getResources().getString(R.string.go_to_bed), vbom);
		attachChild(goToBedText);
		
		message = new Text(centerX, top + 30, gm.baseFont, "", 50, vbom);
		attachChild(message);
		
		blanket = new Sprite(gm.activity.centerX + 130, top + 200, gm.blanket, vbom);
		attachChild(blanket);
		
		setVisible(false);
		
	}
	
	public void setProgressBarLevel(float progress) {
		progressBarFill.setScaleY(progress/100.0f);
		
		progressValue.setText("_" + Integer.toString((int)progress) + "%");
		progressValue.setY(11 + 355 * progressBarFill.getScaleY());		
	}
	
	public void completeRitual(final int ritualId) {
		completedRituals++;
		if(completedRituals == 4) {
			gm.gameScene.sleepy.closeEyes();
		}
		rituals[ritualId].registerEntityModifier(new ScaleModifier(0.5f, 1, 0, 1, 1) {
			@Override protected void onModifierFinished(IEntity pItem) {
				rituals[ritualId].setCurrentTileIndex(1);
				rituals[ritualId].registerEntityModifier(new ScaleModifier(0.5f, 0, 1, 1, 1));
			}
		});
	}
	
	public void increaseSleepiness(int addedZeds) {
		for(int i = 0; i < addedZeds; i++) {
			zzz.setText(zzz.getText() + "Z");
		}
	}
	
	public void displayMessage(String mMessage, final int action) {
		message.setText(mMessage);
		message.registerEntityModifier(new MoveYModifier(0.5f, top + 30, top - 30));
		gm.engine.registerUpdateHandler(new TimerHandler(4, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                message.registerEntityModifier(new MoveYModifier(0.5f, top - 30, top + 30) {
                	@Override protected void onModifierFinished(IEntity pItem) {
                		if(action == 0) touchable = true;
                		else if(action == 1) gm.gameScene.goingToBed.finalAnimation();
                	}
                });
            }
		})); 
	}

}
