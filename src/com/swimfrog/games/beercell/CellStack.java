package com.swimfrog.games.beercell;

import com.swimfrog.games.beercell.exceptions.AgainstTheRulesException;

public class CellStack extends HomeStack {
	
	CellStack(int baseX, int baseY, int width, int height) {
		super(baseX, baseY, width, height);
	}
	
	public void push(Card card) throws AgainstTheRulesException {
		if ( cards.size() == 0 ) {
			cards.addElement(card);
		} else {
			throw new AgainstTheRulesException("You can't add more than one card to this cell.");
		}
	}
}
