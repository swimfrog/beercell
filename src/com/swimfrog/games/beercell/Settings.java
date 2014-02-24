package com.swimfrog.games.beercell;

import java.io.*;

import javax.microedition.rms.*;
import java.util.*;

public class Settings {
	private RecordStore recordStore = null;
	
	private int gamesWon = 0;
	private int gamesAbandoned = 0;
	private int avgTime = 0;
	private int minTime = 0;
	private int maxTime = 0;

	public Settings() {
	    restoreState();
	}
	
	public void clearScores() {
		gamesWon=0;
		gamesAbandoned=0;
		avgTime=0;
		minTime=0;
		maxTime=0;
		saveState();
	}
	
	public void incrementGamesWon() {
		gamesWon++;
		saveState();
	}
	
	public void incrementGamesAbandoned() {
		gamesAbandoned++;
		saveState();
	}
	
	public void addToAvgTime(int time) {
		avgTime = (((avgTime*gamesWon)+time)/(gamesWon+1));
		
		if (time > maxTime) {
			maxTime = time;
		}
		
		if ((time < minTime) || (minTime == 0)) {
			minTime = time;
		}
				
		saveState();
	}
	
	public int getGamesWon() {
		return gamesWon;
	}
	
	public int getGamesAbandoned() {
		return gamesAbandoned;
	}
	
	public int getAvgTime() {
		return avgTime;
	}
	
	public int getMinTime() {
		return minTime;
	}
	
	public int getMaxTime() {
		return maxTime;
	}
	
	private void restoreState() {
	    try {
	    	recordStore = RecordStore.openRecordStore("BeerCell", true);
	    } catch (RecordStoreException rse) {
	    	  System.out.println("Record Store Exception in the ctor." + rse);
	    	  rse.printStackTrace();
	    }
		
		try {
			RecordEnumeration re = recordStore.enumerateRecords(null, null, true);
			if (re.hasNextElement()) {
		        int id = re.nextRecordId();
		        ByteArrayInputStream bais = new ByteArrayInputStream(
		            recordStore.getRecord(id));
		        DataInputStream inputStream = new DataInputStream(bais);
		        try {
		          this.gamesWon = inputStream.readInt();
		          this.gamesAbandoned = inputStream.readInt();
		          this.avgTime = inputStream.readInt();
		          this.maxTime = inputStream.readInt();
		          this.minTime = inputStream.readInt();
		        } catch (EOFException eofe) {
		          System.out.println(eofe);
		          eofe.printStackTrace();
		        }
			}
		} catch (RecordStoreException rse) {
			System.out.println(rse);
			rse.printStackTrace();
		} catch (IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();
		}
		
	    try {
	        if (recordStore != null)
	          recordStore.closeRecordStore();
	      } catch (Exception ignore) {
	        // ignore this
	      }
	}
	
	public void saveState() {
	    try {
	    	recordStore = RecordStore.openRecordStore("BeerCell", true);
	    } catch (RecordStoreException rse) {
	    	  System.out.println("Record Store Exception in the ctor." + rse);
	    	  rse.printStackTrace();
	    }
		
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    DataOutputStream outputStream = new DataOutputStream(baos);
	    try {
	      outputStream.writeInt(gamesWon);
	      outputStream.writeInt(gamesAbandoned);
	      outputStream.writeInt(avgTime);
	      outputStream.writeInt(maxTime);
	      outputStream.writeInt(minTime);
	    } catch (IOException ioe) {
	      System.out.println(ioe);
	      ioe.printStackTrace();
	    }

	    // Extract the byte array
	    byte[] b = baos.toByteArray();
	    try {
	    	RecordEnumeration re = recordStore.enumerateRecords(null, null, true);
	    	if (re.hasNextElement()) {
	    		//Find the record ID and overwrite it.
		        int id = re.nextRecordId();
		        recordStore.setRecord(id, b, 0, b.length);	    		
	    	} else {
		      //Create a new record
	    		recordStore.addRecord(b, 0, b.length);
	    	}
	    } catch (RecordStoreException rse) {
	      System.out.println(rse);
	      rse.printStackTrace();
	    }
	    
	    try {
	        if (recordStore != null)
	          recordStore.closeRecordStore();
	      } catch (Exception ignore) {
	        // ignore this
	      }
	  }
	
	
	
}
