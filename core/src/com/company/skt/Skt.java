package com.company.skt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.company.skt.controller.Menu;
import com.company.skt.lib.ScreenController;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;
import com.company.skt.view.DebugWindow;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Skt extends ScreenController {
	
	private static String basePath;
	private static boolean debug;
	private Lwjgl3Graphics graphics;
	private Lwjgl3Window window;
	private static ScheduledExecutorService debugWindowPositionUpdater;

	public Skt(boolean debug) {
		Skt.debug = debug;
	}
	
	@Override
	public void create () {
		super.create();
		basePath = new File("assets/").exists() ? "assets/" : "";
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		window = graphics.getWindow();
		Local.boot(basePath + "local/");
		Fonts.boot(basePath + "fonts/");
		Assets.boot(basePath);
		if(debug) {
			DebugWindow.createDebugWindow("assets/logs/");
			DebugWindow.showDebugWindow();
			window.setWindowListener(new Lwjgl3WindowAdapter() {
				@Override
				public boolean closeRequested() {
					DebugWindow.disposeDebugWindow();
					return true;
				}
				
				@Override
				public void iconified(boolean isIconified) {
					if(!isIconified) {
						DebugWindow.getWindow().toFront();
					}
				}
				
				@Override
				public void focusGained() {
					DebugWindow.getWindow().toFront();
				}
			});
			debugWindowPositionUpdater = Executors.newSingleThreadScheduledExecutor();
			debugWindowPositionUpdater.scheduleAtFixedRate(() -> {
				DebugWindow.setPosition(window.getPositionX() - 400, window.getPositionY() - 30);
			}, 0, 5, TimeUnit.MILLISECONDS);
		}
		setActiveScreen(new Menu());
	}
	
	public static ScheduledExecutorService getDebugWindowPositionUpdater() {
		return debugWindowPositionUpdater;
	}
	
	public static boolean isDebug() {
		return debug;
	}
	
	public static String getBasePath() {
		return basePath;
	}
	
}
