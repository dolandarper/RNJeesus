package com.dolan.rnjeesus.assets;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.UnicodeFont;

public class SuspendedText {
	private FontRenderer fntRend;
	boolean drawing;
	private long duration;
	private float timer;
	private String str;
	private float x;
	private float y;
	private float dx;
	private float dy;

	public SuspendedText(Font font, Color colour) {
		fntRend = new FontRenderer(new UnicodeFont(font), colour);
		fntRend.load();
		
		drawing = false;
		duration = 0;
		timer = 0;
		
		dx = 0;
		dy = 0;
	}
	
	public void update(){
		
		if(drawing){
			timer += 1f;
			x += dx;
			y += dy;
			if(timer >= duration){
				drawing = false;
				timer = 0;
			}else{
				draw();
			}
		}
	}
	
	private void draw(){
		fntRend.draw(str, x, y);
	}
	
	public void setDuration(long time){
		duration = time;
	}
	
	public void draw(String str, float x, float y, long duration){
		this.str = str;
		this.x = x;
		this.y = y;
		this.duration = duration;
		drawing = true;
	}
	
	public void draw(String str, float x, float y, long duration, float dx, float dy){
		this.dx = dx;
		this.dy = dy;
		draw(str, x, y, duration);
	}

}
