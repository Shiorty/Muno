package at.htlkaindorf.ahif18.data;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Saves all the Card types and the corresponding<br>
 * paths to their textures.
 * <br><br>
 * Last changed: 2022-06-03
 * @author Jan Mandl, Kurz Andreas
 */
public enum Card {

    CARD_BACK("card_back.png", CardGroup.values()),
    PLUS_4("card_back.png", CardGroup.values()),

    R0("red_0.png", CardGroup.Red, CardGroup.N0),
    R1("red_1.png", CardGroup.Red, CardGroup.N1),
    R2("red_2.png", CardGroup.Red, CardGroup.N2),
    R3("red_3.png", CardGroup.Red, CardGroup.N3),
    R4("red_4.png", CardGroup.Red, CardGroup.N4),
    R5("red_5.png", CardGroup.Red, CardGroup.N5),
    R6("red_6.png", CardGroup.Red, CardGroup.N6),
    R7("red_7.png", CardGroup.Red, CardGroup.N7),
    R8("red_8.png", CardGroup.Red, CardGroup.N8),
    R9("red_9.png", CardGroup.Red, CardGroup.N9),
    B0("blue_0.png", CardGroup.Blue, CardGroup.N0),
    B1("blue_1.png", CardGroup.Blue, CardGroup.N1),
    B2("blue_2.png", CardGroup.Blue, CardGroup.N2),
    B3("blue_3.png", CardGroup.Blue, CardGroup.N3),
    B4("blue_4.png", CardGroup.Blue, CardGroup.N4),
    B5("blue_5.png", CardGroup.Blue, CardGroup.N5),
    B6("blue_6.png", CardGroup.Blue, CardGroup.N6),
    B7("blue_7.png", CardGroup.Blue, CardGroup.N7),
    B8("blue_8.png", CardGroup.Blue, CardGroup.N8),
    B9("blue_9.png", CardGroup.Blue, CardGroup.N9),
    G0("green_0.png", CardGroup.Green, CardGroup.N0),
    G1("green_1.png", CardGroup.Green, CardGroup.N1),
    G2("green_2.png", CardGroup.Green, CardGroup.N2),
    G3("green_3.png", CardGroup.Green, CardGroup.N3),
    G4("green_4.png", CardGroup.Green, CardGroup.N4),
    G5("green_5.png", CardGroup.Green, CardGroup.N5),
    G6("green_6.png", CardGroup.Green, CardGroup.N6),
    G7("green_7.png", CardGroup.Green, CardGroup.N7),
    G8("green_8.png", CardGroup.Green, CardGroup.N8),
    G9("green_9.png", CardGroup.Green, CardGroup.N9),
    Y0("yellow_0.png", CardGroup.Yellow, CardGroup.N0),
    Y1("yellow_1.png", CardGroup.Yellow, CardGroup.N1),
    Y2("yellow_2.png", CardGroup.Yellow, CardGroup.N2),
    Y3("yellow_3.png", CardGroup.Yellow, CardGroup.N3),
    Y4("yellow_4.png", CardGroup.Yellow, CardGroup.N4),
    Y5("yellow_5.png", CardGroup.Yellow, CardGroup.N5),
    Y6("yellow_6.png", CardGroup.Yellow, CardGroup.N6),
    Y7("yellow_7.png", CardGroup.Yellow, CardGroup.N7),
    Y8("yellow_8.png", CardGroup.Yellow, CardGroup.N8),
    Y9("yellow_9.png", CardGroup.Yellow, CardGroup.N9);

    /**
     * The folder the files are in.
     */
    private String folder = "cards/";
    /**
     * The texture libgdx needs to display the cards.
     */
    private Texture texture;
    /**
     * The groups the card is in.
     */
    private CardGroup[] groups;

    Card(String texture, CardGroup... cardGroups) {
        this.texture = new Texture(folder + texture);
        this.groups = cardGroups;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean hasEqualGroup(Card other) {
        if(other == null || this.groups == null) {
            return false;
        }

        for(CardGroup cg1 : other.groups) {
            for(CardGroup cg2 : this.groups) {
                if(cg1 == cg2) {
                    return true;
                }
            }
        }

        return false;
    }

    //TODO make that Card Back cant be gg etted
    public static Card randomCard(){
        Random r = new Random();
        return values()[r.nextInt(values().length)];
    }
}
