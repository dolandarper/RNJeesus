package com.dolan.rnjeesus.entities.mob;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import com.dolan.rnjeesus.Settings;
import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.assets.FontRenderer;
import com.dolan.rnjeesus.assets.RNJesus;
import com.dolan.rnjeesus.entities.Entity;
import com.dolan.rnjeesus.entities.EntityMoveable;

public class EntityPlayer extends EntityMoveable {
	
	public boolean jumping;
	public boolean grounded;
	
	private Entity ground;
	
	private Texture armTex;
	private Image arm;
	private Texture legTex;
	private Image leg;
	private Image leg2;
	
	private float legArc;
	
	public float armRotation;
	private float mouseY;
	private float mouseX;
	public float shoulderX;
	public float shoulderY;
	public float gunX;
	public float gunY;
	public Vector2f relativeGun;
	private float fireTimer;
	
	private boolean dead;
	
	private String name;
	private int fontSize;
	private FontRenderer fntRend;
	
	//randomly generated stuff
	private float dmgMin;
	private float dmgMax;
	private float maxHP;
	private float hp;
	private float acceleration;
	private float maxSpeed;
	private float weight;
	private float jumpHeight;
	private float fireCD;
	private float critChance;
	private ArrayList<EntityZombie> zombies;
	private int bounty;
	
	//main RNJesus
	private RNJesus gaben;
	
	public enum Direction{
		left, right;
	}
	
	public Direction facing;
	
	public EntityPlayer(double x, double y, Entity ground, ArrayList<EntityZombie> zombies){
		super(x, y, 128, 128);
		
		this.zombies = zombies;
		
		gaben = new RNJesus();
		this.tex = AssetsLoader.loadTexture("player");
		this.width = tex.getImageWidth();
		this.height = tex.getImageHeight();
		facing = Direction.right;
		
		jumping = false;
		grounded = false;
		
		this.ground = ground;
		
		fireCD = 50;
		fireTimer = 0;

		this.armTex = AssetsLoader.loadTexture("player_arm");
		this.arm = new Image(armTex);
		
		this.legTex = AssetsLoader.loadTexture("player_leg");
		this.leg = new Image(legTex);
		this.leg2 = new Image(legTex);
		
		legArc = 40f;
		
		fontSize = 16;
		name = new String();
		fntRend = new FontRenderer(new UnicodeFont(new Font("Candara", 0, fontSize)), Color.white);
		fntRend.load();
		
		dead = false;
		
		critChance = gaben.nextFloat(0.05f, 0.15f);
		bounty = 0;
		
	}
	

	@Override
	public void update(int delta){
		this.hitbox = new Rectangle((int)x, (int)y, (int)width, (int)height);
		if(jumping){
			dy = -jumpHeight * delta * 0.01;
			jumping = false;
		}
		this.x += dx * delta * 0.01;
		
		if(y + height + dy >= ground.getY()){
			y = ground.getY() - height;
			grounded = true;
		}else{
			this.y += dy * delta * 0.01;
		}
		
		if(hp <= 0){
			hp = 0;
			this.dead = true;
		}
		
		this.dy += weight * delta * 0.01;
		
		if(grounded){
			dy = 0;
		}
		
		mouseX = Mouse.getX();
		mouseY = Settings.SCREEN_HEIGHT - Mouse.getY();
		
		fireTimer +=  delta * 0.1;
		
		hp -= delta * 0.001;
	}
	
	

	public void input(){
		
	}
	
	//movement
	public void moveRight(){
		if(dx < maxSpeed)
			dx += acceleration;
	}
	
	public void moveLeft(){
		if(dx > -maxSpeed)
			dx -= acceleration;
	}
	
	public void stop(){
		if(dx < 0)
			dx += acceleration;
		if(dx > 0)
			dx -= acceleration;
	}
	
	public void jump(){
		if(grounded){
			jumping = true;
			grounded = false;
		}
	}
	
	//shooting
	public EntityBullet shoot(){
		float bltDmg = gaben.nextFloat(dmgMin, dmgMax);
		boolean crit = false;
		float velocity = gaben.nextFloat(250f, 500f);
		if(Math.random() <= critChance){
			bltDmg += gaben.nextFloat(0.5f, 5f) * bltDmg;
			crit = true;
			velocity = 200f;
		}
		EntityBullet bullet = new EntityBullet(gunX, gunY, armRotation, velocity, bltDmg, crit, zombies);
		crit = false;
		return bullet;
	}

