package com.dolan.rnjeesus.assets.gui;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;
import com.dolan.rnjeesus.Settings;
import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.assets.FontRenderer;
import com.dolan.rnjeesus.entities.mob.EntityPlayer;

import static org.lwjgl.opengl.GL11.*;

public class HUD {
	EntityPlayer plr;
	Texture bgTex;
	Image bgImg;
	Image barImg;
	float hp;
	float maxHP;
	float hpPercent;
	
	int width;
	int height;
	
	int hpBarWidth = 492;
	int hpBarHeight = 24;
	
	float barX = 205;
	float barY = 438;
	
	int score;
	
	FontRenderer fntRend;
	
	public HUD(EntityPlayer plr){
		width = Settings.SCREEN_WIDTH;
		height = Settings.SCREEN_HEIGHT;
		bgTex = AssetsLoader.loadTexture("hud");
		bgImg = new Image(bgTex);
		barImg = new Image(AssetsLoader.loadTexture("hp_bar"));
		
		this.plr = plr;
		maxHP = this.plr.getMaxHP();
		hp = this.plr.getHp();
		hpPercent = 1f;
		
		fntRend = new FontRenderer(new UnicodeFont(new Font("Candara", 0, 16)), Color.black);
		fntRend.load();
		
		score = 0;
	}
	
	public void draw(){
		
		
		glPushMatrix();
		{
			float scaleX = (float)(Settings.SCREEN_WIDTH) / (float)(bgTex.getImageWidth());
			float scaleY = (float)(Settings.SCREEN_HEIGHT) / (float)(bgTex.getImageHeight());
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glScalef(scaleX, scaleY, 0);
			
			bgImg.draw(0, 0);
			barImg.draw(barX, barY, hpPercent * hpBarWidth, hpBarHeight);
			fntRend.draw((int)hp + "/" + (int)maxHP, 435, 440);
			
			fntRend.draw("Bounty: " + score, 707, 378);
			
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();
	}
	
	public void update(){
		maxHP = plr.getMaxHP();
		hp = plr.getHp();
		
		if(hp < 0) hp = 0;
		hpPercent = hp / maxHP;
		
		score = plr.getBounty();
		
	}
}
