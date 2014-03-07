package com.dolan.rnjeesus.assets.gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;
import com.dolan.rnjeesus.assets.AssetsLoader;

import static org.lwjgl.opengl.GL11.*;

public class Button {
	
	private float x, y, width, height;
	private Texture tex;
	private boolean enabled;
	
	public Button(float x, float y, float w, float h, String texName){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.tex = AssetsLoader.loadTexture(texName);
		enabled = true;
	}
	
	public void draw(){
		glPushMatrix();
		{
			glEnable(GL_TEXTURE_2D);
			glTranslatef(x, y, 0);
			
			if(enabled){
				glColor3f(0f, 1f, 0f);
			}else{
				glColor3f(1f, 0f, 0f);
			}
			
			tex.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0f, 0f);glVertex2f(0f, 0f);
				glTexCoord2f(1f, 0f);glVertex2f(width, 0f);
				glTexCoord2f(1f, 1f);glVertex2f(width, height);
				glTexCoord2f(0f, 1f);glVertex2f(0f, height);
			}
			glEnd();
			
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();
	}
	
	public boolean isClicked(float mouseX, float mouseY){
		
		Rectangle mouseRect = new Rectangle(mouseX, mouseY, 1f, 1f);
		Rectangle bounds = new Rectangle(x, y, width, height);
		
		return Mouse.isButtonDown(0) && mouseRect.intersects(bounds) && enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
