package com.dolan.rnjeesus.entities.terrain;

import org.newdawn.slick.opengl.Texture;

import com.dolan.rnjeesus.Settings;
import com.dolan.rnjeesus.assets.AssetsLoader;

import static org.lwjgl.opengl.GL11.*;
	
public class Moon{
	Texture tex;
	double width, height;
	double x, y;
	public Moon(double sizeFact, double x, double y){
		tex = AssetsLoader.loadTexture("moon");
		this.x = x;
		this.y = y;
		
		this.width = tex.getImageWidth() * sizeFact;
		this.height = tex.getImageHeight() * sizeFact;
	}
	
	public void draw(){
		glPushMatrix();
		{	
			byte dir = 0;
			
			if(x > Settings.SCREEN_WIDTH){
				dir = -1;
			}
			else dir = 1;
			
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glTranslated(x, y, 0);
			
			tex.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0f * dir, 0f);glVertex2d(0d, 0d);
				glTexCoord2f(1f * dir, 0f);glVertex2d(width, 0d);
				glTexCoord2f(1f * dir, 1f);glVertex2d(width, height);
				glTexCoord2f(0f * dir, 1f);glVertex2d(0d, height);
			}
			glEnd();
			
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();
	}
}
