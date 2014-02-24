package com.swimfrog.games.beercell;

import java.util.Vector;

import com.swimfrog.games.beercell.exceptions.*;


public class MoveMacro extends Move {
	
	Vector actions = new Vector();
	
	MoveMacro() {
		super(0,0, null); //Dummy values, not used.
	}

	MoveMacro(int from, int to, Table table) {
		super(from, to, table);
	}
	
	public void addMove(Move move) {
		actions.addElement(move);
	}
	
	public Iterator createIterator() {
		return new MoveMacroIterator(this);
	}
	
	public void execute() throws AgainstTheRulesException, NoFreeCellException, InvalidCardException {
		for (int i=0; i < actions.size(); i++) {
			((Move) actions.elementAt(i)).execute();
		}
		//getTable().addToHistory(this);
	}
	
	public void addToHistory() {
		getTable().addToHistory(this);
	}
	
	public Move getMoveAt(int location) {
		return (Move) actions.elementAt(location);
	}
	
	public int getSize() {
		return actions.size();
	}
	
	public void undo() {
		for (int i=actions.size()-1; i >= 0; i--) {
			((Move) actions.elementAt(i)).undo();
		}
	}
}
