package com.swimfrog.games.beercell;

import java.util.*;
import javax.microedition.lcdui.*;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public class PlayStack extends Canvas implements Stack {
	private static class PUSHPOPMODE {
		static final boolean TEST = true;
		static final boolean REAL = false;
	}
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
	
	public void deal(Card card) { //TODO: Should probably be enforced by dealing cards at construction time.
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
		return (Card) cards.lastElement();
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
			g.fillRect(base.getX(), base.getY(), width, cardHeight);
			g.setColor(0x00000000);
			g.drawRect(base.getX(), base.getY(), width, cardHeight);
		} else {
			int cardDelta = ((height - cardHeight) / cards.size());
			if (cardDelta > maxVSpacing) {
				cardDelta = maxVSpacing;
			}
			for (int i=0; i < cards.size(); i++) {
				Card theCard = (Card) cards.elementAt(i);
				int cardy = cardDelta * i;
				theCard.setPosition(this.base.getX(), this.base.getY() + cardy);
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
	
	private void push(Card card, boolean testmode) throws AgainstTheRulesException {
		if (cards.size() > 0) {
			int newColor = Suit.getColor(card.getSuit());
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

	public void setCardHeight (int cardHeight) {
		this.cardHeight = cardHeight;
		this.maxVSpacing = cardHeight/2;
	}
	
	public void unselect() throws AgainstTheRulesException {
		if (cards.size() > 0) {
			getTopCard().unselect();
		} else {
			throw new AgainstTheRulesException("No card to unselect");
		}
	}
}
