package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.ui.DrawUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the cards held be the player
 *
 * <br><br>
 * Last changed: 2022-06-17
 * @author Jan Mandl; Andreas Kurz
 */
public class CardCollectionActor extends Actor {

    private static final float CARD_WIDTH = 100;
    private static final float CARD_HEIGHT = CARD_WIDTH / 2 * 3;

    /**
     * Interface that gets notified when a card ist clicked
     */
    public interface CardClickedListener{
        /**
         * Gets called when a card is clicked
         * @param collection the originator of this event
         * @param index the index of the card clicked
         */
        void cardClicked(CardCollectionActor collection, int index);
    }

    /**
     * Data Class that stores information about each card held
     */
    private static class CardInfo {
        Card card;
        Rectangle hitBox;
        boolean isFocused;

        public CardInfo(Card card, Rectangle hitbox){
            this.card = card;
            this.hitBox = hitbox;
            isFocused = false;
        }
    }

    //List of cards currently held by player
    private List<CardInfo> cards;

    @Setter
    private CardClickedListener cardClickedListener;

    //stores if the cards can be played / highlighted
    @Getter
    @Setter
    private boolean active = true;

    public CardCollectionActor(CardClickedListener listener) {
        cardClickedListener = listener;
        cards = new ArrayList<>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(!active){
            //set the batch to draw with transparency
            batch.setColor(255, 255, 255,0.5f);
        }

        //Draw cards
        for(CardInfo ca : cards) {
            DrawUtils.drawCard(batch, ca.card, ca.hitBox.x + getXOffset(), ca.hitBox.y, ca.hitBox.width);
        }

        //Draw outline for focused cards
        for(CardInfo ca : cards) {
            if(ca.isFocused) {
                DrawUtils.drawRectangleOutline(batch, Color.YELLOW, ca.hitBox.x + getXOffset(), ca.hitBox.y, ca.hitBox.width, ca.hitBox.height);
            }
        }
    }

    /**
     * Changes the cards currently displayed to a new Card List
     * @param cardList the cards to be displayed
     */
    public void setCards(List<Card> cardList) {
        cards.clear();
        for(Card c : cardList) {
            addCard(c);
        }
    }

    /**
     * Adds a single Card to the collection
     * @param c Card to be added
     */
    public void addCard(Card c) {
        //counter for going through all the cards to
        //count all the widths together
        float cardsBefore = cards.size();
        //counter for the padding
        float left_padding = 0;

        for(int i = 0;i < cardsBefore; ++i) {
            left_padding += cards.get(i).hitBox.getWidth();
        }

        CardInfo unoCard = new CardInfo(c, new Rectangle(
            getX() + left_padding,
            getY(),
            CARD_WIDTH,
            CARD_HEIGHT
        ));

        cards.add(unoCard);
    }

    /**
     * Removes a single card from the collection
     * @param cardIndex Index of the card to be removed
     */
    public void removeCard(int cardIndex)
    {
        CardInfo c = cards.remove(cardIndex);

        for(int i = cardIndex; i < cards.size(); i++)
        {
            cards.get(i).hitBox.x -= c.hitBox.getWidth();
        }
    }

    /**
     * Gets the Card stored at a specific index
     * @param cardIndex index of the card
     * @return the card identified by the index
     */
    public Card getCard(int cardIndex) {
        return cards.get(cardIndex).card;
    }

    /**
     * Get X Offset of the first card<br>
     * Is used to center the cards
     * @return the x offset of the cards
     */
    public float getXOffset(){
        return (this.getWidth() - this.cards.size() * CARD_WIDTH) / 2;
    }

    @Override
    public void act(float delta) {
        if(!active){
            return;
        }

        //Get touch coordinates
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        Vector2 touchPos = new Vector2(x, y);

        this.getStage().getViewport().unproject(touchPos);

        //Adds the offset of the first card
        touchPos.x -= getXOffset();

        //Test if any of the cards are focused
        for(CardInfo cardInfo : cards)
        {
            cardInfo.isFocused = cardInfo.hitBox.contains(touchPos.x, touchPos.y);
        }

        //Test if any card was clicked
        if(cardClickedListener != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            for(int i = 0; i < cards.size(); i++)
            {
                CardInfo cardInfo = cards.get(i);

                if(cardInfo.hitBox.contains(touchPos.x, touchPos.y))
                {
                    //System.out.println(cardInfo.card.name() + " was clicked!");
                    cardClickedListener.cardClicked(this, i);
                }
            }
        }
    }
}
