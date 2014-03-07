package com.dolan.rnjeesus;

import static org.lwjgl.opengl.GL11.*;
import static com.dolan.rnjeesus.Settings.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Image;

import com.dolan.rnjeesus.assets.AssetsLoader;
import com.dolan.rnjeesus.assets.RNJesus;
import com.dolan.rnjeesus.assets.SuspendedText;
import com.dolan.rnjeesus.assets.gui.Button;
import com.dolan.rnjeesus.assets.gui.DebugMenu;
import com.dolan.rnjeesus.assets.gui.HUD;
import com.dolan.rnjeesus.assets.gui.TextBox;
import com.dolan.rnjeesus.entities.mob.EntityBullet;
import com.dolan.rnjeesus.entities.mob.EntityPlayer;
import com.dolan.rnjeesus.entities.mob.EntityZombie;
import com.dolan.rnjeesus.entities.terrain.Background;
import com.dolan.rnjeesus.entities.terrain.Ground;
import com.dolan.rnjeesus.entities.terrain.Moon;

public class Game {
	
	//global vars
	boolean running = false;
	EntityPlayer player;
	Ground ground;
	float mouseX;
	float mouseY;
	DebugMenu debug;
	DebugMenu stats;
	boolean debugging;
	String name;
	TextBox nameBox;
	Button playButton;
	Background sky;
	Image hills;
	Moon moon;
	RNJesus jesus;
	HUD mainHUD;
	public static DisplayMode dispMode;
	
	private SuspendedText critMarker;
	
	//entities
	ArrayList<EntityZombie> zombies;
	ArrayList<EntityBullet> bullets;
	
	private GameState currState;
	
	public enum GameState {
		MENU, INGAME, LOGIN, DEAD
	}
	
	//constructor
	public Game(){
		
	}
	
	
	
	//start running the game
	void run(){
		initDisplay();
		initOpenGL();
		setupEntities();
		running = true;
		gameLoop();
		cleanup();
	}
	
	//initialise main display window
	void initDisplay(){
		lastFrame = getDelta();
		try {
			DisplayMode[] dispModes = Display.getAvailableDisplayModes();
			DisplayMode dm = null;
			
			for(int i = 0; i < dispModes.length; i++){
				 if (dispModes[i].getWidth() == Settings.SCREEN_WIDTH
					&& dispModes[i].getHeight() == Settings.SCREEN_HEIGHT
			        && dispModes[i].isFullscreenCapable())
				 {
					 dm = dispModes[i];
				 }
			}
			
			dispMode = dm;
			
			Display.setDisplayMode(dm);
			Display.setTitle("RNJeesus");
			Display.setInitialBackground(0.7f, 0.7f, 0.7f);
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	//initialise openGL
	void initOpenGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1200);
		glMatrixMode(GL_MODELVIEW);
	}
	
	//initialise entities
	void setupEntities(){
		jesus = new RNJesus();
		
		bullets = new ArrayList<EntityBullet>();
		zombies = new ArrayList<EntityZombie>();
		
		debugging = false;
		ground = new Ground(0, 0, jesus.nextInt(3000, 500000), 2000);
		debug = new DebugMenu(16, "Candara", Color.white);
		debug.setRootPos(0, 0);
		nameBox = new TextBox((Settings.SCREEN_WIDTH / 2) - (256 / 2), (Settings.SCREEN_HEIGHT / 2) - (64 / 2), 256, 64);
		playButton = new Button(nameBox.getX(), nameBox.getY() + nameBox.getHeight() + 5,
				nameBox.getWidth(), nameBox.getHeight(), "button_play");
		sky = new Background("sky");
		hills = new Image(AssetsLoader.loadTexture("hills"));
		
		moon = new Moon(jesus.nextDouble(1d, 5d), jesus.nextInt(0, SCREEN_WIDTH), jesus.nextInt(0, SCREEN_HEIGHT / 6));
		player = new EntityPlayer(ground.getWidth() / 2, -150, ground, zombies);
		critMarker = new SuspendedText(new Font("Forte", 0, 16), Color.red);
		
	}
	
	//initialise plr
	void initPlayer(){
		player.setName(name);
		player.rndAccel(jesus.nextFloat(2f, 3f));
		player.rndDmg(jesus.nextFloat(50f, 100f));
		player.rndMaxHealth(jesus.nextFloat(50f, 200f));
		player.rndJumpHeight(jesus.nextFloat(50f, 100f));
		player.rndMaxSpd(jesus.nextFloat(20f, 50f));
		player.rndWeight(jesus.nextFloat(3f, 5f));
		player.rndFireCD(jesus.nextFloat(20f, 70f));

		mainHUD = new HUD(this.player);
		
		stats = new DebugMenu(16, "Candara", Color.white);
		stats.setRootPos(SCREEN_WIDTH / 2, 16);
		
		stats.addLine("Jump Height: " + player.getJumpHeight());
		stats.addLine("Damage: " + player.getDmg());
		stats.addLine("Max Speed : " + player.getMaxSpeed());
		stats.addLine("Weight: " + player.getWeight());
		stats.addLine("Fire Cooldown: " + player.getFireCD());
		
		zombies.add(new EntityZombie(player.getX() - 64, player.getY(), jesus.nextDouble(0.7d, 1.4d), player));
	}
	
	//post run events
	void cleanup(){
		running = false;
		Display.destroy();
		System.exit(0);
	}
	
	//main game loop
	void gameLoop(){
		currState = GameState.LOGIN;
		
		while(!Display.isCloseRequested() && running){
			switch(currState){
				case DEAD:
					break;
					
				case INGAME:
					render();
					update(getDelta());
					input();
					break;
					
				case LOGIN:
					drawLoginScreen();
					break;
					
				case MENU:
					break;
					
				default:
					break;
			}
			
			Display.update();
			Display.sync(60);
		}
	}
	
