package com.company.skt;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.company.skt.model.Settings;

import java.awt.*;
import java.util.Properties;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Settings.boot("assets/config/");
		Properties appCfg = Settings.getProperties(Settings.APP);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(appCfg.getProperty("name"));
		config.setResizable(false);
		boolean debug = false;
		if(arg.length > 0) {
			if(arg[0].equals("debug")) {
				debug = true;
			}
		}
		if(debug) {
			config.setWindowedMode(1280, 720);
			config.setWindowPosition(485, (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - 360);
		} else {
			if(appCfg.getProperty("fullscreen").equals("true")) {
				config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
			} else {
				config.setWindowedMode(
					Integer.parseInt(appCfg.getProperty("resolution_x")),
					Integer.parseInt(appCfg.getProperty("resolution_y")));
			}
		}
		new Lwjgl3Application(new Skt(debug), config);
	}
}
