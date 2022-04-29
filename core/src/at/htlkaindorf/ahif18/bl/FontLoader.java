package at.htlkaindorf.ahif18.bl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FontLoader {

/*
    Loading fonts via FreeTypeFont extension which is no longer included

    private static FreeTypeFontGenerator fontGenerator;

    public static BitmapFont generateFreetypeFont(int fontSize, Color color)
    {
        if(fontGenerator == null){
            fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        }

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        return fontGenerator.generateFont(parameter);
    }
*/

    public enum Font{
        PIXEL("fonts/pixel/");

        public String path;

        Font(String path){
            this.path = path;
        }
    }

    public static void setFontLineHeight(BitmapFont font, float height)
    {
        font.getData().setScale(height * font.getScaleY() / font.getLineHeight());
    }

    private static BitmapFont loadFont(String filename)
    {
        //load font
        BitmapFont font = new BitmapFont(Gdx.files.internal(filename + ".fnt"), false);

        //set scaling algorithm
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    public static BitmapFont generateFont(Font font, int fontSize)
    {
        if(Gdx.app.getType() == Application.ApplicationType.WebGL){
            BitmapFont bitmapFont = loadFont(font.path + "html");
            setFontLineHeight(bitmapFont, fontSize);
            return bitmapFont;
        }

        FileHandle[] availableFonts = Gdx.files.internal(font.path).list((dir, name) -> name.matches("-?\\d+.fnt"));

        List<Integer> fontSizes = Arrays.stream(availableFonts)
                                    .map(FileHandle::nameWithoutExtension)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());

        int closestSize = 0;
        int closestDistance = Integer.MAX_VALUE;
        int currentDistance;
        for(int size : fontSizes)
        {
            currentDistance = Math.abs(fontSize - size);

            if(currentDistance < closestDistance)
            {
                closestDistance = currentDistance;
                closestSize = size;
            }
        }

        System.err.println("Loading font: " + font.path + closestSize + " for requested size: " + fontSize);

        BitmapFont bestMatch = loadFont(font.path + closestSize);
        setFontLineHeight(bestMatch, fontSize);

        return bestMatch;
    }

}
