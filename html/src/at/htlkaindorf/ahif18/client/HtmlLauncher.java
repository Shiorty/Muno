package at.htlkaindorf.ahif18.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import at.htlkaindorf.ahif18.MunoGame;

/**
 * Specifies how the game will be launched as a Website<br>
 * HTML support was dropped due to it not being compatible with direct network access :(
 *
 * <br><br>
 * Last changed: 2022-03-21
 * @author Andreas Kurz
 */
@Deprecated
public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MunoGame();
        }
}