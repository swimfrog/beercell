package com.swimfrog.games.beercell;

import javax.microedition.lcdui.*;
import com.swimfrog.games.beercell.exceptions.*;

public class Card extends Canvas {
	private final int rank;
	private final int suit;
	private Deck deck;
	private Image imageNormal;
	private Image imageInverted;
	private Coordinate base = new Coordinate(0,0);
	private int height = 0;
	private int width = 0;
	private boolean selected = false;
	
	Card (Deck deck, int rank, int suit) throws InvalidCardException {
		if (! Rank.isValid(rank)) {
			throw new InvalidCardException(rank+" is not a valid rank.");
		}
		this.rank = rank;
		if (! Suit.isValid(suit)) {
			throw new InvalidCardException(suit+" is not a valid suit.");
		}
		this.suit = suit;
		this.deck = deck;
	}
	
	public Image getImage() {
		//TODO: Make an option to get the imageInverted as well?
		return imageNormal;
	}
	
	private void buildImage() {
		imageNormal = Image.createImage(width, height);
		Graphics g = imageNormal.getGraphics();
	
		//Draw the white part
		g.setColor(0x00FFFFFF);
		g.fillRoundRect(0, 0, width-1, height-1, 3, 3);
		
		//Draw the black outline
		g.setColor(0x00000000);
		g.drawRoundRect(0, 0, width-1, height-1, 3, 3);
		
		//Draw the numbers
		g.setColor(Suit.getColor(suit));
		Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		g.setFont(font);
		g.drawString(getRank(), 2, 0, Graphics.LEFT | Graphics.TOP);
		
		//Draw the icons
		g.drawImage(deck.getSmallSuitImage(suit), width-2, 2, Graphics.TOP|Graphics.RIGHT);
		g.drawImage(deck.getBigSuitImage(suit), width/2, (height/3)*2, Graphics.HCENTER|Graphics.VCENTER);
		
		imageInverted = deck.negateImage(imageNormal);
	}
	
	public Coordinate getBase() {
		return this.base;
	}
	
	public String getRank() {
		switch (this.rank) {
			case Rank.ACE: return "A"; 
			case Rank.TWO: return "2";
			case Rank.THREE: return "3";
			case Rank.FOUR: return "4";
			case Rank.FIVE: return "5";
			case Rank.SIX: return "6";
			case Rank.SEVEN: return "7";
			case Rank.EIGHT: return "8";
			case Rank.NINE: return "9";
			case Rank.TEN: return "10";
			case Rank.JACK: return "J";
			case Rank.QUEEN: return "Q";
			case Rank.KING: return "K";
			default: return "??";
		}
	}
	
	public int getSuit() {
		return this.suit;
	}
	
	public int getValue() {
			return this.rank;
	}
	
	public boolean isSelected() {
		return this.selected;
	}

	protected void paint(Graphics g) {
		if (this.selected == true) {
			g.drawImage(imageInverted, this.base.getX(), this.base.getY(), Graphics.TOP | Graphics.LEFT);
		} else {
			g.drawImage(imageNormal, this.base.getX(), this.base.getY(), Graphics.TOP | Graphics.LEFT);
		}
		
	}
	
	public void print() {
		System.out.println(getRank()+" of "+getSuit()+", Value: "+getValue());
	}
	
	public void select() {
		this.selected = true;
	}
	
	public void setPosition(int x, int y) {
		this.base.setX(x);
		this.base.setY(y);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		buildImage();
	}

	public void unselect() {
		this.selected = false;
	}
	
}
