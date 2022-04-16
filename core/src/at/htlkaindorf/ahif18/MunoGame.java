package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.ui.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MunoGame extends Game {

	public static final int[] SCREEN_SIZE = new int[]{1600, 900};

	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		Texture texture = new Texture(Gdx.files.internal("fonts/pixel.png"), true); // true enables mipmaps
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
		font = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"), new TextureRegion(texture), false);

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
