package com.dolan.rnjeesus.assets;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontRenderer {
	
	private UnicodeFont font;
	private Color colour;
	
	public FontRenderer(UnicodeFont font, Color colour){
		this.font = font;
		this.colour = colour;
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		font.getEffects().add(new ColorEffect(colour));
		font.addAsciiGlyphs();
		try{
			font.loadGlyphs();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
	
	public void draw(String text, float x, float y){
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_BLEND);
			font.drawString(x, y, text);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
	
	public void drawSuspended(String text, float x, float y){
		
	}
}
