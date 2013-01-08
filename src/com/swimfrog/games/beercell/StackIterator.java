package com.swimfrog.games.beercell;

public class StackIterator implements Iterator {
	private Stack stack;
	private int currentLocation;
	
	StackIterator(Stack stack) {
		setStack(stack);
		currentLocation = stack.getSize()-1;
	}
	
	Stack getStack() {
		return stack;
	}

	public boolean hasNext() {
		if (currentLocation == -1) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasPrevious() {
		if (currentLocation == stack.getSize()-1) {
			return true;
		} else {
			return false;
		}
	}

	public Object next() {
		Card theCard = stack.getCardAt(currentLocation);
		currentLocation--;
		return theCard;
	}
	
	public Object previous() {
		// TODO Unimplemented
		return null;
	}

	public void reset() {
		currentLocation = stack.getSize()-1;
	}

	void setStack(Stack stack) {
		this.stack = stack;
	}



}
