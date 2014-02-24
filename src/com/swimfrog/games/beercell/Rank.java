package com.swimfrog.games.beercell;

public class Rank {
	static final int ACE = 1;
	static final int TWO = 2;
	static final int THREE = 3;
	static final int FOUR = 4;
	static final int FIVE = 5;
	static final int SIX = 6;
	static final int SEVEN = 7;
	static final int EIGHT = 8;
	static final int NINE = 9;
	static final int TEN = 10;
	static final int JACK = 11;
	static final int QUEEN = 12;
	static final int KING = 13;
	
	public static boolean isValid(int rank) {
		if ((rank >= Rank.ACE) && (rank <= Rank.KING)) {
			return true;
		} else {
			return false;
		}
	}
}
