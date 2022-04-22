package com.company.skt;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.company.skt.model.Settings;
import com.company.skt.view.DebugWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] args) {
		Settings.boot("assets/config/");
		Properties appCfg = Settings.getProperties(Settings.APP);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(appCfg.getProperty("name"));
		config.setResizable(false);
		ArrayList<String> argList = new ArrayList<>();
		Collections.addAll(argList, args);
		boolean debug = false;
		if(argList.contains("debug")) {
			debug = true;
		}
		boolean log = false;
		if(argList.contains("log")) {
			log = true;
		}
		if(debug) {
			config.setWindowedMode(1280, 720);
			config.setWindowPosition(485, (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - 360);
			Settings.setProperty(Settings.APP, "resolution_x", "1280");
			Settings.setProperty(Settings.APP, "resolution_y", "720");
			Settings.acceptAltAppCfg();
		} else {
			if(appCfg.getProperty("fullscreen").equals("true")) {
				config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
			} else {
				config.setWindowedMode(
					Integer.parseInt(appCfg.getProperty("resolution_x")),
					Integer.parseInt(appCfg.getProperty("resolution_y")));
			}
		}
		new Lwjgl3Application(new Skt(debug, log), config);
	}
}
