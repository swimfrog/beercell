package com.swimfrog.games.beercell;

import java.util.*;
import javax.microedition.lcdui.*;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public class PlayStack extends Canvas implements Stack {
	Vector cards = new Vector();
	int cardHeight = 25;
	int maxVSpacing;
	Coordinate base = new Coordinate(0,0);
	int width = 0;
	int height = 0;
	
	PlayStack(int baseX, int baseY, int width, int height) {
		this.base.setX(baseX);
		this.base.setY(baseY);
		this.width = width;
		this.height = height;
	}
	
	public Card getTopCard() {
		return (Card) cards.lastElement();
	}
	
	public Card getCardAt(int location) {
		return (Card) cards.elementAt(location);
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public Iterator createIterator() {
		return new StackIterator(this);
	}
	
	public boolean hasCards() {
		if (cards.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setCardHeight (int cardHeight) {
		this.cardHeight = cardHeight;
		this.maxVSpacing = cardHeight/3;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean canPop() {
		try {
			pop(PUSHPOPMODE.TEST);
		} catch (AgainstTheRulesException e) {
			return false;
		}
		return true;
	}

	public Card pop() throws AgainstTheRulesException {
		return pop(PUSHPOPMODE.REAL);
	}
	
	private Card pop(boolean testmode) throws AgainstTheRulesException {
		if (cards.size() > 0) {
			if (testmode == PUSHPOPMODE.REAL) {
				Card theCard = getTopCard();
				cards.removeElement(theCard);
				return theCard;
			} else {
				return null;
			}
		} else {
			throw new AgainstTheRulesException("No card to pop");
		}
	}
	
	public void deal(Card card) { //TODO: Should probably be enforced by dealing cards at construction time.
		cards.addElement(card);
	}
	
	public boolean canPush(Card card) {
		try {
			push(card, PUSHPOPMODE.TEST);
		} catch (AgainstTheRulesException e) {
			return false;
		}
		return true;
	}
	
	private static class PUSHPOPMODE {
		static final boolean TEST = true;
		static final boolean REAL = false;
	}

	public void push(Card card) throws AgainstTheRulesException {
		push(card, PUSHPOPMODE.REAL);
	}
	
	private void push(Card card, boolean testmode) throws AgainstTheRulesException {
		if (cards.size() > 0) {
			int newColor = Suit.getColor(card.getSuit());
			
			//TODO: For some reason, when doing a movemacro, the wrong colors get evaluated... something wrong with this...
			Card oldCard = this.getTopCard();
			int oldColor = Suit.getColor(oldCard.getSuit());
			if ((newColor != oldColor) && (card.getValue() == this.getTopCard().getValue()-1)) {
				if (testmode == PUSHPOPMODE.REAL) {
					cards.addElement(card);
				}
			} else {
				throw new AgainstTheRulesException("You can only place the previous card in alternating suit on this stack.");
			}
		} else {
			if (testmode == PUSHPOPMODE.REAL) {
				cards.addElement(card);
			}
		}
	}
	
	public void select() throws AgainstTheRulesException {
		if (cards.size() > 0) {
			getTopCard().select();
		} else {
			throw new AgainstTheRulesException("No card to select");
		}
	}
	
	public void unselect() throws AgainstTheRulesException {
		if (cards.size() > 0) {
			getTopCard().unselect();
		} else {
			throw new AgainstTheRulesException("No card to unselect");
		}
	}
	
	public boolean isSelected() {
		if (cards.size() > 0) {
			return getTopCard().isSelected();
		} else {
			return false;
		}
	}

	public void paint(Graphics g) {
		if (cards.size() == 0) {
			g.setColor(0x0022BB22);
			g.fillRect(base.getX(), base.getY(), width, cardHeight);
			g.setColor(0x00000000);
			g.drawRect(base.getX(), base.getY(), width, cardHeight);
		} else {
			for (int i=0; i < cards.size(); i++) {
				Card theCard = (Card) cards.elementAt(i);
				int y = (this.base.getY() + (height - cardHeight) / cards.size() * i);
				//But don't go over 1/4 card shown underneath. This makes short stacks line up better.
				if (y > maxVSpacing) {
					y = (this.base.getY() + (maxVSpacing * i));
				}
				theCard.setPosition(this.base.getX(), y);
				theCard.paint(g);
			}
		}
	}
	
	public Coordinate getBase() {
		return this.base;
	}
}
