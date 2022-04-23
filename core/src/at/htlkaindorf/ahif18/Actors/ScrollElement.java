package at.htlkaindorf.ahif18.Actors;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScrollElement extends Actor {

    private PlayerInfo player;

    public ScrollElement(PlayerInfo play) {
        setBounds(0, 0, 100, 100);
        this.player = play;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        MunoGame.font.getData().setScale(1);
        MunoGame.font.setColor(Color.BLACK);
        MunoGame.font.draw(batch, player.getPlayerName(), getX(), getY());

    }
}
