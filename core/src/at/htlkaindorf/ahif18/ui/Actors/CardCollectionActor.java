package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class CardCollectionActor extends Actor {

    private final float CARD_WIDTH = 100;
    private final float CARD_PADDING_LEFT = 100;
    private final float CARD_PADDING_BOTTOM = 90;
    private List<UnoCard> cards;

    public CardCollectionActor() {
        cards = new ArrayList<>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(UnoCard ca : cards) {
            ca.draw(batch, parentAlpha);
        }
    }

    public void addCard(Card c) {
        //counter for going through all the cards to
        //count all the widths together
        float cardsBefore = cards.size();
        //counter for the padding
        float left_padding = 0;

        for(int i = 0;i < cardsBefore; ++i) {
            left_padding += cards.get(i).getWidth();
        }

        UnoCard unoCard = new UnoCard(c);
        unoCard.setBounds(
            left_padding + CARD_PADDING_LEFT,
            CARD_PADDING_BOTTOM,
            CARD_WIDTH,
            -1
        );

        cards.add(unoCard);
    }
}
