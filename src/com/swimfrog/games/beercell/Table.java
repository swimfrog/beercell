package com.swimfrog.games.beercell;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.swimfrog.games.beercell.exceptions.*;

import java.util.*;

public class Table extends Canvas {
	class FreeCellAllocator {
		private boolean[] num = new boolean[CELLSTACKLENGTH];
		FreeCellAllocator() {
			//Initialize this temporary object by memorizing the current cell states.
			for (int i=PLAYSTACKLENGTH; i < PLAYSTACKLENGTH+CELLSTACKLENGTH; i++) {
				if (stacks[i].hasCards()) {
					num[i-PLAYSTACKLENGTH]=true;
				}
			}
		}
		public int getFreeCell() throws NoFreeCellException {
			for (int i=0; i < num.length; i++) {
				if (num[i]) {
					continue;
				} else {
					num[i]=true; //Mark this as "taken" for this move.
					return i + PLAYSTACKLENGTH;
				}
			}
			// If we get here, then there are no more free cells, so throw error.
			throw new NoFreeCellException();
		}
		public int getNumFreeCells() {
			int freecells = 0;
			for (int i=0; i < num.length; i++) {
				if ( ! num[i]) {
					freecells++;
				}
			}
			return freecells;
		}
	}
	private final int PLAYSTACKLENGTH = 8;
	private final int CELLSTACKLENGTH = 4;
	private final int HOMESTACKLENGTH = 4;
	private Stack stacks[] = new Stack[PLAYSTACKLENGTH + CELLSTACKLENGTH + HOMESTACKLENGTH];
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
	private BeerCell controller;
	TimerStrip timerStrip;
	
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
		
