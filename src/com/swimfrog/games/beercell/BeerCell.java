package com.swimfrog.games.beercell;


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.media.control.*;

public class BeerCell extends MIDlet implements CommandListener {
    private Display display;
    protected boolean started;
    Command exitCommand = new Command("Exit", Command.EXIT, 0);
    Command backCommand = new Command("Back", Command.BACK, 2);
    Command undoCommand = new Command("Undo move", Command.SCREEN, 1);
    Command autoCommand = new Command("Auto solve", Command.SCREEN, 0);
    Command clearCommand = new Command("Clear scores", Command.SCREEN, 5);
    Command cheatCommand = new Command("Cheat", Command.SCREEN, 10);
    private List mainMenu;
    private Table table;
    private StatsScreen statsScreen;
    private boolean running = false;	//Already running.
    private Settings settings;
    
	public void alert(String text) {
		Alert alert = new Alert("Alert", text, null, AlertType.ERROR);
		display.setCurrent(alert);
	}
	
	public void commandAction(Command c, Displayable d) {
		if (d == mainMenu) {
            // New example selected
            int index = mainMenu.getSelectedIndex();
            String name = mainMenu.getString(index);
            if (name.equals("Quit")) {
            	if ( table != null ) {
            		// If there is a current game 
            		settings.incrementGamesAbandoned();
            	}
            	notifyDestroyed();
            } else if (name.equals("Start New Game")) {
            	if ( table != null ) {
            		// If there is a current game 
            		settings.incrementGamesAbandoned();
            	}
            	table = new Table(this);
            	table.addCommand(exitCommand);
            	table.addCommand(backCommand);
            	table.addCommand(undoCommand);
            	table.addCommand(autoCommand);
        	    table.setCommandListener(this);
            	display.setCurrent(table);
            } else if (name.equals("View Statistics")) {
            	statsScreen = new StatsScreen(this, settings);
            	statsScreen.addCommand(backCommand);
            	statsScreen.addCommand(clearCommand);
            	statsScreen.setCommandListener(this);
            	display.setCurrent(statsScreen);
            } else if (name.equals("Resume Existing")) {
            	if (table == null) {
            		alert("You haven't started a game yet!");
            	} else {
            		display.setCurrent(table);
            	}
            }
        } else if (c == exitCommand) {
            // Exit. No need to call destroyApp
            // because it is empty.
        	if ( table != null ) {
        		// If there is a current game 
        		settings.incrementGamesAbandoned();
        	}
            notifyDestroyed();
        } else if (c == backCommand) {
            // Go back to main selection list
        	display.setCurrent(mainMenu);
        } else if (c == clearCommand) {
            // Go back to main selection list
        	settings.clearScores();
        	statsScreen.repaint();
        } else if (c == undoCommand) {
        	// Undo the previous move
        	table.undo();
        } else if (c == autoCommand) {
        	// Do some random thing
        	table.autoHome();
        } else if (c == cheatCommand) {
        	// Cheat!
        	table.cheat();
        }
	}
	
	private void createMenu() {
    	mainMenu = new List("BeerCell", Choice.IMPLICIT);
    	mainMenu.append("Start New Game", null);
    	mainMenu.append("Resume Existing", null);
    	mainMenu.append("View Statistics", null);
    	mainMenu.append("Quit", null);
    	mainMenu.setCommandListener(this);
    }
    
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException { }
    
    protected void pauseApp() {
    	if ( table != null ) {
    		table.pause();
    	}
    }
    
    protected void startApp() throws MIDletStateChangeException {
    	if ( ! running ) {
    		//Restore our state from the last run.
    		settings = new Settings();
    		
    		//System.err.println("Stats: gamesWon: "+settings.getGamesWon());
    		//System.err.println("Stats: gamesAbandoned: "+settings.getGamesAbandoned());
    		//System.err.println("Stats: avgTime: "+settings.getAvgTime());
    		
	    	display = Display.getDisplay(this);
	
	    	//if (! running) {
	        	createMenu();
	    	//} else {
	    		//mainMenu.append("Resume Existing", null);
	    	//}
			
			display.setCurrent(mainMenu);
			running = true;
    	} else {
        	if ( table != null) {
        		table.resume();
        	}
    	}
	}
    
    protected void winGame() {
    	settings.addToAvgTime(table.getTimerValue());
    	settings.incrementGamesWon();
    	
    	WinScreen winScreen = new WinScreen(this);
    	winScreen.addCommand(backCommand);
    	winScreen.setCommandListener(this);
    	display.setCurrent(winScreen);
    	
    	// We don't need the table anymore.
    	table = null;
    }
    
    protected void addCheat() {
    	table.addCommand(cheatCommand);
    }
}
