package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private MunoGame game;

    private OrthographicCamera camera;

    public GameScreen(MunoGame game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color());

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

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            game.setScreen(new MainMenueScreen(game));
            this.dispose();
        }
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
