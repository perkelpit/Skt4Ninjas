package com.company.skt;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.company.skt.model.Settings;

import java.util.Properties;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Settings.boot("assets/config/");
		Properties appCfg = Settings.getProperties(Settings.APP);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(appCfg.getProperty("name"));
		if(appCfg.getProperty("fullscreen").equals("true")) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else {
			config.setWindowedMode(
				Integer.parseInt(appCfg.getProperty("resolution_x")),
				Integer.parseInt(appCfg.getProperty("resolution_y")));
		}
		new Lwjgl3Application(new Skt(), config);
	}
}
