package com.swimfrog.games.beercell;

import java.util.*;
import javax.microedition.lcdui.*;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public class HomeStack extends Canvas implements Stack {
	private static class PUSHPOPMODE {
		static final boolean TEST = true;
		static final boolean REAL = false;
	}
	Vector cards = new Vector();
	Coordinate base = new Coordinate(0,0);
	int width = 0;
	
	int height = 0;
	
	HomeStack(int baseX, int baseY, int width, int height) {
		this.base.setX(baseX);
		this.base.setY(baseY);
		this.width = width;
		this.height = height;
	}

	public boolean canPop() {
		try {
			pop(PUSHPOPMODE.TEST);
		} catch (AgainstTheRulesException e) {
			return false;
		}
		return true;
	}

	public boolean canPush(Card card) {
		try {
			push(card, PUSHPOPMODE.TEST);
		} catch (AgainstTheRulesException e) {
			return false;
		}
		return true;
	}
	
	public Iterator createIterator() {
		return new StackIterator(this);
	}
	
	public void deal(Card card) {
		cards.addElement(card);
	}
	
	public Coordinate getBase() {
		return this.base;
	}
	
	public Card getCardAt(int location) {
		return (Card) cards.elementAt(location);
	}

	public int getHeight() {
		return this.height;
	}
	
	public int getSize() {
		return cards.size();
	}

	public Card getTopCard() {
		return (Card) cards.elementAt(cards.size()-1);
	}
	
	public boolean hasCards() {
		if (cards.size() > 0) {
			return true;
		} else {
			return false;
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
			g.fillRect(base.getX(), base.getY(), width, height);
			g.setColor(0x00000000);
			g.drawRect(base.getX(), base.getY(), width, height);
		} else {
			for (int i=0; i < cards.size(); i++) {
				Card theCard = (Card) cards.elementAt(i);
				theCard.setPosition(base.getX(), base.getY());
				theCard.paint(g);
			}
		}
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
	
	public void push(Card card) throws AgainstTheRulesException {
		push(card, PUSHPOPMODE.REAL);
	}
	
	public void push(Card card, boolean testmode) throws AgainstTheRulesException {
		if (cards.size() > 0) {
			if ((card.getSuit() == this.getTopCard().getSuit()) && (card.getValue() == this.getTopCard().getValue()+1)) {
				if (testmode == PUSHPOPMODE.REAL) {
					cards.addElement(card);
				}
			} else {
				throw new AgainstTheRulesException("You can only place the next card in this suit on this stack.");
			}
		} else {
			if (card.getValue() == Rank.ACE) {
				if (testmode == PUSHPOPMODE.REAL) {
					cards.addElement(card);
				}
			} else {
				throw new AgainstTheRulesException("Only an ace can be placed in a blank home stack.");
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

}
