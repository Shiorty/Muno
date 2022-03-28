package at.htlkaindorf.ahif18.test;

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

public class BucketGame extends ApplicationAdapter {

    private OrthographicCamera camera;

    private Rectangle bucket;
    private ArrayList<Rectangle> raindrops;

    private long lastDropTime;

    SpriteBatch batch;
    Texture img;

    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new ArrayList<>();

        batch = new SpriteBatch();
        img = new Texture("hu.gif");
    }

    @Override
    public void render () {
        ScreenUtils.clear(0, 0, 0, 1);

        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;

        if(TimeUtils.nanoTime() - lastDropTime > 100000000) spawnRaindrop();

        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if(raindrop.y + 64 < 0){
                iter.remove();
            }

            if(raindrop.overlaps(bucket)){
                Gdx.app.exit();
            }
        }

        batch.begin();
        batch.draw(img, bucket.x, bucket.y, bucket.width, bucket.height);
        for(Rectangle raindrop : raindrops){
            batch.draw(img, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
        }
        batch.end();

        camera.update();
    }

    public void spawnRaindrop()
    {
        lastDropTime = TimeUtils.nanoTime();

        Random r = new Random();
        raindrops.add(new Rectangle(
                r.nextInt(750),
                480 + 50,
                50,
                50
        ));
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}