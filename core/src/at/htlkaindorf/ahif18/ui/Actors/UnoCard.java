package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Getter;
import lombok.Setter;

import static at.htlkaindorf.ahif18.ui.DrawUtils.drawCard;

/**
 * GUI class that is used to represent single Uno cards.
 * <br>
 * Last changed: 2022-06-16
 * @author Jan Mandl, Andreas Kurz
 */
@Getter
@Setter
public class UnoCard extends Actor {

    private static final float OPACITY_WHEN_INACTIVE = 0.5f;

    private Card card;
    private boolean active;

    public UnoCard(Card card){
        this.card = card;
        this.active = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!active){
            batch.setColor(255, 255, 255, OPACITY_WHEN_INACTIVE);
        }

        drawCard(batch, card, this.getX(), this.getY(), getWidth());
    }
}
