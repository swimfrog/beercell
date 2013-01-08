package com.swimfrog.games.beercell;


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class BeerCell extends MIDlet implements CommandListener {
    private Display display;
    protected boolean started;
    private Command exitCommand;
    private Command backCommand;
    private Command testCommand;
    private List mainMenu;
    private Table table = new Table(this);
    
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException { }
	protected void pauseApp() {	}
	
	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);	
		createCommands();
		createMenu();
		
		display.setCurrent(mainMenu);
	}
	public void commandAction(Command c, Displayable d) {
		if (d == mainMenu) {
            // New example selected
            int index = mainMenu.getSelectedIndex();
            String name = mainMenu.getString(index);
            if (name.equals("Quit")) {
            	notifyDestroyed();
            } else if (name.equals("Play")) {
            	display.setCurrent(createTable());
            }
        } else if (c == exitCommand) {
            // Exit. No need to call destroyApp
            // because it is empty.
            notifyDestroyed();
        } else if (c == backCommand) {
            // Go back to main selection list
            display.setCurrent(mainMenu);
        } else if (c == testCommand) {
        	// Do some random thing
        	table.testRandomMovements();
        }
	}
	
	public void alert(String text) {
		Alert alert = new Alert("Alert", text, null, AlertType.ERROR);
		display.setCurrent(alert);
	}
	
    private void createCommands() {
        exitCommand = new Command("Exit", Command.EXIT, 0);
        backCommand = new Command("Back", Command.BACK, 1);
        testCommand = new Command("Test", Command.SCREEN, 1);
    }
    
    private void createMenu() {
    	mainMenu = new List("BeerCell", List.IMPLICIT);
    	mainMenu.append("Play", null);
    	mainMenu.append("Quit", null);
    	mainMenu.setCommandListener(this);
    }
    
    private void addCommands(Displayable d) {
        d.addCommand(exitCommand);
        d.addCommand(backCommand);
        d.addCommand(testCommand);
        d.setCommandListener(this);
    }
    
    private Canvas createTable() {
    	Canvas canvas = table;
    	addCommands(canvas);
    	return canvas;
    }
}
