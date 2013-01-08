package com.swimfrog.games.beercell;

import java.util.*;
import javax.microedition.lcdui.*;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public interface Stack {
	Vector cards = new Vector();
	Coordinate base = new Coordinate(0,0);
	int width = 0;
	
	public void deal(Card card);
	
	void push(Card card) throws AgainstTheRulesException;
	
	Card pop() throws AgainstTheRulesException;
	boolean canPop();
	boolean canPush(Card card);
	
	void paint(Graphics g);
	
	int getHeight();
	
	Coordinate getBase();
	
	Card getTopCard();
	Card getCardAt(int location);
	int getSize();
	boolean hasCards();
	Iterator createIterator();
	
	void select() throws AgainstTheRulesException;
	
	void unselect() throws AgainstTheRulesException;
	
	boolean isSelected();
}
