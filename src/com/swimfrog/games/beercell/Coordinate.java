package com.swimfrog.games.beercell;

public class Coordinate {
	private int x = 0;
	private int y = 0;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	
}
