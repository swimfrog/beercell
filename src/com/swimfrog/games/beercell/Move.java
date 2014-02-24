package com.swimfrog.games.beercell;

import com.swimfrog.games.beercell.exceptions.*;
import javax.microedition.lcdui.*;

public class Move {
	private int from;
	private int to;
	private Table table;
	
	Move(int from, int to, Table table) {
		setFrom(from);
		setTo(to);
		setTable(table);
	}
	
	public void execute() throws AgainstTheRulesException, NoFreeCellException, InvalidCardException {
		
		Table theTable = getTable();

		//Stack fromStack = theTable.getStackAt(from);
		Card fromCard = theTable.getCardAt(from);
		Coordinate fromCoord = fromCard.getBase();
		/* Shouldn't need this, because if we're moving from an empty stack,
		 * we should have caught the error by now... */
//		if (fromStack.hasCards()) {
//			fromCoord = theTable.getCardAt(from).getBase();
//		} else {
//			fromCoord = theTable.getStackAt(from).getBase();
//		}
		
		Stack toStack = theTable.getStackAt(to);
		Coordinate toCoord;
		if (toStack.hasCards()) {
			toCoord = theTable.getCardAt(to).getBase();
		} else {
			toCoord = theTable.getStackAt(to).getBase();
		}
		
		Image image = fromCard.getImage();
		
		Animator animator = new Animator(image, fromCoord, toCoord);
		
		//System.err.println(animator.toString());

		getTable().doMove(this);
		//getTable().addToHistory(this);
		
		getTable().checkWin();
	}
	
	public int getFrom() {
		return from;
	}
	
	public void addToHistory() {
		table.addToHistory(this);
	}

	public Table getTable() {
		return table;
	}

	public int getTo() {
		return to;
	}

	private void setFrom(int from) {
		this.from = from;
	}

	private void setTable(Table table) {
		this.table = table;
	}

	private void setTo(int to) {
		this.to = to;
	}

	public void undo() {
		getTable().unmove(this);
	}
}