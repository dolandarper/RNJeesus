package com.dolan.rnjeesus.assets;

import java.util.Random;



public class RNJesus{
	Random mainRNG;
	
	public RNJesus(){
		mainRNG = new Random();
	}
	
	public float nextFloat(float from, float to){
		return from + (mainRNG.nextFloat() * (to - from));
	}
	
	public double nextDouble(double from, double to){
		return from + (mainRNG.nextDouble() * (to - from));
	}
	
	public int nextInt(int from, int to){
		return from + mainRNG.nextInt(to - from);
	}
	
}
