package at.htlkaindorf.ahif18.ui;

import at.htlkaindorf.ahif18.data.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Contains various methods for drawing Elements on Screen
 *
 * <br><br>
 * Last changed: 2022-06-17
 * @author Andreas Kurz
 */
public class DrawUtils
{
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Draws a rectangular outline with a line width of 1<br>
     * Is the same as calling drawRectangleOutline(batch, color, x, y, width, height, 1);
     * @param batch the batch to be drawn to
     * @param color the color of the outline
     * @param x x position of the bottom left corner
     * @param y y position of the bottom left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     */
    public static void drawRectangleOutline(Batch batch, Color color, float x, float y, float width, float height)
    {
        drawRectangleOutline(batch, color, x, y, width, height, 1);
    }

    /**
     * Draws a rectangular outline with a specified line width
     * @param batch the batch to be drawn to
     * @param color the color of the outline
     * @param x x position of the bottom left corner
     * @param y y position of the bottom left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param thickness thickness of the outline
     */
    public static void drawRectangleOutline(Batch batch, Color color, float x, float y, float width, float height, int thickness)
    {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(thickness);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    /**
     * Draws a Rectangle filled with color
     * @param batch the batch to be drawn to
     * @param color the color of the rectangle
     * @param x x position of the bottom left corner
     * @param y y position of the bottom left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     */
    public static void drawRectangle(Batch batch, Color color, float x, float y, float width, float height)
    {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
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
    public static void drawCardCentered(Batch batch, Card card, float x, float y, float width)
    {
        float height = width * 3 / 2;
        batch.draw(card.getTexture(), x - width/2, y - height/2, width, height);
    }

    /**
     * Draws a single card at a specific location
     * @param batch batch which is used to draw the card
     * @param card card to be drawn
     * @param rectangle position and with of the card
     */
    public static void drawCard(Batch batch, Card card, Rectangle rectangle)
    {
        batch.draw(card.getTexture(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    /**
     * Draws a single card at a specific location
     * The width gets calculated according to the card aspect ratio
     * @param batch batch which is used to draw the card
     * @param card card to be drawn
     * @param x x position of the leftmost point of the card
     * @param y y position of the bottom of the card
     * @param width width of the card
     */
    public static void drawCard(Batch batch, Card card, float x, float y, float width)
    {
        drawCard(batch, card, new Rectangle(
           x,
           y,
           width,
           width / 2 * 3
        ));
    }
}
