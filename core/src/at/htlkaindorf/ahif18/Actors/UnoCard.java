package at.htlkaindorf.ahif18.Actors;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UnoCard extends Actor {

    private Card card;

    public UnoCard(Card card){
        this.card = card;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawCard(batch, card, this.getX(), this.getY(), getWidth());
    }

    public void drawCard(Batch batch, Card card, float x, float y, float width)
    {
        float height = width * 3 / 2;
        batch.draw(card.getTexture(), x - width/2, y - height/2, width, height);
    }
}
