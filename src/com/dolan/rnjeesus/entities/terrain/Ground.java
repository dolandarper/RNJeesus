package com.dolan.rnjeesus.entities.terrain;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.entities.Entity;

public class Ground extends Entity {
	
	public double width;
	public double height;
	public Texture tex;
	private double tileX;
	private double tileY;
	
	public Ground(double x, double y, double width, double height){
		super(x, y, width, height);
		tex = AssetsLoader.loadTexture("ground");
		this.width = width;
		this.height = height;
		this.tileX = width / tex.getImageWidth();
		this.tileY = height / tex.getImageHeight();
		this.hitbox = new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	
	public void draw(){
		
			tex.bind();
			glBegin(GL_QUADS);
			{
				glColor3f(1f, 1f, 1f);
				glTexCoord2d(0 * tileX, 0 * tileY);glVertex2d(0, 0);
				glTexCoord2d(1 * tileX, 0 * tileY);glVertex2d(width, 0);
				glTexCoord2d(1 * tileX, 1 * tileY);glVertex2d(width, height);
				glTexCoord2d(0 * tileX, 1 * tileY);glVertex2d(0, height);
			}
			glEnd();
	}

	@Override
	public void update(int delta) {
	}
	
}
