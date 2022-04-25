package at.htlkaindorf.ahif18.Actors;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.ui.MainMenuCardsActor;
import com.badlogic.gdx.graphics.g2d.Batch;

public class CardActor extends MainMenuCardsActor {

    private Card card;
    private float xPos, yPos;
    private float width;

    public CardActor(float x, float y, float width) {
        this.xPos = x;
        this.yPos = y;
        this.width = width;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.drawCard(batch, card, xPos, yPos, width);
    }

    public float getWidth() {
        return width;
    }

    public void setCard(Card c) {
        this.card = c;
    }

    public void setxPos(float x) {
        this.xPos = x;
    }

    public void setyPos(float y) {
        this.yPos = y;
    }
}
