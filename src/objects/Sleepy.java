package objects;

import managers.GameManager;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Sleepy extends Sprite {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;

	public AnimatedSprite eyes; 

	public Sleepy(float pX, float pY, ITextureRegion pTexture, VertexBufferObjectManager vbom) {
		super(pX, pY, pTexture, vbom);
		
		registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(3, 1, 1.05f, 1, 1.05f),	new ScaleModifier(3, 1.05f, 1, 1.05f, 1))));
		
		eyes = new AnimatedSprite(getWidth() / 2, getHeight() / 2 + 60, gm.eyes, vbom);
		eyes.animate(new long[] { 5000, 100 }, 0, 1, true);
		attachChild(eyes);
		
	}
	
	public void closeEyes() {
		eyes.stopAnimation(1);
	}

}
