package com.dolan.rnjeesus.entities.terrain;

import org.newdawn.slick.Image;

import static org.lwjgl.opengl.GL11.*;

import com.dolan.rnjeesus.Settings;
import com.dolan.rnjeesus.assets.AssetsLoader;

public class Background {
	Image tex;
	
	public Background(String texName){
		tex = new Image(AssetsLoader.loadTexture(texName));
	}
	
	public void draw(){
		glEnable(GL_TEXTURE_2D);
		glPushMatrix();
		{
			glColor3f(1f, 1f, 1f);
			
			tex.draw(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
		}
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
}
