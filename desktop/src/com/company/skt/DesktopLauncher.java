package com.company.skt;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.company.skt.model.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
/**
 * Entry-class for the libGDX-project on dektop(only one implemented by now). <br>
 * It checks for args {@code debug} and {@code log} and sets the proper resolution and window-mode for the app. <br>
 * It also boot the {@link Settings} class, because it is already needed here. <br>
 * It afterwards start the actual program by calling: <br>
 * {@code new Lwjgl3Application(new Skt(debug, log), config);}<br><br>
 * If {@code log} is passed within {@code args} all those messages created by the program via
 * {@code DebugWindow.println()} are written to a time stamped log file. <br>
 * If {@code debug} is passed a debug window monitoring some relevant values and
 * showing those messages will be docked to the left of the game, which is set to a resolution of 1280x720 and to
 * windowed mode. */
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
			Settings.acceptTempAppCfg();
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
