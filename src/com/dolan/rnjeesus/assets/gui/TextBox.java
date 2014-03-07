package com.dolan.rnjeesus.assets.gui;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.assets.FontRenderer;

import static org.lwjgl.opengl.GL11.*;

public class TextBox {
	
	private float x, y;
	private float width, height;
	private String text;
	
	private Texture tex;
	private FontRenderer font;
	
	public TextBox(float x, float y, float width, float height){
		tex = AssetsLoader.loadTexture("textbox");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		font = new FontRenderer(new UnicodeFont(new Font("Candara", 0, 16)), new Color(0, 0, 0));
		font.load();
		
		text = new String();
	}
	
	public void draw(){
		glPushMatrix();
		{
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glTranslatef(x, y, 0);
			glColor3f(1f, 1f, 1f);
			tex.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0f, 0f);glVertex2f(0f, 0f);
				glTexCoord2f(1f, 0f);glVertex2f(width, 0f);
				glTexCoord2f(1f, 1f);glVertex2f(width, height);
				glTexCoord2f(0f, 1f);glVertex2f(0f, height);
			}
			glEnd();
			
			glColor3f(1f, 1f, 1f);
			font.draw(text, (width / 2) - (text.length() * 2), (height / 2) - (16 / 2));
			
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_BLEND);
			
		}
		glPopMatrix();
	}
	
	public void addText(char ch){
		text += ch;
	}
	
	public void backSpace(){
		if(text.length() > 0){
			text = text.substring(0, text.length() - 1);
		}
	}
	
	
	//getters & setters
	public float getX() {
		return x;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
