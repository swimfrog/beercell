package com.swimfrog.games.beercell;

public class Suit {
	static final int CLUBS = 0;
	static final int DIAMONDS = 1;
	static final int HEARTS = 2;
	static final int SPADES = 3;
	static final int RED = 0x00FF0000;
	static final int BLACK = 0x00000000;
	
	public static int getColor(int suit) {
		switch (suit) {
			case Suit.CLUBS: return BLACK;
			case Suit.DIAMONDS: return RED;
			case Suit.HEARTS: return RED;
			case Suit.SPADES: return BLACK;
			default: return BLACK;
		}
	}
	
	public static boolean isValid(int suit) {
		if ((suit >= Suit.CLUBS) && (suit <= Suit.SPADES)) {
			return true;
		} else {
			return false;
		}
	}
}
