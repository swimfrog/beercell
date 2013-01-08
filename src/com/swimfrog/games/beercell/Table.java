package com.swimfrog.games.beercell;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.swimfrog.games.beercell.exceptions.*;
import java.util.*;

public class Table extends Canvas {
	private BeerCell controller;
	private final int PLAYSTACKLENGTH = 8;
	private final int CELLSTACKLENGTH = 4;
	private final int HOMESTACKLENGTH = 4;
	private Stack stacks[] = new Stack[8 + 4 + 4]; // 8 Play, 4 Cell, 4 Home
	private int tableColor = 0x0033CC33;
	private final int width = getWidth();
	private final int height = getHeight();
	private final int marginLeft, marginRight, marginTop, marginBottom;
	private final int hSpacingPlay, hSpacingCell, hSpacingHome, vSpacing;
	private final int stackWidth, cardHeight;
	private Cursor cursor;
    static int[] keyCodes = {KEY_NUM0, KEY_NUM1, KEY_NUM2, KEY_NUM3, KEY_NUM4,
        KEY_NUM5, KEY_NUM6, KEY_NUM7, KEY_NUM8, KEY_NUM9,
        KEY_POUND, KEY_STAR};
	static String[] keyNames = {"KEY_NUM0", "KEY_NUM1", "KEY_NUM2", "KEY_NUM3", "KEY_NUM4",
	        "KEY_NUM5", "KEY_NUM6", "KEY_NUM7", "KEY_NUM8", "KEY_NUM9",
	        "KEY_POUND", "KEY_STAR"};
	static int[] gameActions = {LEFT, RIGHT, FIRE};
	static String[] gameNames = {"LEFT", "RIGHT", "FIRE"};
	int cursorLocation = 0;
	boolean pointer;
	int pointerX = 0;
	int pointerY = 0;
	Vector moveHistory = new Vector();

	
	private int getSelectionLocation() {
		for (int i=0; i < stacks.length; i++) {
			if (stacks[i].isSelected()) {
				return i;
			}
			
		}
		return -1;
	}
	
	protected void keyPressed(int keyCode) {
		int selectionLocation = getSelectionLocation();
		
		// Selection buttons
		if ((keyCode == KEY_NUM4) || (keyCode == -3) || (keyCode == LEFT)) {
			//NUM4, left, bbtrackball left
			gotoPrevious();
		} else if ((keyCode == KEY_NUM6) || (keyCode == -4) || (keyCode == RIGHT)) {
			//NUM5, right, bbtrackball right
			gotoNext();
			
		// Action buttons
		} else if ((keyCode == -5) || (keyCode == FIRE) || (keyCode == -8) || (keyCode == 10)) {
			//Emulator OK, FIRE, bbtrackball click, keyboard return
			if (selectionLocation > -1) {
				if (selectionLocation == cursorLocation) {
					// Selecting the same card that's already selected
					try {
						//Toggle selection
						stacks[selectionLocation].unselect();
					} catch (Exception e) {
						controller.alert(e.toString());
					}
				} else {
					try {
						//Create the move and execute it. 
						Move move = new Move(selectionLocation, cursorLocation, this);
						move.execute();
						if (stacks[selectionLocation].hasCards()) {
							stacks[selectionLocation].unselect();
						}
					} catch (AgainstTheRulesException e) {
						//If no solution is found, then complain.
						controller.alert(e.toString());
					} catch (NoFreeCellException e) {
						//If the solver couldn't find enough cells, then complain.
						controller.alert(e.toString());
					} finally {
						try {
							//And make sure nothing is left selected
							selectionLocation = getSelectionLocation();
							if (selectionLocation > -1) {
								stacks[selectionLocation].unselect();
							}
						} catch (Exception e) {
							//And if that fails, complain
							controller.alert(e.toString());
						}
					}
				}
				this.repaint();
			} else {
				try {
					// Select the card under the cursor
					stacks[cursorLocation].select();
				} catch (Exception e) {
					//And if that fails, complain
					controller.alert(e.toString());
				}
				repaint();					
			}
		} else if ((keyCode == UP) || (keyCode == -1)) {
			//UP, Emulator UP
			try {
				//Find a free cell and if found, create a move and execute.
				int freeCell = getFreeCell();
				Move move = new Move(selectionLocation, freeCell, this);
				move.execute();
				if (stacks[selectionLocation].hasCards()) {
					stacks[selectionLocation].unselect();
				}
			} catch (AgainstTheRulesException e) {
				//If no solution is found, then complain.
				controller.alert(e.toString());
			} catch (NoFreeCellException e) {
				//If the solver couldn't find enough cells, then complain.
				controller.alert(e.toString());
			} finally {
				try {
					//And make sure nothing is left selected
					selectionLocation = getSelectionLocation();
					if (selectionLocation > -1) {
						stacks[selectionLocation].unselect();
					}
				} catch (Exception e) {
					//And if that fails, complain
					controller.alert(e.toString());
				}
			}
			
		// Other buttons
		} else {
			//DEBUG
			//controller.alert("KeyCode:"+keyCode+"\nCursor:"+cursor.toString());
		}
	}
	
