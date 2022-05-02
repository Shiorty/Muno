package at.htlkaindorf.ahif18.ui.Screens;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class TestScreen implements Screen {

    private static final float CAMERA_SCALE = 1.5f;

    private MunoGame game;

    private OrthographicCamera camera;

    public TestScreen(MunoGame game){
        this.game = game;
        camera = new OrthographicCamera(1920 * CAMERA_SCALE, 1080 * CAMERA_SCALE);
        camera.setToOrtho(false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color());

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        int x = 0;
        int y = 0;
        int width = MunoGame.SCREEN_SIZE[0] / 9;
        int height = MunoGame.SCREEN_SIZE[1] / 4;
        for(Card card : Card.values()){
            game.batch.draw(card.getTexture(), x, y, width, height);

            x+= width;
            if(x >= MunoGame.SCREEN_SIZE[0]){
                y += height;
                x = 0;
            }
        }

        game.batch.draw(Card.BLUE_0.getTexture(), 0, 0);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }

        moveCamera();
    }

    public void moveCamera(){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.position.y+= 5;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.position.x-= 5;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.position.y-= 5;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.position.x+= 5;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(-1, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.rotate(1, 0, 0, 1);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.zoom += 0.01;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.zoom -= 0.01;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.position.x = width/2f * CAMERA_SCALE;
        camera.position.y = height/2f * CAMERA_SCALE;

        camera.viewportHeight = height * CAMERA_SCALE;
        camera.viewportWidth = width * CAMERA_SCALE;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
