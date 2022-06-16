package at.htlkaindorf.ahif18.ui.Actors;

import at.htlkaindorf.ahif18.ui.DrawUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class JoinForm extends Group {

    public interface FormSubmitListener{
        void onSubmit(String IP);
    }

    public interface FormClosedListener{
        void onClose();
    }

    private static Color BACKGROUND_COLOR = Color.CORAL;

    private Table mainTable;
    private TextField tfIPAddress;
    private TextButton btnOk;
    private TextButton btnClose;

    public JoinForm(Skin skin)
    {
        mainTable = new Table();
        mainTable.setFillParent(true);

        tfIPAddress = new TextField("", skin);
        tfIPAddress.setMessageText("IP-Address");
        btnClose = new TextButton("X", skin, "noHover");
        btnOk = new TextButton("Beitreten", skin);

        mainTable.columnDefaults(0).pad(25).padTop(0).expandX();
        mainTable.add(btnClose).align(Align.right);
        mainTable.row();
        mainTable.add(tfIPAddress).fillX().align(Align.left);
        mainTable.row();
        mainTable.add(btnOk).height(25).expandY().align(Align.bottomRight);

        this.setColor(BACKGROUND_COLOR);
        this.addActor(mainTable);
    }

    @Override
    public void setDebug(boolean enabled) {
        mainTable.setDebug(enabled);
    }

    @Override
    public void setColor(Color color) {
        //the transparency of the color is ignored
        super.setColor(color.r, color.g, color.b, 1);
    }

    public void setFormSubmitListener(FormSubmitListener listener){
        btnOk.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onSubmit(tfIPAddress.getText());
            }
        });
    }

    public void setOnCloseListener(FormClosedListener listener){
        btnClose.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onClose();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        DrawUtils.drawRectangle(batch, batch.getColor(), getX(), getY(), getWidth(), getHeight());

        super.draw(batch, parentAlpha);
    }
}
