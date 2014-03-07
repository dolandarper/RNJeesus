package com.dolan.rnjeesus.entities.mob;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.assets.FontRenderer;
import com.dolan.rnjeesus.assets.RNJesus;
import com.dolan.rnjeesus.entities.EntityMoveable;

import static org.lwjgl.opengl.GL11.*;

public class EntityZombie extends EntityMoveable {
	
	private Texture legTex;
	private Image leg;
	private Image leg2;
	private float weight;
	private double sizeFact;
	private float legArc = 40;
	private boolean grounded;
	private float maxHP;
	private float hp;
	private float dmg;
	
	private EntityPlayer plr;
	private RNJesus bluzord;
	public boolean dead;
	private FontRenderer fntRend;
	
	private int bounty;

	public EntityZombie(double x, double y, double sizeFact, EntityPlayer plr) {
		super(x, y, 64 * sizeFact, 128 * sizeFact);
		this.plr = plr;
		dead = false;
		bluzord = new RNJesus();
		this.sizeFact = sizeFact;
		tex = AssetsLoader.loadTexture("zombie_body");
		this.width = tex.getImageWidth() * sizeFact;
		this.height = tex.getImageHeight() * sizeFact;
		this.grounded = false;
		
		this.weight = 5f * (float)sizeFact;
		
		this.maxHP = bluzord.nextFloat(50f, 200f);
		this.hp = maxHP;
		this.dmg = bluzord.nextFloat(50f, 100f);
		
		legTex = AssetsLoader.loadTexture("zombie_leg");
		leg = new Image(legTex);
		leg2 = new Image(legTex);
		
		fntRend = new FontRenderer(new UnicodeFont(new Font("Gautami", 0, 16)), Color.white);
		fntRend.load();
		
		bounty = (int)((dmg + maxHP) * bluzord.nextFloat(0.9f, 1.1f));
	}
	
	public void changeSize(double size){
		sizeFact = size;
		this.width *= width * sizeFact;
		this.height *= sizeFact;
	}
	
	@Override
	public void update(int delta){
		
		this.hitbox = new Rectangle((float)x, (float)y, (float)width, (float)height);
		
		x += dx * delta * 0.01;
		y += dy * delta * 0.01;
		
		dy += weight * delta * 0.01;
		
		if(y + height + dy >= 0){
			y = 0 - height;
			grounded = true;
		}else{
			this.y += dy * delta * 0.01;
		}
		
		if(grounded){
			dy = 0;
		}
		
		if(hp <= 0){
			hp = 0;
			dead = true;
			plr.gifBounty(bounty);
		}
	}
	
	@Override
	public void draw(){
		
		float leg1Rotation = (float) (Math.sin(Math.toRadians(x * dx * 0.09)) * legArc);
		float leg2Rotation = -leg1Rotation;
		float hipX = (float) ((0.5 * width) - (0.5f * leg.getWidth()));
		float hipY = (float) ((0.75 * height) - (0.1f * leg.getHeight()));
		
		leg.setCenterOfRotation((0.5f * leg.getWidth()), (0.1f * leg.getHeight()));
		leg.setRotation(leg1Rotation);
		leg2.setCenterOfRotation((0.5f * leg.getWidth()), (0.1f * leg.getHeight()));
		leg2.setRotation(leg2Rotation);
		
		float hpPercent = hp / maxHP;
		
		glPushMatrix();
		{
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glTranslated(x, y, 0);
			glColor3f(1f, 1f, 1f);
			tex.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0f, 0f);glVertex2d(0d, 0d);
				glTexCoord2f(1f, 0f);glVertex2d(width, 0d);
				glTexCoord2f(1f, 1f);glVertex2d(width, height);
				glTexCoord2f(0f, 1f);glVertex2d(0d, height);
			}
			glEnd();
			
			float legWidth = (float)(leg.getWidth() * sizeFact);
			float legHeight = (float)(leg.getHeight() * sizeFact);

			leg.draw(hipX, hipY, legWidth, legHeight);
			leg2.draw(hipX, hipY, legWidth, legHeight);
			
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_BLEND);
			
			glTranslatef(0, -8, 0);
			glColor3f(1f, 1f, 1f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0d, 0d);
				glVertex2d(width, 0d);
				glVertex2d(width, 8d);
				glVertex2d(0d, 8d);
			}
			glEnd();
			glColor3f(0f, 1f, 0f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0d, 0d);
				glVertex2d(width * hpPercent, 0d);
				glVertex2d(width * hpPercent, 8d);
				glVertex2d(0d, 8d);
			}
			glEnd();
			
			fntRend.draw((int)hp + "/" + (int)maxHP, (float)(0), -16);
		}
		glPopMatrix();
	}

	public void damage(float dmg) {
		hp -= dmg;
	}
	
}
