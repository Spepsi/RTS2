package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import model.Game;

public class Main {
	// A REGLER \\
	public static float ratioSpace = 1f;
	public static int framerate = 60;
	public static int nDelay = 2;
	///////\\\\\\\\\
	public static float increment = 0.05f;
	public static boolean fullscreen=false;
	
	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.out.println(new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		
		try {
			
//			int resolutionX = (int)screenSize.getWidth();
//			int resolutionY = (int)screenSize.getHeight();
//			fullscreen = true;
			int resolutionX = (int)screenSize.getWidth()/2;
			int resolutionY = (int)screenSize.getHeight()/2;
			fullscreen = false;
			Game game = new Game(resolutionX,resolutionY);
			AppGameContainer app = new AppGameContainer(game);
			Game.app = app;
			app.setIcon("ressources/images/danger/iconeJeu.png");
			app.setDisplayMode(resolutionX, resolutionY, fullscreen);
			app.setFullscreen(fullscreen);
			app.setShowFPS(true);
			app.setAlwaysRender(true);
			app.setUpdateOnlyWhenVisible(false);
			app.setClearEachFrame(true);
			app.setVSync(true);
			//app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}