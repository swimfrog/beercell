package com.swimfrog.games.beercell;

import java.io.IOException;
import java.util.*;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Deck {
	private Vector cards = new Vector();
	private int cardWidth;
	private int cardHeight;
	private Image bigSuits[] = new Image[4];
	private Image littleSuits[] = new Image[4];
	
	Deck(int cardWidth, int cardHeight) {
		this.cardWidth = cardWidth;
		this.cardHeight = cardHeight;
		initialize();
		
		for (int i=0; i < cards.size(); i++) {
			Card theCard = (Card) cards.elementAt(i);
			theCard.setSize(cardWidth, cardHeight);
		}
	}
	
	public void initialize() {
		//Fill the deck with cards
		cards.addElement(new Card(this, Rank.ACE, Suit.DIAMONDS));
		
		cards.addElement(new Card(this, Rank.TWO, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.THREE, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.FOUR, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.FIVE, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.SIX, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.SEVEN, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.EIGHT, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.NINE, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.TEN, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.JACK, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.QUEEN, Suit.DIAMONDS));
		cards.addElement(new Card(this, Rank.KING, Suit.DIAMONDS));
		
		cards.addElement(new Card(this, Rank.ACE, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.TWO, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.THREE, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.FOUR, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.FIVE, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.SIX, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.SEVEN, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.EIGHT, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.NINE, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.TEN, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.JACK, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.QUEEN, Suit.HEARTS));
		cards.addElement(new Card(this, Rank.KING, Suit.HEARTS));
		
		cards.addElement(new Card(this, Rank.ACE, Suit.SPADES));
		cards.addElement(new Card(this, Rank.TWO, Suit.SPADES));
		cards.addElement(new Card(this, Rank.THREE, Suit.SPADES));
		cards.addElement(new Card(this, Rank.FOUR, Suit.SPADES));
		cards.addElement(new Card(this, Rank.FIVE, Suit.SPADES));
		cards.addElement(new Card(this, Rank.SIX, Suit.SPADES));
		cards.addElement(new Card(this, Rank.SEVEN, Suit.SPADES));
		cards.addElement(new Card(this, Rank.EIGHT, Suit.SPADES));
		cards.addElement(new Card(this, Rank.NINE, Suit.SPADES));
		cards.addElement(new Card(this, Rank.TEN, Suit.SPADES));
		cards.addElement(new Card(this, Rank.JACK, Suit.SPADES));
		cards.addElement(new Card(this, Rank.QUEEN, Suit.SPADES));
		cards.addElement(new Card(this, Rank.KING, Suit.SPADES));
		
		cards.addElement(new Card(this, Rank.ACE, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.TWO, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.THREE, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.FOUR, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.FIVE, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.SIX, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.SEVEN, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.EIGHT, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.NINE, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.TEN, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.JACK, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.QUEEN, Suit.CLUBS));
		cards.addElement(new Card(this, Rank.KING, Suit.CLUBS));
		
		// Build the images for the suits, both big and small
		for (int i=0; i < 4; i++) {
			bigSuits[i] = buildSuitImage(i, BuildSuitImageMode.BIG);
			littleSuits[i] = buildSuitImage(i, BuildSuitImageMode.SMALL);
		}
	}
	
	public Image getBigSuitImage(int i) {
		return this.bigSuits[i];
	}
	
	public Image getSmallSuitImage(int i) {
		return this.littleSuits[i];
	}
	
	private class BuildSuitImageMode {
		final static int BIG = 0;
		final static int SMALL = 1;
	}
	
	private Image buildSuitImage(int suit, int mode) {
		int divisor;
		switch (mode) {
			case BuildSuitImageMode.BIG: divisor = 2; break;
			case BuildSuitImageMode.SMALL: divisor = 4; break;
			default: divisor = 1;
		}
		
		Image icon = null;
		// Pull in graphics files and populate the icons
		try {
			switch (suit) {
				case Suit.CLUBS: icon = Image.createImage("/clubs.png"); break;
				case Suit.DIAMONDS: icon = Image.createImage("/diamonds.png"); break;
				case Suit.HEARTS: icon = Image.createImage("/hearts.png"); break;
				case Suit.SPADES: icon = Image.createImage("/spades.png"); break;
			}
		} catch (IOException e) {
			icon = Image.createImage(this.cardWidth/divisor, this.cardHeight/divisor);
			Graphics ig = icon.getGraphics();
			ig.drawString("?", 0, 0, Graphics.TOP|Graphics.LEFT);
		}
		
		if ((icon.getWidth() >= this.cardWidth/divisor) && (icon.getHeight() >= this.cardHeight/divisor)) {
			icon = resizeImage(icon, cardWidth/divisor, cardWidth/divisor);
		}
		
		return icon;
	}
	
	private Image resizeImage(Image image, int newWidth, int newHeight) {
		int srcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		Image newImage = Image.createImage(newWidth, newHeight);
		Graphics g = newImage.getGraphics();

		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				g.setClip(x, y, 1, 1);
				int dx = x * srcWidth / newWidth;
				int dy = y * srcHeight / newHeight;
				g.drawImage(image, x - dx, y - dy,
				Graphics.LEFT | Graphics.TOP);
			}
		}

		Image immutableImage = Image.createImage(newImage);

		return immutableImage;
	}
	
	public Image negateImage(Image image) {
		int[] imageRGB = new int[image.getWidth() * image.getHeight()];
		image.getRGB(imageRGB, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
		for (int i = 0; i < imageRGB.length; i++) {
			imageRGB[i] = ~ imageRGB[i]; //NOT
		}

		return Image.createRGBImage(imageRGB, image.getWidth(), image.getHeight(), false);
	}
	
	void print() {
		for (int i=0; i < cards.size(); i++) {
			Card thisCard = (Card) cards.elementAt(i);
			thisCard.print();
		}
	}
	
	public int length() {
		return cards.size();
	}
	
	public Card popRandom() {
		Random generator = new Random();
		int r = generator.nextInt(cards.size());
		Card theCard = (Card) cards.elementAt(r);
		cards.removeElementAt(r);
		//cards.remove(theCard);
		return theCard;
	}
}
