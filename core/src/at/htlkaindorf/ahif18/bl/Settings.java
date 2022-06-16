package at.htlkaindorf.ahif18.bl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;

/**
 * Used to manage and store all user preferences
 *
 * Last changed: 2022-06-16
 * @author Andreas Kurz
 */
public class Settings {

    private static final String PREFERENCES_NAME = "at.htlkaindorf.muno.settings";

    /**
     * Enum containing all Keys as well as their respective default values
     */
    @Getter
    @AllArgsConstructor
    private enum Key {
        BACKGROUND_COLOR("#8fcdc900");

        String defaultValue;
    }

    private static Settings instance;
    public static synchronized Settings getInstance()
    {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    private Preferences settings;

    private Settings()
    {
        settings = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    /**
     * Resets the preference to default values
     */
    public void restoreDefaults()
    {
        for(Key key : Key.values())
        {
            settings.putString(key.name(), key.getDefaultValue());
        }
        settings.flush();
    }

    /**
     * Writes a value to disk
     * @param key key of the value to be written
     * @param value the value
     */
    public void saveValue(Key key, String value)
    {
        settings.putString(key.name(), value);
        settings.flush();
    }

    /**
     * Returns the value currently associated with the Key<br>
     * It will return the Keys default value if no preference is found
     * @param key the specified key
     * @return the value associated with the key
     */
    public String getKeyValue(Key key){
        return settings.getString(key.name(), key.getDefaultValue());
    }

    /**
     * Gets the current background color
     * @return the background color set
     */
    public Color getBackgroundColor()
    {
        return Color.valueOf(
            getKeyValue(Key.BACKGROUND_COLOR)
        );
    }

    /**
     * Sets the background color<br>
     * Doesn't save invalid values
     * @param hex hex string representing the color;<br>
     *            may start with a #<br>
     *            max length is 9
     */
    public void setBackgroundColor(String hex)
    {
        if(!hex.startsWith("#"))
            hex = "#" + hex;

        if(hex.length() < 9)
            hex = hex + String.join("", Collections.nCopies(9 - hex.length(), "0"));

        try
        {
            //try to convert the hex value into a color
            //throws exception if the string is invalid
            Color.valueOf(hex);

            //save the new color value
            saveValue(Key.BACKGROUND_COLOR, hex);
        }
        catch(Exception e)
        {
            //if the string value is invalid do nothing
            e.printStackTrace();
        }
    }
}
