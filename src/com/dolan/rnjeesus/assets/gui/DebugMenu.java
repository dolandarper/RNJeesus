package com.dolan.rnjeesus.assets.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.UnicodeFont;

import com.dolan.rnjeesus.assets.FontRenderer;


public class DebugMenu {
	
	private Vector2f root;
	private FontRenderer font;
	private ArrayList<String> items;
	private int fontSize;
	private float spaceUsed = 0;
	
	public DebugMenu(int fontSize, String fontName, Color colour){
		this.fontSize = fontSize;
		this.items = new ArrayList<String>();
		this.font = new FontRenderer(new UnicodeFont(new Font(fontName, 0, this.fontSize)), colour);
		this.font.load();
		root = new Vector2f(0, 0);
	}
	
	public void setRootPos(float x, float y){
		root = new Vector2f(x, y);
	}
	
	public void drawStaticText(){
		spaceUsed = root.y;
		for(String str : items){
			font.draw(str, root.x, spaceUsed);
			spaceUsed += fontSize;
		}
	}
	
	public void drawUpdatedLine(String str){
		font.draw(str, root.x, spaceUsed);
		spaceUsed += fontSize;
	}
	
	public void addLine(String str){
		items.add(str);
	}
}
