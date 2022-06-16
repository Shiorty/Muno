package at.htlkaindorf.ahif18.bl;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains various methods to load and resize fonts
 *
 * Last changed: 2022-06-16
 * @author Andreas Kurz
 */
public class FontLoader {

    //Define all available Fonts
    public enum Font{
        PIXEL("fonts/pixel/");

        public String path;

        Font(String path){
            this.path = path;
        }
    }

    //---   Loading fonts via FreeTypeFont extension which is now included              ---//
    //---   it was original removed because it lacks html compatability                 ---//
    //---   as it yields better results, it's the preferred method of loading fonts     ---//

    public static BitmapFont generateFont(Font fontType, int fontSize)
    {
        //create a FontGenerator
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontType.path + "font.ttf"));

        //Set all parameters
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        //generate the font
        BitmapFont font = fontGenerator.generateFont(parameter);

        //set the resize algorithm of the font
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    /**
     * Changes the font size of the given font
     * @param font the font to be changed
     * @param height the specified size
     */
    private static void setFontLineHeight(BitmapFont font, float height)
    {
        font.getData().setScale(height * font.getScaleY() / font.getLineHeight());
    }




    //--- Fonts loading without the FreeTypeFont extension ---//
    /**
     * Loads a font from the given path
     * @deprecated
     * @param filename is relative to the assets-directory;
     *                 should not contain an extension
     * @return the newly loaded font
     */
    @Deprecated
    private static BitmapFont loadFont(String filename)
    {
        //load font
        BitmapFont font = new BitmapFont(Gdx.files.internal(filename + ".fnt"), false);

        //set scaling algorithm
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    /**
     * <p>
     *     Generates a new font with the given fontSize.
     *     If a fontFile of the requested size isn't available it will be upscaled
     * </p>
     * @deprecated
     * @param font specifies which Font to load
     * @param fontSize specified font size
     * @return the newly created font
     */
    @Deprecated
    public static BitmapFont generateNonFreetypeFont(Font font, int fontSize)
    {
        //Find all .fnt files in a specific font directory
        FileHandle[] availableFonts = Gdx.files.internal(font.path).list((dir, name) -> name.matches("-?\\d+.fnt"));

        //Get the available fontSizes from the file names
        List<Integer> fontSizes = Arrays.stream(availableFonts)
                                    .map(FileHandle::nameWithoutExtension)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());

        //Find the best match for the requested size (is an index of the fontSizes list)
        int bestMath = IntStream.range(0, fontSizes.size())
                        .boxed()
                        .min(Comparator.comparingInt(x -> Math.abs(fontSize - fontSizes.get(x))))
                        .get();

        //get the size
        int closestSize = fontSizes.get(bestMath);

        //load the size into memory
        BitmapFont bestMatch = loadFont(font.path + closestSize);
        setFontLineHeight(bestMatch, fontSize);

        return bestMatch;
    }
}