	protected void keyRepeated(int keyCode) {
		//
	}
	
	protected void keyReleased(int keyCode) {
		//
	}    
	
//	protected void pointerPressed(int x, int y) {
//		pointerX = x;
//		pointerY = y;
//		pointer = true;
//		repaint();
//	}
//	
//	protected void pointerDragged(int x, int y) {
//		pointerX = x;
//		pointerY = y;
//		pointer = true;
//		repaint();
//	}
//	
//	protected void pointerReleased(int x, int y) {
//		pointer = false;
//		repaint();
//	}
	
	private void gotoNext() {
		if (cursorLocation == stacks.length-1) {
			cursorLocation = 0;
		} else {
			cursorLocation++;
		}
		cursor.setStack(stacks[cursorLocation]);
		//controller.alert("KeyCode:"+keyCode+"\nCursor:"+cursor.toString());
		repaint();
	}
	
	private void gotoPrevious() {
		if (cursorLocation == 0) {
			cursorLocation = stacks.length-1;
		} else {
			cursorLocation--;
		}		
		cursor.setStack(stacks[cursorLocation]);
		//controller.alert("KeyCode:"+keyCode+"\nCursor:"+cursor.toString());
		repaint();
	}
	
	Table(BeerCell controller) {
		this.controller = controller;
		// Work out where all of the stacks should go
		// 5% margins
		marginLeft = (int) (width * .05);
		marginRight = (int) (width * .05);
		marginTop = (int) (height * .05);
		marginBottom = (int) (height * .05);
		
		// Other Spacings
		hSpacingPlay = (int) (width * .03);
		hSpacingCell = (int) (width * .01);
		hSpacingHome = (int) (width * .01);
		vSpacing = (int) (height * .05);
		//cSpacing = (int) (width * .10);
		
		// The rest of the space can be used for stacks
		stackWidth = ((width - (marginLeft + marginRight + (hSpacingPlay * 7))) / 8);
		cardHeight = (int) (stackWidth * 1.33);
		
//		// Create the stacks, calculating their base screen position as we go along.
//		int playStackTopMargin = marginTop + cardHeight + vSpacing;
//		for (int i=0; i < play.length; i++) {
//			PlayStack theStack = new PlayStack((marginLeft + ((stackWidth + hSpacingPlay) * i)), playStackTopMargin, stackWidth, (height - playStackTopMargin - marginBottom));
//			theStack.setCardHeight(cardHeight);
//			play[i] = theStack;
//			
//		}
//		for (int i=0; i < cell.length; i++) {
//			cell[i] = new CellStack((marginLeft + ((stackWidth + hSpacingCell) * i)), marginTop, stackWidth, cardHeight);
//		}
//		for (int i=0; i < home.length; i++) {
//			Coordinate startingPoint = new Coordinate(width - (marginRight + ((stackWidth + hSpacingHome) * home.length)), marginTop);
//			home[i] = new HomeStack(startingPoint.getX() + ((stackWidth + hSpacingHome) * i), startingPoint.getY(), stackWidth, cardHeight);
//		}
		
		// Create the stacks, calculating their base screen position as we go along.
		int playStackTopMargin = marginTop + cardHeight + vSpacing;
		for (int i=0; i < stacks.length; i++) {
			if (i < PLAYSTACKLENGTH) { //Play stacks
				PlayStack theStack = new PlayStack((marginLeft + ((stackWidth + hSpacingPlay) * i)), playStackTopMargin, stackWidth, (height - playStackTopMargin - marginBottom));
				theStack.setCardHeight(cardHeight);
				stacks[i] = theStack;
			} else if ((i >= PLAYSTACKLENGTH) && (i < PLAYSTACKLENGTH + CELLSTACKLENGTH)) { //Cell stacks
				stacks[i] = new CellStack((marginLeft + ((stackWidth + hSpacingCell) * (i - PLAYSTACKLENGTH))), marginTop, stackWidth, cardHeight);
			} else { //Home Stacks
				Coordinate startingPoint = new Coordinate(width - (marginRight + ((stackWidth + hSpacingHome) * 4)), marginTop);
				stacks[i] = new HomeStack(startingPoint.getX() + ((stackWidth + hSpacingHome) * (i - PLAYSTACKLENGTH - CELLSTACKLENGTH)), startingPoint.getY(), stackWidth, cardHeight);
			}	
		}
		
		populatePlayArea(new Deck(stackWidth, cardHeight));
		
		//Start the cursor off at play[0]
		cursor = new Cursor(stacks[cursorLocation]);
		cursor.setOffset(stackWidth/2, cardHeight/2);
	}
	
