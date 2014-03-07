package com.dolan.rnjeesus.entities;

import org.newdawn.slick.geom.Rectangle;




public class EntityMoveable extends Entity implements IEntityMoveable {

	protected double dx, dy;
	
	public EntityMoveable(double x, double y, double width, double height){
		super(x, y, width, height);
		this.dx = 0;
		this.dy = 0;
	}
	
	@Override
	public void update(int delta){
		this.x += delta * dx;
		this.y += delta * dy;
	}
	
	public double getDX(){
		return this.dx;
	}
	
	public double getDY(){
		return this.dy;
	}
	
	public void setDX(double dx){
		this.dx = dx;
	}
	
	public void setDY(double dy){
		this.dy = dy;
	}

	@Override
	public void draw() {
		
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return hitbox;
	}

}
