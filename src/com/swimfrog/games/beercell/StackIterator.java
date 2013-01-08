package com.swimfrog.games.beercell;

public class StackIterator implements Iterator {
	private Stack stack;
	private int currentLocation;
	
	StackIterator(Stack stack) {
		setStack(stack);
		currentLocation = stack.getSize()-1;
	}

	public boolean hasNext() {
		if (currentLocation == 0) {
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

	void setStack(Stack stack) {
		this.stack = stack;
	}

	Stack getStack() {
		return stack;
	}



}
