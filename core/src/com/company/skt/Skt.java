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
	private boolean debug;
	Lwjgl3Graphics graphics;
	DisplayMode displayMode;
	Lwjgl3Window window;
	ScheduledExecutorService debugWindowPositionUpdater;

	public Skt(boolean debug) {
		this.debug = debug;
	}
	
	@Override
	public void create () {
		super.create();
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
					DebugWindow.getWindow().requestFocus();
				}
			}
			
			@Override
			public void refreshRequested() {
				super.refreshRequested();
			}
			
			@Override
			public void focusGained() {
				DebugWindow.getWindow().requestFocus();
			}
		});
		if(debug) {
			// TODO only when window is actually moved
			debugWindowPositionUpdater = Executors.newSingleThreadScheduledExecutor();
			debugWindowPositionUpdater.scheduleAtFixedRate(() -> {
				DebugWindow.setPosition(window.getPositionX() - 400, window.getPositionY() - 30);
			}, 0, 1, TimeUnit.MILLISECONDS);
		}
		Local.boot("assets/local/");
		Fonts.boot("assets/fonts/");
		Assets.boot("assets/");
		DebugWindow.createDebugWindow();
		if(debug) {
			DebugWindow.showDebugWindow();
		}
		setActiveScreen(new Menu());
	}
	
	@Override
	public void render() {
		super.render();
	}
}