	//draw login screen
	private void drawLoginScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		nameBox.draw();
		playButton.draw();
		if(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				char ch = Keyboard.getEventCharacter();
				if(Keyboard.isKeyDown(Keyboard.KEY_BACK)){
					nameBox.backSpace();
				}else if(Character.isAlphabetic(ch) || Character.isDigit(ch)){
					nameBox.addText(Keyboard.getEventCharacter());
				}
			}
		}

		float mouseX = Mouse.getX();
		float mouseY = Settings.SCREEN_HEIGHT - Mouse.getY();
		
		if(!nameBox.getText().equals("") && nameBox.getText().length() >= 4){
			playButton.setEnabled(true);
		}else{
			playButton.setEnabled(false);
		}
		
		if(playButton.isClicked(mouseX, mouseY)){
			if(!nameBox.getText().equals("")){
				name = nameBox.getText();
				initPlayer();
				currState = GameState.INGAME;
				Display.setInitialBackground(0f, 0.7f, 0.9f);
			}
		}
		
	}
	
	//render objects
	void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//drawing sky and rape moon
		sky.draw();
		moon.draw();
		
		//drawing the hills
		glPushMatrix();
		{
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glTranslatef(0, 0f, 0f);
			hills.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		if(debugging){
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			debug.drawStaticText();
			debug.drawUpdatedLine("X: " + (int)player.getX() + "; Y: " + (int)player.getY());
			debug.drawUpdatedLine("DX: " + (int)player.getDX() + "; DY: " + (int)player.getDY() + "; Grounded: " + player.grounded);
			debug.drawUpdatedLine("MouseX: " + mouseX + "; MouseY: " + mouseY);
			debug.drawUpdatedLine("Facing: " + player.facing);
			debug.drawUpdatedLine("Bullets: " + bullets.size());
			debug.drawUpdatedLine("Zombies: " + zombies.size());
			debug.drawUpdatedLine("GunX: " + player.gunX + "; GunY: " + player.gunY);
			debug.drawUpdatedLine("Relative Gun: X: " + player.relativeGun.x + "; Y: " + player.relativeGun.y);
			debug.drawUpdatedLine("Arm angle: " + player.armRotation);
			debug.drawUpdatedLine("Fire Timer: " + player.getFireTimer() + "/" + player.getFireCD());
			
			stats.drawStaticText();
			stats.drawUpdatedLine("Crit Chance: " + player.getCritChance());
			glDisable(GL_BLEND);
		}
		
		
		glPushMatrix();
		{

			
			glTranslated((Settings.SCREEN_WIDTH / 2) - player.getX() - (player.getWidth() / 2),
					(Settings.SCREEN_HEIGHT / 2) - player.getY() - (player.getHeight() / 2), 0);
			
			//draw bullets
			for(EntityBullet blt : bullets){
						blt.draw();
					}
			ground.draw();
			
			for(EntityZombie zmb : zombies){
				zmb.draw();
			}

			critMarker.update();
			
		}
		glPopMatrix();

		player.draw();
		mainHUD.draw();
	}
	
	//update entities
	void update(int delta){
		mouseX = Mouse.getX();
		mouseY = Settings.SCREEN_HEIGHT - Mouse.getY();
		player.update(delta);
		mainHUD.update();

		Iterator<EntityBullet> blts = bullets.iterator();
		
		while(blts.hasNext()){
			EntityBullet blt = blts.next();
			blt.draw();
			blt.update(delta);
			if(blt.destroyed){
				if(blt.hit){
					if(blt.isCrit()){
						float dx = jesus.nextFloat(-1f, 1f);
						float dy = jesus.nextFloat(-0.5f, 0f);
						critMarker.draw("!" + (int)blt.getDmg() + "!", (float)blt.getX(), (float)blt.getY(), 25, dx, dy);
					}
				}
				blts.remove();
			}
		}
		
		Iterator<EntityZombie> zmbs = zombies.iterator();
		
		while(zmbs.hasNext()){
			EntityZombie zmb = zmbs.next();
			zmb.draw();
			zmb.update(delta);
			if(zmb.dead){
				zmbs.remove();
			}
		}
		
		if(player.isDead()){
			currState = GameState.DEAD;
		}
		
	}
	
	//handle user input
	void input(){
		//keys to hold down
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			running = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			player.moveRight();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			player.moveLeft();
		}
		else if((!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) || (Keyboard.isKeyDown(Keyboard.KEY_A)
				&& Keyboard.isKeyDown(Keyboard.KEY_D))){
			player.stop();
		}
		
		//once off presses - not holding down key
		
		
		//end input
		
		//one press keys
		while(Keyboard.next()){
			if(Keyboard.isKeyDown(Keyboard.KEY_L)){
				debugging = !debugging;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				player.jump();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
				zombies.add(new EntityZombie(player.getX(), player.getY() - 64, jesus.nextFloat(0.7f, 1.3f), player));
			}
		}
		//end input
		
		//mouse presses
		while(Mouse.next()){
		}

		if(Mouse.isButtonDown(0)){
			if(player.getFireTimer() >= player.getFireCD()){
				bullets.add(player.shoot());
				player.setFireTimer(0);
			}
		}
		 
	}
	
	public static void main(String args[]){
		new Game().run();
	}
	
	//delta stuff
	private long lastFrame;
	
	private long getTime(){
		return(Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private int getDelta(){
		long currentTime = getTime();
		int delta = (int)(currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
}
