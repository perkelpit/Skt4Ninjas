package com.company.skt;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.company.skt.Skt;
import com.company.skt.data.Settings;

import java.util.Properties;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		Settings.initiate("./src/config/");
		Properties properties = Settings.getProperties(Settings.APP);
		//config. = properties
		
		new Lwjgl3Application(new Skt(), config);
	}
}
