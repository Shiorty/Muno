package at.htlkaindorf.ahif18.ui;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Displays multiple cards<br>
 * The number of cards should never be even!
 */
public class CardActor extends Actor {

    /**
     * Factor which determines the size of the cards
     */
    private static final double CARD_SCALE_FACTOR = 0.75;
    /**
     * Determines the amount of randomly generated cards<br>
     * number of cards should always be odd
     */
    private static final int NUMBER_OF_CARDS = 7;

    private Card[] cards;

    /**
     * Initialises the card array with random Cards<br>
     * The amount of cards is equal to the NUMBER_OF_CARDS constant
     */
    public CardActor()
    {
        //set default width
        super.setBounds(0, 0, 500, 500);

        Random rand = new Random();

        cards = new Card[NUMBER_OF_CARDS];
        for(int i = 0; i < NUMBER_OF_CARDS; i++)
        {
            cards[i] = Card.values()[rand.nextInt(Card.values().length)];
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = this.getX();
        float y = this.getY();
        float centerX = x + this.getWidth() / 2;
        float centerY = y + this.getHeight() / 2;

        //the width of the center card
        double widthOfCenterCard = (this.getHeight() / 1.5);

        //calculates the width of the leftmost card
        double currentWidth = (widthOfCenterCard * Math.pow(CARD_SCALE_FACTOR, (cards.length - 1) >> 1));

        //calculates the distance of the leftmost card in relation to the screen center
        double currentDistance = 0;
        for(int i = 0; i < cards.length / 2; i++){
            currentDistance += widthOfCenterCard * Math.pow(CARD_SCALE_FACTOR, i + 1);
        }

        //draws all of the cards except the center one
        for(int i = 0; i < cards.length - 1; i += 2)
        {
            drawCard(batch, cards[i], (float) (centerX - currentDistance), centerY, (float) currentWidth);
            drawCard(batch, cards[i+1], (float) (centerX + currentDistance), centerY, (float) currentWidth);

            //set the distance and width of the next card pair
            currentDistance -= currentWidth;
            currentWidth /= CARD_SCALE_FACTOR;
        }
        //draws the center card
        drawCard(batch, cards[cards.length - 1], centerX, centerY, (int) widthOfCenterCard);
    }


    /**
     * Draws a single card, which is centered
     * The center point of the card is located at x,y
     * @param batch batch which is used to draw the card
     * @param card card to be drawn
     * @param x x-position of the center point
     * @param y y-position of the center point
     * @param width the width of the card
     */
    public void drawCard(Batch batch, Card card, float x, float y, float width)
    {
        float height = width * 3 / 2;
        batch.draw(card.getTexture(), x - width/2, y - height/2, width, height);
    }


}
