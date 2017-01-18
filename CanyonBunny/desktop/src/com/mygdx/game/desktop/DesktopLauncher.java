package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.mygdx.game.CanyonBunnyMain;

public class DesktopLauncher {

	private static  boolean rebuildAtlas = true;
	private static  boolean drawDebugOutline = false;

	public static void main (String[] arg) {
		if(rebuildAtlas){
			TexturePacker2.Settings settings = new TexturePacker2.Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			//settings.debug = drawDebugOutline;
			TexturePacker2.process(settings,"android/assets-raw/images","android/assets/images","canyonbunny.pack");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.useGL30 = false;
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