		timerStrip = new TimerStrip(this, 0, this.getHeight()-marginBottom, this.getWidth(), this.getHeight());
	}
	
	public int getTimerValue() {
		return timerStrip.getValue();
	}
	
	public void addToHistory(Move move) {
		//Add the move to the moveHistory so we can undo it later, if requested.
		moveHistory.addElement(move);
	}
	
	public void autoHome() {
		boolean found = true; //Set once for the beginning.
		while (found) { // As long as we found something on a previous pass, keep trying.
			found = false;
			//TODO: Search cellstacks as well. I got arrayIndexOutOfBounds before, because it overstepped some boundary.
			for (int i=0; i < PLAYSTACKLENGTH; i++) {
				if (stacks[i].hasCards()) {
					Card theCard = stacks[i].getTopCard();
					int homeStack = findHome(theCard);
					if (homeStack > -1) {
						found = true;
						Move move = new Move(i, homeStack, this);
						try {
							move.execute();
							addToHistory(move);
						} catch (Exception e) {
							controller.alert(e.getMessage());
						}
					}
				}
			}
		}
	}
	
	public void pause() {
		timerStrip.stop();
	}
	
	public void resume() {
		timerStrip.start();
	}
	
	public void cheat() {
		/*
		 * Move all cards to the homestacks, avoiding the rules
		 */
		try {
			int baseStack=PLAYSTACKLENGTH+CELLSTACKLENGTH;
			for (int x=baseStack; x < baseStack+HOMESTACKLENGTH; x++) {
				int y=stacks[x].getSize();
				int stack = 0;
				while (y < 13) {
					if (stacks[stack].hasCards()) {
						Card theCard = stacks[stack].pop();
						stacks[x].deal(theCard);
						y++;
					} else {
						stack++;
					}
				}
			}
		} catch (Exception e) {
			//Ignore all exceptions. We're cheating here.
		}
		checkWin();
	}
	
	public void checkWin() {
		int homestack=PLAYSTACKLENGTH+CELLSTACKLENGTH;
		for (int i=homestack; i < homestack+HOMESTACKLENGTH; i++) {
			if (stacks[i].getSize() < 13) {
				return;
			}
		}
		controller.winGame();
	}
	
	public void doMove(Move move) throws AgainstTheRulesException, NoFreeCellException, InvalidCardException {
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
			addToHistory(move);
		}
		this.repaint();
	}
	
	private int findHome(Card card) {
		for (int i=PLAYSTACKLENGTH+CELLSTACKLENGTH; i < PLAYSTACKLENGTH+CELLSTACKLENGTH+HOMESTACKLENGTH; i++) {
			if (stacks[i].canPush(card)) {
				return i;
			}
		}
		return -1;
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
	
	public Card getCardAt(int i) throws InvalidCardException {
		if (stacks[i].hasCards()) {
			return stacks[i].getTopCard();
		} else {
			throw new InvalidCardException("There are no cards in this stack.");
		}
	}
	
	public int getCardHeight() {
		return cardHeight;
	}
	
	private int getSelectionLocation() {
		for (int i=0; i < stacks.length; i++) {
			if (stacks[i].isSelected()) {
				return i;
			}
			
		}
		return -1;
	}
	
	public Stack getStackAt(int i) {
		return (Stack) stacks[i];
	}
	
	private void gotoLocation(int loc) {
		cursorLocation = loc;
		cursor.setStack(stacks[cursorLocation]);
		//controller.alert("KeyCode:"+keyCode+"\nCursor:"+cursor.toString());
		repaint();
	}
	
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
						controller.alert(e.getMessage());
					}
				} else {
					try {
						//Create the move and execute it. 
						Move move = new Move(selectionLocation, cursorLocation, this);
						move.execute();
						addToHistory(move);
						if (stacks[selectionLocation].hasCards()) {
							stacks[selectionLocation].unselect();
						}
					} catch (AgainstTheRulesException e) {
						//If no solution is found, then complain.
						controller.alert(e.getMessage());
					} catch (NoFreeCellException e) {
						//If the solver couldn't find enough cells, then complain.
						controller.alert(e.getMessage());
					} catch (InvalidCardException e) {
						controller.alert(e.getMessage());
					} finally {
						try {
							//And make sure nothing is left selected
							selectionLocation = getSelectionLocation();
							if (selectionLocation > -1) {
								stacks[selectionLocation].unselect();
							}
						} catch (Exception e) {
							//And if that fails, complain
							controller.alert(e.getMessage());
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
					controller.alert(e.getMessage());
				}
				repaint();					
			}
		} else if ((keyCode == UP) || (keyCode == -1)) {
			//UP, Emulator UP
			if (getSelectionLocation() == -1) {
				//Move the selection location to the top, for convenience.
				gotoLocation(PLAYSTACKLENGTH+CELLSTACKLENGTH-1);
				this.repaint();
			} else {
				//Move the currently-selected card home if possible, or to a free cell, if available.
				try {
					Card theCard = stacks[selectionLocation].getTopCard();
					int homeslot = findHome(theCard);
					if (homeslot > -1) {
						Move move = new Move(selectionLocation, homeslot, this);
						move.execute();
						addToHistory(move);
					} else {
						//Find a free cell and if found, create a move and execute.
						int freeCell = new FreeCellAllocator().getFreeCell();
						Move move = new Move(selectionLocation, freeCell, this);
						move.execute();
						addToHistory(move);
					}
					if (stacks[selectionLocation].hasCards()) {
						stacks[selectionLocation].unselect();
					}
				} catch (AgainstTheRulesException e) {
					//If no solution is found, then complain.
					controller.alert(e.getMessage());
				} catch (NoFreeCellException e) {
					//If the solver couldn't find enough cells, then complain.
					controller.alert(e.getMessage());
				} catch (InvalidCardException e) {
					controller.alert(e.getMessage());
				} finally {
					try {
						//And make sure nothing is left selected
						selectionLocation = getSelectionLocation();
						if (selectionLocation > -1) {
							stacks[selectionLocation].unselect();
						}
					} catch (Exception e) {
						//And if that fails, complain
						controller.alert(e.getMessage());
					}
				}
			
			}
			
		// Other buttons
		} else {
			//DEBUG
			//controller.alert("KeyCode:"+keyCode+"\nCursor:"+cursor.toString());
		}
	}
	
	protected void keyReleased(int keyCode) {
		//
	}
	
	protected void keyRepeated(int keyCode) {
		//
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
		
		//Update the timer strip.
		timerStrip.paint(g);
	}
	
	private void populatePlayArea(Deck deck) {
		// Retrieve cards from the deck and populate the play area.
/*
 * 	Used for debugging playstack drawing.
 * 
 * 		for (int i=0; i<13; i++) {
			Card tempCard = deck.popRandom();
			stacks[0].deal(tempCard);
		}
		for (int i=0; i<12; i++) {
			Card tempCard = deck.popRandom();
			stacks[1].deal(tempCard);
		}
		for (int i=0; i<11; i++) {
			Card tempCard = deck.popRandom();
			stacks[2].deal(tempCard);
		}
		for (int i=0; i<10; i++) {
			Card tempCard = deck.popRandom();
			stacks[3].deal(tempCard);
		}
		for (int i=0; i<6; i++) {
			Card tempCard = deck.popRandom();
			stacks[4].deal(tempCard);
		}*/
		
		int stackNum = 0;
		int deckSize = deck.length();
		for (int i=0; i < deckSize; i++ ) {
			try {
				Card tempCard = deck.popRandom();
				stacks[stackNum].deal(tempCard);
			} catch (Exception e) {
				controller.alert(e.getMessage());
			}
			if (stackNum == 7) {
				stackNum=0;
			} else {
				stackNum++;
			}
		}
		// Now the deck is empty.
	}

	private Move solveMove(Move move) throws NoFreeCellException, AgainstTheRulesException {
		MoveMacro newMove = new MoveMacro();
			Iterator stackIterator = stacks[move.getFrom()].createIterator();
			FreeCellAllocator allocator = new FreeCellAllocator();
			{
				//Find out if any move would be possible before we go to the trouble of creating a movemacro.
				boolean found = false;
				while (stackIterator.hasNext()) {
					Card theCard = (Card) stackIterator.next();
					if (stacks[move.getTo()].canPush(theCard)) {
						found = true;
						break;
					}
					/*
					 * TODO: A side effect of this is if the fromstack includes any
					 * card that could be moved, the move will allocate free cells
					 * until it runs out, even if the card is buried deep?
					 * //int freeCells = allocator.getNumFreeCells();
					 * //for (int i=0; i < freeCells; i++) {
					 */
				}
				if (! found) {
					throw new AgainstTheRulesException("There are no valid moves possible.");
				}
			}
			//Go back to the beginning.
			stackIterator.reset();
			while (stackIterator.hasNext()) {
				Card theCard = (Card) stackIterator.next();
				// if this card can be moved, then create the move and end
				if (stacks[move.getTo()].canPush(theCard)) {
					newMove.addMove(move);
					break;
				} else {
					// if it can't be moved directly, then get a free cell and create a move to the free cell.
					int cell = allocator.getFreeCell();
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
	

	public void testAnimation() {
		Animator animator = new Animator(null, new Coordinate(4,67), new Coordinate(145,120));
		
		System.err.println(animator.toString());
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
			controller.alert(e.getMessage());
		}
		this.repaint();
	}
	
	public void undo() {
		if (moveHistory.size() > 0) {
			unmove((Move) moveHistory.lastElement());
			moveHistory.removeElement(moveHistory.lastElement());			
		} else {
			controller.alert("You haven't made any moves yet!");
		}
		
	}
	
	public void unmove(Move move) {
		try {
			Card theCard = stacks[move.getTo()].getTopCard();
			stacks[move.getFrom()].deal(theCard);
			stacks[move.getTo()].pop();
		} catch (Exception e) {
			controller.alert("Sorry, I guess you're outta luck.");
		}
		this.repaint();
	}
}

