package at.htlkaindorf.ahif18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MunoGame extends ApplicationAdapter {

	private OrthographicCamera camera;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {

	}

	@Override
	public void render () {

	}

	public void spawnRaindrop()	
	{

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
