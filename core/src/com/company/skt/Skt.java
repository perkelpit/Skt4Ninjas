package com.company.skt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.company.skt.controller.Menu;
import com.company.skt.lib.ScreenController;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;
import com.company.skt.view.DebugWindow;

public class Skt extends ScreenController {
	private boolean debug;
	Lwjgl3Graphics graphics;
	DisplayMode displayMode;
	Lwjgl3Window window;

	public Skt(boolean debug) {
		this.debug = debug;
	}
	
	@Override
	public void create () {
		super.create();
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		displayMode = graphics.getDisplayMode();
		window = graphics.getWindow();
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
		if(debug) {
			DebugWindow.setPosition(window.getPositionX() - 400, window.getPositionY() - 30);
		}
	}
}
