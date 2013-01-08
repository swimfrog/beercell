package com.swimfrog.games.beercell;

import javax.microedition.lcdui.*;

public class TimerStrip extends Canvas implements Runnable {
	Table controller;
	Coordinate ul;
	Coordinate br;
	private boolean isRunning;
	private int value = 0;
	
	public void stop() {
		isRunning = false;
	}
	
	public void start() {
		isRunning = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public TimerStrip(Table controller, int x1, int y1, int x2, int y2) {
		this.controller = controller;
		ul = new Coordinate(x1, y1);
		br = new Coordinate(x2, y2);

		start();
	}
	
	protected void incrementTimer() {
		value++;
	}
	
	private String renderTime(int value) {
		int hours = value / 3600;
		value = value - hours * 3600;
		int minutes = value / 60;
		value = value - minutes * 60;
		int seconds = value;
		
		return pad(hours) + ":" + pad(minutes) + ":" + pad(seconds);
	}
	
	private String pad (int n) {
		if (n > 9) {
			return ""+n;
		} else {
			return "0"+n;
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(0x0033CC33);
		g.fillRect(ul.getX(), ul.getY(), this.getWidth(), this.getHeight());
		g.setColor(0xFF000000);
		g.drawString(renderTime(getValue()), ul.getX(), ul.getY(), Graphics.TOP | Graphics.LEFT);
	}

	public int getValue() {
		return value;
	}

	public void run() {
	    while (isRunning) {
	    	incrementTimer();
	    	//Repaint only the timer strip.
	        controller.repaint(ul.getX(), ul.getY(), br.getX(), br.getY());
	        try {
	          Thread.sleep(1000);
	        } catch (InterruptedException ie) {
	        }
	      }
		
	}

}
