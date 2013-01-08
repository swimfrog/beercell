package com.swimfrog.games.beercell;

public class MoveMacroIterator implements Iterator {
	private MoveMacro move;
	private int currentLocation;
	
	MoveMacroIterator(MoveMacro moveMacro) {
		setMove(moveMacro);
		currentLocation = moveMacro.getSize()-1;
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

	void setMove(MoveMacro move) {
		this.move = move;
	}

	MoveMacro getMove() {
		return move;
	}

}
