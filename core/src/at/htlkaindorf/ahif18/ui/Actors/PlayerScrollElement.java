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
 * Last changed: 2022-06-17
 * @author Jan Mandl, Andreas Kurz
 */
public class PlayerScrollElement extends Group {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.valueOf("E8FFFF");
    private static final Color LOCAL_PLAYER_COLOR = Color.valueOf("A6F6F1");

    private static final Color CURRENT_PLAYER_OUTLINE_COLOR = Color.valueOf("41AEA9");

    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(FontLoader.generateFont(FontLoader.Font.PIXEL, 48), Color.BLACK);

    private final Table table;
    private final Label lbPlayerName;
    private final Label lbCardAmount;

    @Setter
    @Getter
    private boolean isCurrentPlayer = false;

    @Setter
    @Getter
    private boolean isLocalPlayer = false;

    @Setter
    @Getter
    private PlayerInfo player;

    public PlayerScrollElement(PlayerInfo play) {
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

    public Color getCurrentBackgroundColor(){
        return isLocalPlayer ? LOCAL_PLAYER_COLOR : DEFAULT_BACKGROUND_COLOR;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Draw Background Shape
        DrawUtils.drawRectangle(batch, getCurrentBackgroundColor(), getX(), getY(), getWidth(), getHeight());

        if(isCurrentPlayer){
            DrawUtils.drawRectangleOutline(batch, CURRENT_PLAYER_OUTLINE_COLOR, getX(), getY(), getWidth(), getHeight(), 4);
        }

        //set label values
        lbPlayerName.setText(player.getPlayerName());
        lbCardAmount.setText(String.format(" X %02d", player.getCardAmount()));

        //Draws Table
        super.draw(batch, parentAlpha);
    }
}
