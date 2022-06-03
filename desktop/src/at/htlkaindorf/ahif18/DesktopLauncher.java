package at.htlkaindorf.ahif18;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(20);
//		 config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		// config.setWindowPosition(1700, 900);
		config.setWindowSizeLimits(160, 90, -1, -1);

		new Lwjgl3Application(new MunoGame(), config);
	}
}
