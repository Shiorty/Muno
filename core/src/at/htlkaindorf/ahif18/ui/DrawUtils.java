package at.htlkaindorf.ahif18.ui;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Contains various methods for drawing Elements on Screen
 *
 * Last changed: 2022.05.02
 * @author Andreas Kurz
 */
public class DrawUtils
{
    /**
     * Draws a single card, which is centered
     * The center point of the card is located at x,y
     * @param batch batch which is used to draw the card
     * @param card card to be drawn
     * @param x x-position of the center point
     * @param y y-position of the center point
     * @param width the width of the card
     */
    public static void drawCard(Batch batch, Card card, float x, float y, float width)
    {
        float height = width * 3 / 2;
        batch.draw(card.getTexture(), x - width/2, y - height/2, width, height);
    }
}
