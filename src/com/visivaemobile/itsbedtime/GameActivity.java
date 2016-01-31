package com.visivaemobile.itsbedtime;

import java.io.IOException;

import managers.GameManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {
	
	private Camera camera;
	private CropResolutionPolicy crp;
	private GameManager gm;
	public int CAMERA_WIDTH = 800;
	public int CAMERA_HEIGHT = 480;
	
	public final float centerX = CAMERA_WIDTH / 2;
    public final float centerY = CAMERA_HEIGHT / 2;
    public float left;
    public float bottom;
    public float right;
    public float top;
    
    public AlertDialog.Builder exitGame;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        exitGame = new AlertDialog.Builder(this);
		exitGame.setTitle(R.string.exit_title); 
		exitGame.setMessage(R.string.exit_message); 
		exitGame.setPositiveButton(R.string.exit_yes, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface arg0, int arg1) { System.exit(0); } 
		});  
		exitGame.setNegativeButton(R.string.exit_no, new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface arg0, int arg1) {} 
		});		
		exitGame.create();
    }

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		crp = new CropResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
	    
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, crp, camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    
	    return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
		GameManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		gm = GameManager.getInstance();
		GameManager.getInstance().createSplashResources(pOnCreateResourcesCallback);		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
		left = crp.getLeft();
	    bottom = crp.getBottom();
	    right = crp.getRight();
	    top = crp.getTop();
		gm.createSplashScene(pOnCreateSceneCallback);		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		gm.createGameScene();
		this.mEngine.registerUpdateHandler(new TimerHandler(5.0f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                mEngine.setScene(gm.gameScene);
                //Enable elements after showing the game scene
                gm.gameScene.gameHud.setVisible(true);
                gm.gameScene.gameHud.touchable = true;
                gm.gameScene.selectorsEnabled = true;
                if(gm.music.isPlaying()) gm.music.stop();
                gm.music.play();
            }
		}));		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
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
	    	else if (gm.gameScene.selectorsEnabled || gm.gameScene.goingToBed.activityEnabled){
	    		exitGame.show();
	    	}
	    }
	    return false; 
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();	        
	    if (this.isGameLoaded()) {
	        System.exit(0);
	    }
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	}

	
}