	//rendering
	@Override
	public void draw(){
		
		
		
		byte direction = 0;
		
		switch(facing){
			case left:
				direction = -1;
				break;
			case right:
				direction = 1;
				break;
		}
		
		if(direction == 0){
			System.out.println("Invalid Direction!");
			Display.destroy();
			System.exit(1);
		}
		
		//shoulder position of arm
		Vector2f armRotationPoint = new Vector2f(0.1f * arm.getWidth(), direction * 0.5f * arm.getHeight());
		arm.setCenterOfRotation(armRotationPoint.x, armRotationPoint.y);
		
		//relative position of the shoulder
		shoulderX = (float)(0.4 * width);
		shoulderY = (float)(0.35 * height) - direction * (arm.getHeight() * 0.5f)
				+ (0.4f * arm.getHeight());
		
		float gunXOnImg = (arm.getWidth());
		float gunYOnImg = (0.3f * arm.getHeight());
		
		float hyp = (float) Math.sqrt(Math.pow((gunXOnImg - armRotationPoint.x), 2) - Math.pow((gunYOnImg - armRotationPoint.y), 2));
		
		//relative position of gun
		if(facing == Direction.right){
			gunX = (float)x + shoulderX + ((float)Math.cos(Math.toRadians(armRotation)) * hyp);
			gunY = (float)y + shoulderY + ((float)Math.sin(Math.toRadians(armRotation)) * hyp );
		}else{
			gunX = (float)x + shoulderX + ((float)Math.cos(Math.toRadians(armRotation)) * hyp );
			gunY = (float)y + shoulderY + ((float)Math.sin(Math.toRadians(armRotation)) * hyp ) - (arm.getHeight());
		}
		
		relativeGun = new Vector2f((float)(gunX - x), (float)(gunY - y));

		//angle of arm rotation (degrees)
		armRotation = (float)Math.toDegrees(Math.atan2(mouseY - (Settings.SCREEN_HEIGHT / 2) - (height / 2) + shoulderY,
														mouseX - (Settings.SCREEN_WIDTH / 2) - (width / 2) + shoulderX));
		arm.setRotation(armRotation);
		
		float leg1Rotation = (float) (Math.sin(Math.toRadians(x * dx * 0.09)) * legArc);
		float leg2Rotation = -leg1Rotation;
		float hipX = (float) ((0.5 * width) - (0.5f * leg.getWidth()));
		float hipY = (float) ((0.75 * height) - (0.1f * leg.getHeight()));
		
		leg.setCenterOfRotation((0.5f * leg.getWidth()), (0.1f * leg.getHeight()));
		leg.setRotation(leg1Rotation);
		leg2.setCenterOfRotation((0.5f * leg.getWidth()), (0.1f * leg.getHeight()));
		leg2.setRotation(leg2Rotation);
		
		tex.bind();
		glPushMatrix();
		{

			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glTranslatef((Settings.SCREEN_WIDTH / 2) - (float)(this.width / 2), (Settings.SCREEN_HEIGHT / 2) - (float)(this.height / 2), 0f);
			glColor4f(1f, 1f, 1f, 1f);
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0 * direction, 0);glVertex2d(0, 0);
				glTexCoord2f(1 * direction, 0);glVertex2d(width, 0);
				glTexCoord2f(1 * direction, 1);glVertex2d(width, height);
				glTexCoord2f(0 * direction, 1);glVertex2d(0, height);
			}
			glEnd();
			
			
			arm.draw(shoulderX, shoulderY, arm.getWidth(), direction * arm.getHeight());
			leg.draw(hipX, hipY);
			leg2.draw(hipX, hipY);
			
			//drawing player related text - nametag, etc.
			if(!name.equals(null)){
				fntRend.draw(name, (float)(-(name.length() / 2) * (fontSize / 4)), (float) (-fontSize));
			}
			

			
			glDisable(GL_TEXTURE_2D);
			//drawing laser
			glLineWidth(1f);
			glColor4f(1f, 0f, 0f, 0.3f);
			glBegin(GL_LINES);
			{
				glVertex2d(relativeGun.x, relativeGun.y);
				glVertex2d(relativeGun.x + (Math.cos(Math.toRadians(armRotation)) * (hyp * 50)),
						   relativeGun.y + (Math.sin(Math.toRadians(armRotation)) * (hyp * 50)));
			}
			glEnd();
			glDisable(GL_BLEND);
		}
		glPopMatrix();
		
		
		//setting direction the player is facing
		
		if(armRotation > 90 || armRotation < -90){
			facing = Direction.left;
		}else{
			facing = Direction.right;
		}	
		
		
	}
	
	/*----------------------/
	/* 						/
	/* Getters and setters	/
	/* 						/
	/----------------------*/
	
	public int getBounty() {
		return bounty;
	}

	public void gifBounty(int bounty) {
		this.bounty += bounty;
	}
	
	public float getCritChance() {
		return critChance;
	}

	public void setCritChance(float critChance) {
		this.critChance = critChance;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public float getFireTimer() {
		return fireTimer;
	}

	public void setFireTimer(float fireTimer) {
		this.fireTimer = fireTimer;
	}

	public float getFireCD() {
		return fireCD;
	}

	public void setFireCD(float fireCD) {
		this.fireCD = fireCD;
	}
	
	public float getDmg() {
		return dmgMax;
	}

	public void setDmg(float dmg) {
		this.dmgMax = dmg;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(float maxHP) {
		this.maxHP = maxHP;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getJumpHeight() {
		return jumpHeight;
	}

	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}
	
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	
	//RNJesus init
	public void rndDmg(float dmg){
		this.dmgMax = dmg;
		this.dmgMin = dmgMax - (gaben.nextFloat(0.3f, 0.6f) * dmgMax);
	}
	
	public void rndMaxHealth(float hp){
		this.maxHP = hp;
		this.hp = maxHP;
	}
	
	public void rndAccel(float accel){
		this.acceleration = accel;
	}
	
	public void rndMaxSpd(float mxSpd){
		this.maxSpeed = mxSpd;
	}
	
	public void rndJumpHeight(float jmpHt){
		this.jumpHeight = jmpHt;
	}
	
	public void rndWeight(float wt){
		this.weight = wt;
	}
	
	public void rndFireCD(float fCD){
		this.fireCD = fCD;
	}
	
}
