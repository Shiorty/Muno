package at.htlkaindorf.ahif18;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

/**
 * Main for the Desktop Version<br>
 * Launches a new MunoGame object
 *
 * <br><br>
 * Last changed: 2022-06-16
 * @author Andreas Kurz
 */
public class DesktopLauncher {

	public static void main (String[] arg)
	{
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);
//		 config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
//		 config.setWindowPosition(1700, 900);
		config.setWindowSizeLimits(160, 90, -1, -1);

		if(arg.length >= 1){
			String[] values = arg[0].split(";");
			int width = Integer.parseInt(values[0]);
			int height = Integer.parseInt(values[1]);

			config.setWindowedMode(width, height);
		}

		if(arg.length >= 2){
			String[] values = arg[1].split(";");
			int x = Integer.parseInt(values[0]);
			int y = Integer.parseInt(values[1]);

			config.setWindowPosition(x, y);
		}

		new Lwjgl3Application(new MunoGame(), config);
	}
}
