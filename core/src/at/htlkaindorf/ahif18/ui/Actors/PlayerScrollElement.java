package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import at.htlkaindorf.ahif18.bl.FontLoader;
import at.htlkaindorf.ahif18.ui.DrawUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collections;


public class PlayerScrollElement extends Group {

    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(FontLoader.generateFont(FontLoader.Font.PIXEL, 48), Color.BLACK);

    private PlayerInfo player;
    private ShapeRenderer shapeRenderer;

    private Table table;
    private Label lbPlayerName;
    private Label lbCardAmount;

    public PlayerScrollElement(PlayerInfo play) {
//        setBounds(0, 0, 100, 100);
        this.player = play;
        this.shapeRenderer = new ShapeRenderer();

        lbPlayerName = new Label("", LABEL_STYLE);
        lbCardAmount = new Label("", LABEL_STYLE);

        table = new Table();
        table.setFillParent(true);
        table.add(lbPlayerName).left().padLeft(15);
        table.add(new UnoCard(Card.CARD_BACK)).width(25).height(25 / 2 * 3).expandX().right();
        table.add(lbCardAmount).padRight(15);
        table.setDebug(false);

        this.addActor(table);
    }

    @Override
    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
        table.setDebug(enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Draw Background Shape
        DrawUtils.drawRectangle(batch, Color.valueOf("E8FFFF"), getX(), getY(), getWidth(), getHeight());

        //set label values
        lbPlayerName.setText(player.getPlayerName());
        lbCardAmount.setText(formatPlayerCardAmount(player.getCardAmount()));

        //Draws Table
        super.draw(batch, parentAlpha);
    }

    public String formatPlayerCardAmount(int cardAmount)
    {
        String result = cardAmount + "";

        result = repeatString("0", 2 - result.length()) + result;
        result = " X " + result;

        return result;
    }

    public String repeatString(String string, int count)
    {
        if(count <= 0){
            return "";
        }

        return String.join("", Collections.nCopies(count, string));
    }
}
