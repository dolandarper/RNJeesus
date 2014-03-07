package com.dolan.rnjeesus.entities.mob;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.entities.EntityMoveable;


public class EntityBullet extends EntityMoveable {

	private Image img;
	private Image critImg;
	private float velocity;
	private float angle;
	private float dmg;
	private boolean crit; //TODO: Add crit text
	private ArrayList<EntityZombie> zombies;
	public boolean destroyed;
	public boolean hit;
	
	public EntityBullet(double x, double y, float angle, float velocity, float dmg, boolean crit, ArrayList<EntityZombie> zombies) {
		super(x, y, 6, 2);
		destroyed = false;
		hit = false;
		
		this.zombies = zombies;
		
		this.dmg = dmg;
		this.crit = crit;
		
		this.angle = angle;
		this.tex = AssetsLoader.loadTexture("bullet");
		img = new Image(tex);
		critImg = new Image(AssetsLoader.loadTexture("bullet_crit"));
		
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.velocity = velocity;
		dx = (float)(Math.cos(Math.toRadians(this.angle))) * this.velocity;
		dy = (float)(Math.sin(Math.toRadians(this.angle))) * this.velocity;
	}
	
	@Override
	public void update(int delta){
		this.hitbox = new Rectangle((float)x, (float)y, (float)width, (float)height);
		checkCollisions();
		
		this.x += this.dx * delta * 0.01;
		this.y += this.dy * delta * 0.01;
	}
	
	@Override
	public void draw(){
		glPushMatrix();
		{
			glEnable(GL_BLEND);
			if(crit){
				critImg.setRotation(angle);
				critImg.draw((float)x, (float)y);
			}else {
				img.setRotation(angle);
				img.draw((float)x, (float)y);
			}
			
			glDisable(GL_BLEND);
		}
		glPopMatrix();
	}
	
	public void checkCollisions(){
		for(EntityZombie zmb : zombies){
			if(this.intersects(zmb)){
				zmb.damage(this.dmg);
				destroy();
				this.hit = true;
			}
		}
	}
	
	public float getDmg() {
		return dmg;
	}

	public void setDmg(float dmg) {
		this.dmg = dmg;
	}

	public boolean isCrit() {
		return crit;
	}

	public void setCrit(boolean crit) {
		this.crit = crit;
	}

	public void destroy(){
		destroyed = true;
	}
	
}
