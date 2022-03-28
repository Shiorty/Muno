package at.htlkaindorf.ahif18.data;

import com.badlogic.gdx.graphics.Texture;

public enum Card {

    RED_0("cards/red_0.png"),
    RED_1("cards/red_1.png"),
    RED_2("cards/red_2.png"),
    RED_3("cards/red_3.png"),
    RED_4("cards/red_4.png"),
    RED_5("cards/red_5.png"),
    RED_6("cards/red_6.png"),
    RED_7("cards/red_7.png"),
    RED_8("cards/red_8.png"),
    RED_9("cards/red_9.png"),
    BLUE_0("cards/blue_0.png"),
    BLUE_1("cards/blue_1.png"),
    BLUE_2("cards/blue_2.png"),
    BLUE_3("cards/blue_3.png"),
    BLUE_4("cards/blue_4.png"),
    BLUE_5("cards/blue_5.png"),
    BLUE_6("cards/blue_6.png"),
    BLUE_7("cards/blue_7.png"),
    BLUE_8("cards/blue_8.png"),
    BLUE_9("cards/blue_9.png"),
    GREEN_0("cards/green_0.png"),
    GREEN_1("cards/green_1.png"),
    GREEN_2("cards/green_2.png"),
    GREEN_3("cards/green_3.png"),
    GREEN_4("cards/green_4.png"),
    GREEN_5("cards/green_5.png"),
    GREEN_6("cards/green_6.png"),
    GREEN_7("cards/green_7.png"),
    GREEN_8("cards/green_8.png"),
    GREEN_9("cards/green_9.png"),
    YELLOW_0("cards/yellow_0.png"),
    YELLOW_1("cards/yellow_1.png"),
    YELLOW_2("cards/yellow_2.png"),
    YELLOW_3("cards/yellow_3.png"),
    YELLOW_4("cards/yellow_4.png"),
    YELLOW_5("cards/yellow_5.png"),
    YELLOW_6("cards/yellow_6.png"),
    YELLOW_7("cards/yellow_7.png"),
    YELLOW_8("cards/yellow_8.png"),
    YELLOW_9("cards/yellow_9.png");

    private Texture texture;

    Card(String texture)
    {
        this.texture = new Texture(texture);
    }

    public Texture getTexture() {
        return texture;
    }
}
