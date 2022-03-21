package at.htlkaindorf.ahif18;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import at.htlkaindorf.ahif18.MunoGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("MUNO");
		config.setForegroundFPS(60);
		config.setWindowedMode(800, 480);
		new Lwjgl3Application(new MunoGame(), config);
	}
}
