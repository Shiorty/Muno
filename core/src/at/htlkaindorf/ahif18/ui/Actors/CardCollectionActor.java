package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.ui.DrawUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Mandl; Andreas Kurz
 */
public class CardCollectionActor extends Actor {

    private class CardInfo {
        Card card;
        Rectangle hitBox;
        boolean isFocused;

        public CardInfo(Card card, Rectangle hitbox){
            this.card = card;
            this.hitBox = hitbox;
            isFocused = false;
        }
    }

    private final float CARD_WIDTH = 100;
    private final float CARD_HEIGHT = CARD_WIDTH / 2 * 3;
    private List<CardInfo> cards;
    private int playedCard;

    public CardCollectionActor() {
        cards = new ArrayList<>();
        playedCard = -1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(CardInfo ca : cards) {

            DrawUtils.drawCard(batch, ca.card, ca.hitBox.x + getXOffset(), ca.hitBox.y, ca.hitBox.width);

            if(ca.isFocused)
            {
                DrawUtils.drawRectangleOutline(batch, Color.YELLOW, ca.hitBox.x + getXOffset(), ca.hitBox.y, ca.hitBox.width, ca.hitBox.height);
            }
        }
    }

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

    public Card removeCard(int cardIndex)
    {
        CardInfo c = cards.remove(cardIndex);

        for(int i = cardIndex; i < cards.size(); i++)
        {
            cards.get(i).hitBox.x -= c.hitBox.getWidth();
        }

        return c.card;
    }

    public int retrievePlayedCard() {
        int playedCard = this.playedCard;
        this.playedCard = -1;
        return playedCard;
    }

    //Get X Offset of the first card
    //Is used to center the cards
    public float getXOffset(){
        return (this.getWidth() - this.cards.size() * CARD_WIDTH) / 2;
    }

    @Override
    public void act(float delta) {

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
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            if(playedCard != -1)
            {
                System.err.println("Card has not been removed!");
                return;
            }

            for(int i = 0; i < cards.size(); i++)
            {
                CardInfo cardInfo = cards.get(i);

                if(cardInfo.hitBox.contains(touchPos.x, touchPos.y))
                {
                    System.out.println(cardInfo.card.name());
                    playedCard = i;
                }
            }
        }
    }
}
