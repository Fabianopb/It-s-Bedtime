package objects;

import managers.GameManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Sleepy extends Sprite {
	
	protected GameManager gm = GameManager.getInstance();
	protected VertexBufferObjectManager vbom = gm.vbom;

	public TiledSprite eyes; 

	public Sleepy(float pX, float pY, ITextureRegion pTexture, VertexBufferObjectManager vbom) {
		super(pX, pY, pTexture, vbom);
		
		eyes = new TiledSprite(getWidth() / 2, getHeight() / 2 + 60, gm.eyes, vbom);
		attachChild(eyes);
		
	}

}
