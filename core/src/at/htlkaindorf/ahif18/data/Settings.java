package at.htlkaindorf.ahif18.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

public class Settings {

    private static final String PREFERENCES_NAME = "at.htlkaindorf.muno.settings";
    private static Settings instance;

    public static synchronized Settings getInstance()
    {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    public static final String KEY_BACKGROUND_COLOR = "backgroundColor";
    private Preferences settings;

    private Settings()
    {
        settings = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public Color getBackgroundColor()
    {
        return Color.valueOf(
            settings.getString(KEY_BACKGROUND_COLOR, "#00003300")
        );
    }

    public void setBackgroundColor(String hex){
        if(!hex.startsWith("#")){
            hex = "#" + hex;
        }

        if(hex.length() == 7){
            hex += "00";
        }

        try
        {
            Color.valueOf(hex);
            settings.putString(KEY_BACKGROUND_COLOR, hex);
            settings.flush();
        }
        catch(Exception e)
        {

        }
    }

    public String getValue(String key)
    {
        return settings.getString(key);
    }
}
