package com.swimfrog.games.beercell;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class WinScreen extends Canvas {
//	static int[] keyCodes = {KEY_NUM0, KEY_NUM1, KEY_NUM2, KEY_NUM3, KEY_NUM4,
//        KEY_NUM5, KEY_NUM6, KEY_NUM7, KEY_NUM8, KEY_NUM9,
//        KEY_POUND, KEY_STAR};
//	static String[] keyNames = {"KEY_NUM0", "KEY_NUM1", "KEY_NUM2", "KEY_NUM3", "KEY_NUM4",
//	        "KEY_NUM5", "KEY_NUM6", "KEY_NUM7", "KEY_NUM8", "KEY_NUM9",
//	        "KEY_POUND", "KEY_STAR"};
//	static int[] gameActions = {LEFT, RIGHT, FIRE};
//	static String[] gameNames = {"LEFT", "RIGHT", "FIRE"};
	private BeerCell controller;
	//private Image beerIcon;
	Image winImage;
	
	public WinScreen(BeerCell controller) {
		this.controller = controller;
		
		try {
			winImage = Image.createImage("/beer_coupon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ( winImage.getWidth() != this.getWidth()) {
			//If the image doesn't match the screen size, resize it.
			int newHeight = winImage.getHeight() * this.getWidth() / winImage.getWidth();
			winImage = resizeImage(winImage, this.getWidth(), newHeight);
		}
	}
	
	private Image resizeImage(Image image, int newWidth, int newHeight) {
		int srcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		Image newImage = Image.createImage(newWidth, newHeight);
		Graphics g = newImage.getGraphics();

		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				g.setClip(x, y, 1, 1);
				int dx = x * srcWidth / newWidth;
				int dy = y * srcHeight / newHeight;
				g.drawImage(image, x - dx, y - dy,
				Graphics.LEFT | Graphics.TOP);
			}
		}

		Image immutableImage = Image.createImage(newImage);

		return immutableImage;
	}

	
//	protected void keyPressed(int keyCode) {
//		if ((keyCode == -5) || (keyCode == FIRE) || (keyCode == -8) || (keyCode == 10)) {
//			
//		}
//	}

	protected void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(0x0033CC33);
		g.fillRect(0, 0, getWidth(), getHeight());
/*		g.setColor(0xFF000000);
		g.setStrokeStyle(Graphics.DOTTED);
		int marginX = getWidth()/16;
		int marginY = getHeight()/16;
		g.drawRect(marginX, marginY, getWidth()-(marginX*2), getHeight()-(marginY*2));
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.drawString("COUPON", getWidth()/2, marginY*2, Graphics.LEFT|Graphics.TOP);
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		g.drawString("You win a free beer!", getWidth()/2, marginY*4, Graphics.LEFT|Graphics.TOP);
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL));
		g.drawString("Clip this coupon to redeem. Offer not valid anywhere.", marginX, getHeight()-(marginY*3), Graphics.LEFT|Graphics.TOP);
		g.drawString("Limit one per customer.", marginX, getHeight()-(marginY*2), Graphics.LEFT|Graphics.TOP);
		g.drawImage(beerIcon, getWidth()/8, (getHeight()/2)-(beerIcon.getHeight()/2), Graphics.LEFT|Graphics.TOP);*/

		g.drawImage(winImage, 0, 0, Graphics.TOP | Graphics.LEFT);
	}
}
