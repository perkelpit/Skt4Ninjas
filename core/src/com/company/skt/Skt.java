package com.company.skt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.company.skt.controller.Menu;
import com.company.skt.lib.ScreenController;
import com.company.skt.lib.TaskCompleteException;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;
import com.company.skt.model.Styles;
import com.company.skt.view.DebugWindow;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * {@code Skt} is the actual starting point of the program. It boots all those "static" classes (
 * {@link Local}, {@link Assets}, {@link Fonts} except {@link com.company.skt.model.Settings Settings}, which needs to
 * be booted earlier in {@code com.company.skt.DesktopLauncher}. <br><br>
 * It also processes the program args {@code log} and {@code debug} passed as params in the constructor: <br>
 * If {@code log} is {@code true} all those messages created by the program via
 * {@code DebugWindow.println()} are written to a time stamped log file. <br>
 * If {@code debug} is {@code true} a debug window monitoring some relevant values and
 * showing those messages will be docked to the left of the game, which is set to a resolution of 1280x720 and to
 * windowed mode by {@code com.company.skt.DesktopLauncher}. */
public class Skt extends ScreenController {
	
	private static String basePath;
	private static boolean debug;
	private static boolean log;
	private static boolean stop;
	private Lwjgl3Graphics graphics;
	private Lwjgl3Window window;
	private static ScheduledThreadPoolExecutor executor;
	
	static {
		/* TODO seems a crude solution:
	 	 * better figure out how to combine singleThreadScheduled and CachedThreadPool */
		executor = new ScheduledThreadPoolExecutor(16);
	}

	public Skt(boolean debug, boolean log) {
		Skt.debug = debug;
		Skt.log = log;
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
		if(isLog()) {
			DebugWindow.bootLogging(basePath + "logs/");
		}
		window.setWindowListener(new Lwjgl3WindowAdapter() {
			@Override
			public boolean closeRequested() {
				setStop(true);
				if(debug) {
					DebugWindow.disposeDebugWindow();
				}
				executor.shutdown();
				return true;
			}
			
			@Override
			public void iconified(boolean isIconified) {
				if(debug && !isIconified) {
					DebugWindow.getWindow().toFront();
				}
			}
			
			@Override
			public void focusGained() {
				if(debug) {
					DebugWindow.getWindow().toFront();
				}
			}
		});
		if(debug) {
			DebugWindow.createDebugWindow("assets/logs/");
			DebugWindow.showDebugWindow();
			executor.scheduleAtFixedRate(() -> {
				if(isStop()) {
					throw new TaskCompleteException();
				}
				DebugWindow.setPosition(window.getPositionX() - 400, window.getPositionY() - 30);
			}, 0, 5, TimeUnit.MILLISECONDS);
		}
		setActiveScreen(new Menu());
	}
	
	public static ScheduledThreadPoolExecutor getExecutor() {
		return executor;
	}
	
	public static boolean isDebug() {
		return debug;
	}
	
	public static boolean isLog() {
		return log;
	}
	
	public static String getBasePath() {
		return basePath;
	}
	
	public static void setStop(boolean stop) {
		Skt.stop = stop;
	}
	
	public static boolean isStop() {
		return stop;
	}
	
}
