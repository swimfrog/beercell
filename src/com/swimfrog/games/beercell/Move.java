package com.swimfrog.games.beercell;

import com.swimfrog.games.beercell.exceptions.*;


public class Move {
	private int from;
	private int to;
	private Table table;
	
	Move(int from, int to, Table table) {
		setFrom(from);
		setTo(to);
		setTable(table);
	}
	
	public void execute() throws AgainstTheRulesException, NoFreeCellException {
		getTable().move(this);
	}
	
	public void undo() {
		getTable().unmove(this);
	}

	private void setFrom(int from) {
		this.from = from;
	}

	public int getFrom() {
		return from;
	}

	private void setTo(int to) {
		this.to = to;
	}

	public int getTo() {
		return to;
	}

	private void setTable(Table table) {
		this.table = table;
	}

	public Table getTable() {
		return table;
	}
}
