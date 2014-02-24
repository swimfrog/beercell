package com.swimfrog.games.beercell;

public class MoveMacroIterator implements Iterator {
	private MoveMacro move;
	private int currentLocation;
	
	MoveMacroIterator(MoveMacro moveMacro) {
		setMove(moveMacro);
		currentLocation = moveMacro.getSize()-1;
	}
	
	MoveMacro getMove() {
		return move;
	}

	public boolean hasNext() {
		if (currentLocation == move.getSize()-1) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasPrevious() {
		if (currentLocation == -1) {
			return false;
		} else {
			return true;
		}
	}

	public Object next() {
		Move theMove = move.getMoveAt(currentLocation);
		currentLocation++;
		return theMove;
	}
	
	public Object previous() {
		Move theMove = move.getMoveAt(currentLocation);
		currentLocation--;
		return theMove;
	}

	public void reset() {
		currentLocation = move.getSize()-1;
	}

	void setMove(MoveMacro move) {
		this.move = move;
	}

}
