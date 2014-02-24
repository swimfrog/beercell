package com.swimfrog.games.beercell;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m2g.*;

public class StatsScreen extends Canvas {
//	static int[] keyCodes = {KEY_NUM0, KEY_NUM1, KEY_NUM2, KEY_NUM3, KEY_NUM4,
//        KEY_NUM5, KEY_NUM6, KEY_NUM7, KEY_NUM8, KEY_NUM9,
//        KEY_POUND, KEY_STAR};
//	static String[] keyNames = {"KEY_NUM0", "KEY_NUM1", "KEY_NUM2", "KEY_NUM3", "KEY_NUM4",
//	        "KEY_NUM5", "KEY_NUM6", "KEY_NUM7", "KEY_NUM8", "KEY_NUM9",
//	        "KEY_POUND", "KEY_STAR"};
//	static int[] gameActions = {LEFT, RIGHT, FIRE};
//	static String[] gameNames = {"LEFT", "RIGHT", "FIRE"};
	private BeerCell controller;
	private Settings settings;
	//private Image beerIcon;
	Image winImage;
	
	public StatsScreen(BeerCell controller, Settings settings) {
		this.controller = controller;
		this.settings = settings;
	}
	
//	protected void keyPressed(int keyCode) {
//		if ((keyCode == -5) || (keyCode == FIRE) || (keyCode == -8) || (keyCode == 10)) {
//			
//		}
//	}
	
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
		g.setColor(0x00FFFFFF);
		g.fillRect(0, 0, getWidth(), getHeight());

		//g.drawImage(winImage, 0, 0, Graphics.TOP | Graphics.LEFT);
		g.setColor(0x00000000);
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		g.drawString("You have won "+settings.getGamesWon()+" games.", this.getWidth()/20, this.getHeight()/6, Graphics.TOP | Graphics.LEFT);
		g.drawString("You have abandoned "+settings.getGamesAbandoned()+" games.", this.getWidth()/20, this.getHeight()/6*2, Graphics.TOP | Graphics.LEFT);
		g.drawString("Average win time: "+renderTime(settings.getAvgTime()), this.getWidth()/20, this.getHeight()/6*3, Graphics.TOP | Graphics.LEFT);
		g.drawString("Minimum win time: "+renderTime(settings.getMinTime()), this.getWidth()/20, this.getHeight()/6*4, Graphics.TOP | Graphics.LEFT);
		g.drawString("Maximum win time: "+renderTime(settings.getMaxTime()), this.getWidth()/20, this.getHeight()/6*5, Graphics.TOP | Graphics.LEFT);
	}
}
