package com.company.skt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.company.skt.controller.Menu;
import com.company.skt.lib.ScreenController;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;
import com.company.skt.view.DebugWindow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Skt extends ScreenController {
	private static boolean debug;
	private Lwjgl3Graphics graphics;
	private DisplayMode displayMode;
	private Lwjgl3Window window;
	private static ScheduledExecutorService debugWindowPositionUpdater;

	public Skt(boolean debug) {
		Skt.debug = debug;
	}
	
	@Override
	public void create () {
		super.create();
		Local.boot("assets/local/");
		Fonts.boot("assets/fonts/");
		Assets.boot("assets/");
		DebugWindow.createDebugWindow("assets/logs/");
		if(debug) {
			DebugWindow.showDebugWindow();
		}
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		displayMode = graphics.getDisplayMode();
		window = graphics.getWindow();
		window.setWindowListener(new Lwjgl3WindowAdapter() {
			@Override
			public boolean closeRequested() {
				if(debug) {
					debugWindowPositionUpdater.shutdownNow();
				}
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
		if(debug) {
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
}
