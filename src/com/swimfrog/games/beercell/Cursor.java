package com.swimfrog.games.beercell;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Cursor extends Canvas {
	Coordinate offset = new Coordinate(0,0);	//Describes the offset relative to the card's base.
	int state = CursorState.NORMAL;
	Image image[] = new Image[2];
	Stack stack;
	
	public Cursor(Stack stack) {
		this.stack = stack;
		
		initialize();
	}
	
	public void initialize() {
		try {
			//Read in the normal cursor image.
			image[CursorState.NORMAL] = Image.createImage("/cursor.png");
		} catch (IOException e) {
			image[CursorState.NORMAL] = Image.createImage(16, 16);
			Graphics ig = image[CursorState.NORMAL].getGraphics();
			ig.drawString("?", 0, 0, Graphics.TOP|Graphics.LEFT);
		}
		
		try {
			//Read in the grabbed cursor image.
			image[CursorState.GRAB] = Image.createImage("/cursor.png");
		} catch (IOException e) {
			image[CursorState.GRAB] = Image.createImage(16, 16);
			Graphics ig = image[CursorState.GRAB].getGraphics();
			ig.drawString("?", 0, 0, Graphics.TOP|Graphics.LEFT);
		}
	}
	
	protected void paint(Graphics g) {
		//Draw the cursor over the currently-selected stack
//		if ( selectedStack != null) {
//			Coordinate selectedBase = selectedStack.getTopCard().getBase();
//			g.drawImage(cursor, selectedBase.getX(), selectedBase.getY(), Graphics.BOTTOM|Graphics.HCENTER);
//		}
		if ( stack != null) {
				Coordinate selectedBase = new Coordinate(0,0);
			if (stack instanceof PlayStack) {
				if (stack.hasCards()) {
					selectedBase = (stack).getTopCard().getBase();
				} else {
					selectedBase = (stack).getBase();
				}
			} else if ((stack instanceof CellStack) || (stack instanceof HomeStack)) {
				selectedBase = (stack).getBase();
			}
			g.drawImage(image[state], selectedBase.getX()+offset.getX(), selectedBase.getY()+offset.getY(), Graphics.HCENTER|Graphics.VCENTER);
			
		}
	}
	
	public void setOffset(int x, int y) {
		offset.setX(x);
		offset.setY(y);
	}
	
	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public String toString() {
		return "Offset:"+offset.toString()+" State:"+state+" Location:"+(stack).getTopCard().getBase();
	}
	
	//public Cursor (int offsetX, int offsetY) {
	//	offset = new Coordinate(offsetX, offsetY);
	//}
}
