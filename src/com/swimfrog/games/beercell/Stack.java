package com.swimfrog.games.beercell;

import java.util.*;
import javax.microedition.lcdui.*;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public interface Stack {
	Vector cards = new Vector();
	Coordinate base = new Coordinate(0,0);
	int width = 0;
	
	boolean canPop();
	
	boolean canPush(Card card);
	
	Iterator createIterator();
	public void deal(Card card);
	Coordinate getBase();
	
	Card getCardAt(int location);
	
	int getHeight();
	
	int getSize();
	
	Card getTopCard();
	boolean hasCards();
	boolean isSelected();
	void paint(Graphics g);
	Card pop() throws AgainstTheRulesException;
	
	void push(Card card) throws AgainstTheRulesException;
	
	void select() throws AgainstTheRulesException;
	
	void unselect() throws AgainstTheRulesException;
}
