package at.htlkaindorf.ahif18.Actors;

import at.htlkaindorf.ahif18.data.PlayerInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static at.htlkaindorf.ahif18.MunoGame.font;

public class PlayerScrollElement extends Actor {

    private PlayerInfo player;
    private ShapeRenderer shapeRenderer;

    public PlayerScrollElement(PlayerInfo play) {
//        setBounds(0, 0, 100, 100);
        this.player = play;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(Color.valueOf("E8FFFF"));

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        font.getData().setScale(0.5f);
        font.setColor(Color.BLACK);
        drawText(batch, font, player.getPlayerName(), getX() + 10, getY(), getHeight());
        drawText(batch, font, player.getCardAmount() + "", getX() + getWidth() - 50, getY(), getHeight());
    }

    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y, float height){
        float centerY = y + height/2 + font.getXHeight()/2;
        font.draw(batch, text, x, centerY);
    }
}
