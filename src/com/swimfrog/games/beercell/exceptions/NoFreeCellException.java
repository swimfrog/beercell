package com.swimfrog.games.beercell.exceptions;


public class NoFreeCellException extends Exception {

	public NoFreeCellException() {
		super("There are not enough free cells available to complete this move.");
	}
}
