package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.bl.FontLoader;
import at.htlkaindorf.ahif18.ui.Screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MunoGame extends Game {

	public static final int[] SCREEN_SIZE = new int[]{ 1600, 900 };

	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = FontLoader.generateFont(FontLoader.Font.PIXEL, 64);

		this.setScreen(new MainMenuScreen(this));
	}


	public void changeScreen(Screen screen){
		this.getScreen().dispose();
		this.setScreen(screen);
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
