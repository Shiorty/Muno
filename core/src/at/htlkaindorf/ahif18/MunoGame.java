package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.ui.Screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Contains information about the current game state
 *
 * <br><br>
 * Last changed: 2022-06-03
 * @author Andreas Kurz
 */
public class MunoGame extends Game {

	public static final int[] SCREEN_SIZE = new int[]{ 1600, 900 };

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		this.setScreen(new MainMenuScreen(this));
	}

	/**
	 * Changes the current screen and disposes the old ones
	 * @param screen the new screen
	 */
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

		if(this.getScreen() != null){
			this.getScreen().dispose();
		}
	}
}
