package com.swimfrog.games.beercell;

public interface Iterator {
	public boolean hasNext();
	public boolean hasPrevious();
	public Object next();
	public Object previous();
	public void reset();
}
