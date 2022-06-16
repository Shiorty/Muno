package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.bl.FontLoader;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import at.htlkaindorf.ahif18.ui.DrawUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player in the scroll area
 *
 * Last changed: 2022-06-16
 * @author Jan Mandl, Andreas Kurz
 */
public class PlayerScrollElement extends Group {

    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(FontLoader.generateFont(FontLoader.Font.PIXEL, 48), Color.BLACK);

    private final Table table;
    private final Label lbPlayerName;
    private final Label lbCardAmount;

    @Setter
    @Getter
    private PlayerInfo player;

    public PlayerScrollElement(PlayerInfo play) {
//        setBounds(0, 0, 100, 100);
        this.player = play;

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
        lbCardAmount.setText(String.format(" X %02d", player.getCardAmount()));

        //Draws Table
        super.draw(batch, parentAlpha);
    }
}
