package com.swimfrog.games.beercell;

import javax.microedition.lcdui.*;

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
	
	public void select() {
		this.selected = true;
	}
	
	public void unselect() {
		this.selected = false;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	Card (Deck deck, int rank, int suit) {
		//TODO: Check to make sure rank is valid
		this.rank = rank;
		//TODO: Check to make sure suit is valid
		this.suit = suit;
		this.deck = deck;
	}
	
	public Coordinate getBase() {
		return this.base;
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

	private void buildImage() {
		imageNormal = Image.createImage(width, height);
		Graphics g = imageNormal.getGraphics();
		
		g.setColor(0x00FFFFFF);
		g.fillRoundRect(0, 0, imageNormal.getWidth()-1, imageNormal.getHeight()-1, 3, 3);
		g.setColor(0x00000000);
		//g.drawRoundRect(this.base.getX(), this.base.getY(), width, height, 3, 3);
		g.drawRoundRect(0, 0, imageNormal.getWidth()-1, imageNormal.getHeight()-1, 3, 3);
		g.setColor(Suit.getColor(suit));
		Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		g.setFont(font);
		g.drawString(getRank(), 2, 0, Graphics.LEFT | Graphics.TOP);
		//g.drawString(getRank(), this.base.getX()+width, this.base.getY()+height-1, Graphics.RIGHT | Graphics.BASELINE);
		
		//Draw the icon
		g.drawImage(deck.getSmallSuitImage(suit), width-2, 2, Graphics.TOP|Graphics.RIGHT);
		g.drawImage(deck.getBigSuitImage(suit), width/2, (height/3)*2, Graphics.HCENTER|Graphics.VCENTER);
		//g.drawImage(resizeImage(icon, icon.getWidth(), icon.getHeight()), 0, 0, Graphics.TOP|Graphics.RIGHT);
		
		//TODO getRGB() and invert the values to create imageInverted
		imageInverted = deck.negateImage(imageNormal);
	}
	
	public int getValue() {
			return this.rank;
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
	
	public void print() {
		System.out.println(getRank()+" of "+getSuit()+", Value: "+getValue());
	}

	protected void paint(Graphics g) {
		if (this.selected == true) {
			g.drawImage(imageInverted, this.base.getX(), this.base.getY(), Graphics.TOP | Graphics.LEFT);
		} else {
			g.drawImage(imageNormal, this.base.getX(), this.base.getY(), Graphics.TOP | Graphics.LEFT);
		}
		
	}
	
}
