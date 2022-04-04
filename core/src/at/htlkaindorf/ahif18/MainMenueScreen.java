package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MainMenueScreen implements Screen {

    final MunoGame game;

    private Stage stage;

    public MainMenueScreen(MunoGame game)
    {
        this.game = game;

        Skin skin = new Skin();
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        Label title = new Label("Muno", skin);

        stage = new Stage(new ExtendViewport(500, 500));
        stage.addActor(title);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        ScreenUtils.clear(Color.WHITE);
        stage.act(delta);
        stage.draw();

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
