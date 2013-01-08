package com.swimfrog.games.beercell;

import javax.microedition.lcdui.*;


public class Animator extends Canvas {
	private Coordinate from;
	private Coordinate to;
	private Image image;
	
	private final int totalFrames = 10; //Total number of frames to render.
	
	private Coordinate[] animationLocations = new Coordinate[totalFrames];
	
	public Animator(Image image, Coordinate from, Coordinate to) {
		this.image = image;
		this.from = from;
		this.to = to;
		
		calculate();
	}
	
	private void calculate() {
		int tmpX=0, tmpY=0;
		for (int i=0; i < totalFrames; i++) {
			tmpX = (from.getX() - to.getX() / (totalFrames - i));
			tmpY = (from.getY() - to.getY() / (totalFrames - i));
			animationLocations[i] = new Coordinate(tmpX, tmpY);
		}
	}
	
	public String toString() {
		String theString = "";
		for (int i=0; i<animationLocations.length; i++) {
			theString += i+": "+animationLocations[i].toString()+"; ";
		}
		return theString;
	}

	protected void paint(Graphics g) {
		
	}

}
