package at.htlkaindorf.ahif18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.ScreenUtils;

public class MunoGame extends Game {

	public static final int[] SCREEN_SIZE = new int[]{ 1920, 1080 };

	public SpriteBatch batch;
	public static BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		Texture texture = new Texture(Gdx.files.internal("fonts/pixel.png"), true); // true enables mipmaps
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image

		if (font == null) {
			font = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"), new TextureRegion(texture), false);
		}

		this.setScreen(new MainMenueScreen(this));
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
