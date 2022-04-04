package at.htlkaindorf.ahif18.Actors;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScrollElement extends Actor {

    private Card card;

    public ScrollElement(Card card){
        setBounds(0, 0, 100, 100);
        this.card = card;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(card.getTexture(), getX(), getY(), 100, 100);
    }
}
