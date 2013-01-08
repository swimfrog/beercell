package com.swimfrog.games.beercell;

import java.util.Vector;

import com.swimfrog.games.beercell.exceptions.*;


public class MoveMacro extends Move {
	
	MoveMacro(int from, int to, Table table) {
		super(from, to, table);
	}
	
	MoveMacro() {
		super(0,0, null); //Dummy values, not used.
	}

	Vector actions = new Vector();
	
	public void execute() throws AgainstTheRulesException, NoFreeCellException {
		for (int i=0; i < actions.size(); i++) {
			((Move) actions.elementAt(i)).execute();
		}
			
	}
	
	public void undo() {
		for (int i=actions.size()-1; i >= 0; i--) {
			((Move) actions.elementAt(i)).undo();
		}
	}
	
	public void addMove(Move move) {
		actions.addElement(move);
	}
	
	public int getSize() {
		return actions.size();
	}
	
	public Move getMoveAt(int location) {
		return (Move) actions.elementAt(location);
	}
	
	public Iterator createIterator() {
		return new MoveMacroIterator(this);
	}
}
