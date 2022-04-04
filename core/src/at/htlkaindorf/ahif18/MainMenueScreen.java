package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.test.BucketGameScreen;
import at.htlkaindorf.ahif18.test.TolleCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenueScreen implements Screen {

    final MunoGame game;

    OrthographicCamera camera;

    public MainMenueScreen(MunoGame game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1]);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        game.font.getData().setScale(2);
        game.font.draw(game.batch, "Muno", 0, MunoGame.SCREEN_SIZE[1] - 100, MunoGame.SCREEN_SIZE[0], Align.center, false);
        game.font.getData().setScale(1);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);


        int centerX = MunoGame.SCREEN_SIZE[0] / 2;
        int centerY = MunoGame.SCREEN_SIZE[1] / 2;

        drawCard(Card.YELLOW_1, centerX - 125 - 150, centerY, 125);
        drawCard(Card.YELLOW_9, centerX + 125 + 150, centerY, 125);
        drawCard(Card.GREEN_4, centerX - 150, centerY, 190);
        drawCard(Card.GREEN_7, centerX + 150, centerY, 190);
        drawCard(Card.RED_0, centerX, centerY, 250);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    public void drawCard(Card card, int x, int y, int width)
    {
        int height = width * 3 / 2;
        game.batch.draw(card.getTexture(), x - width/2, y - height/2, width, height);
    }

    @Override
    public void resize(int width, int height) {

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
