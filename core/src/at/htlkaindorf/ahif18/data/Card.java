package at.htlkaindorf.ahif18.data;

import com.badlogic.gdx.graphics.Texture;

public enum Card {

    CARD_BACK("cards/card_back.png"),

    R0("cards/red_0.png"),
    R1("cards/red_1.png"),
    R2("cards/red_2.png"),
    R3("cards/red_3.png"),
    R4("cards/red_4.png"),
    R5("cards/red_5.png"),
    R6("cards/red_6.png"),
    R7("cards/red_7.png"),
    R8("cards/red_8.png"),
    R9("cards/red_9.png"),
    B0("cards/blue_0.png"),
    B1("cards/blue_1.png"),
    B2("cards/blue_2.png"),
    B3("cards/blue_3.png"),
    B4("cards/blue_4.png"),
    B5("cards/blue_5.png"),
    B6("cards/blue_6.png"),
    B7("cards/blue_7.png"),
    B8("cards/blue_8.png"),
    B9("cards/blue_9.png"),
    G0("cards/green_0.png"),
    G1("cards/green_1.png"),
    G2("cards/green_2.png"),
    G3("cards/green_3.png"),
    G4("cards/green_4.png"),
    G5("cards/green_5.png"),
    G6("cards/green_6.png"),
    G7("cards/green_7.png"),
    G8("cards/green_8.png"),
    G9("cards/green_9.png"),
    Y0("cards/yellow_0.png"),
    Y1("cards/yellow_1.png"),
    Y2("cards/yellow_2.png"),
    Y3("cards/yellow_3.png"),
    Y4("cards/yellow_4.png"),
    Y5("cards/yellow_5.png"),
    Y6("cards/yellow_6.png"),
    Y7("cards/yellow_7.png"),
    Y8("cards/yellow_8.png"),
    Y9("cards/yellow_9.png");

    private Texture texture;

    Card(String texture)
    {
        this.texture = new Texture(texture);
    }

    public Texture getTexture() {
        return texture;
    }
}