	public void testRandomMovements() {
		try {
			Random generator = new Random();
			int fromStack=0;
			int toStack=0;
			
			//Make sure we don't go from/to the same stack.
			while (fromStack == toStack) {
				fromStack = generator.nextInt(stacks.length);
				toStack = generator.nextInt(stacks.length);
			}
			
			stacks[toStack].push(stacks[fromStack].pop());
		} catch (Exception e) {
			controller.alert(e.toString());
		}
		this.repaint();
	}
	
	public int getCardHeight() {
		return cardHeight;
	}
	
	private void populatePlayArea(Deck deck) {
		// Retrieve cards from the deck and populate the play area.
		int stackNum = 0;
		int deckSize = deck.length();
		for (int i=0; i < deckSize; i++ ) {
			try {
				Card tempCard = deck.popRandom();
				stacks[stackNum].deal(tempCard);
			} catch (Exception e) {
				controller.alert(e.toString());
			}
			if (stackNum == 7) {
				stackNum=0;
			} else {
				stackNum++;
			}
		}
		// Now the deck is empty.
	}
	
	public void unmove(Move move) {
		try {
			Card theCard = stacks[move.getFrom()].getTopCard();
			stacks[move.getFrom()].deal(theCard);
			stacks[move.getTo()].pop();
		} catch (Exception e) {
			controller.alert("Can't do it.");
		}
	}

	public void move(Move move) throws AgainstTheRulesException, NoFreeCellException {
		Card theCard = null;
		try {
			theCard = stacks[move.getFrom()].pop();
			stacks[move.getTo()].push(theCard);
		} catch (AgainstTheRulesException e1) {
			if (! theCard.equals(null)) {
				//If we managed to pop the card, deal it back.
				stacks[move.getFrom()].deal(theCard);
			}
			//TODO: If this move is already part of a movemacro, then we don't want to keep trying to solve it.
			Move newMove = solveMove(move);
			newMove.execute();
			//Add the move to the moveHistory
			moveHistory.addElement(newMove);
			//TODO: Add some functionality for cleaning up old moves.
		}
		this.repaint();
	}
	
	private int getFreeCell() throws NoFreeCellException {
		for (int i=PLAYSTACKLENGTH; i < PLAYSTACKLENGTH+CELLSTACKLENGTH; i++) {
			if (stacks[i].hasCards()) {
				continue;
			} else {
				return i;
			}
		}
		// If we get here, then there are no free cells, so throw error.
		throw new NoFreeCellException();
	}
	
	private Move solveMove(Move move) throws NoFreeCellException {
		MoveMacro newMove = new MoveMacro();
			Iterator stackIterator = stacks[move.getFrom()].createIterator();
			while (stackIterator.hasNext()) {
			Card theCard = (Card) stackIterator.next();
				// if this card can be moved, then create the move and end
				//stacks[move.getTo()].push(theCard);
				if (stacks[move.getTo()].canPush(theCard)) {
					newMove.addMove(move);
					break;
				} else {
					// if it can't be moved directly, then get a free cell and create a move to the free cell.
					int cell = getFreeCell();
					newMove.addMove(new Move(move.getFrom(), cell, this));
				} //If we get a NoFreeCellException here, it's passed back and newMove gets gc'ed.
			}
			// Now, go back through the moves and move everything to the destination stack in reverse order.
			Iterator moveIterator = newMove.createIterator();
			moveIterator.previous(); //Skip the very last move, we don't need to repeat that.
			while (moveIterator.hasPrevious()) {
				Move theMove = (Move) moveIterator.previous();
				newMove.addMove(new Move(theMove.getTo(), move.getTo(), this));
			}
		return newMove;
	}

	
	protected void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        // Create a green background
        g.setColor(tableColor);
        g.fillRect(0, 0, width, height);
        
        
        // Ask each stack to paint itself.
        // PlayStack
		for (int i=0; i < stacks.length; i++) {
			stacks[i].paint(g);
		}
		
		//Draw some fancies
		g.setColor(0x00FFFFFF);
		g.drawLine(width/2, marginTop, width/2, marginTop+cardHeight);
		
		//Draw the cursor over the currently-selected stack
		cursor.paint(g);
	}
}
