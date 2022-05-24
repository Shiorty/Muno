package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static at.htlkaindorf.ahif18.ui.DrawUtils.*;

public class UnoCard extends Actor {

    private Card card;

    public UnoCard(Card card){
        this.card = card;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawCard(batch, card, this.getX(), this.getY(), getWidth());
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
