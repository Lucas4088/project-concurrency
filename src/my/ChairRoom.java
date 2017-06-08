package my;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class ChairRoom {
	public final int MAX_AVAILABLE_SEATS = 6;
    private Map<JPanel,Integer> chairRoomChairs = new HashMap<>();
    private WaitingRoom waitingRoom;
    private Lock checkForClientToShaveLock;
    private Lock checkForClientToHaircut;
    private Lock checkForClientToStyle;
    private Lock occupyChairLock;
    private Semaphore numberOfServices;
    private Semaphore emptySeats;
    public ChairRoom(JPanel[] tab, WaitingRoom waitingRoom ) {
    	checkForClientToShaveLock = new ReentrantLock(true);
    	checkForClientToHaircut = new ReentrantLock(true);
    	checkForClientToStyle = new ReentrantLock(true);
    	occupyChairLock = new ReentrantLock(true);
    	
    	numberOfServices = new Semaphore(3, true);
    	emptySeats = new Semaphore(6,true);
    	
    	for(int i = 0;i<MAX_AVAILABLE_SEATS ;i++ )
    		chairRoomChairs.putIfAbsent(tab[i], 0);
    	this.waitingRoom = waitingRoom;
    }
	
    public void  checkItsQueue(Hairdresser h){
    	 try {
  			emptySeats.acquire();
  		} catch (InterruptedException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
    	 System.out.println(h.name + " nabywanie krzes³a");
		switch(h.getServiceType()){
		case HAIRCUTTING : {
			while(waitingRoom.getHaircutQueue().isEmpty()){
				synchronized(SharedData.getInstance().getIsHaircutClient()){
					try {
						SharedData.getInstance().getIsHaircutClient().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		break;
		case SHAVING : {
			while(waitingRoom.getShavingQueue().isEmpty()){
				synchronized(SharedData.getInstance().getIsShavingClient()){
					try {
						SharedData.getInstance().getIsShavingClient().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		break;
		case STYLING : {
			while(waitingRoom.getStylingQueue().isEmpty()){
				synchronized(SharedData.getInstance().getIsStylingClient()){
					try {
						SharedData.getInstance().getIsStylingClient().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		break;
		}
		System.out.println(h.name + " nabywanie us³ug");
    	
    	///
	   //jeden z przedstawicieli ka¿dego typu us³ugi sprawdza czego potrzebuje klient
    	try {
    		numberOfServices.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//zajmujê krzes³o
    	System.out.println(h.name + " nabyto us³ugi");
    	 
    	switch(h.getServiceType()){
    		case HAIRCUTTING : checkForClientToHaircut.lock();
    		break;
    		case SHAVING : checkForClientToShaveLock.lock();
    		break;
    		case STYLING : checkForClientToStyle.lock();
    		break;
    	}
    	try{
    		
    		
    		h.changeDirection(Direction.RIGHT);
    		System.out.println(h.name + " went working");
    		do{
    			
    			System.out.print("");
    		}while(Math.abs(h.getPosition().getX()-700) >=55);
    		if(Math.abs(h.getPosition().getX()-700) <=20){
    			h.changeDirection(Direction.STOP);
    		}
    		switch(h.getServiceType()){
    		case HAIRCUTTING : {
    			if(!waitingRoom.getHaircutQueue().isEmpty()){
    				occupyChair(h);
        			h.setWorking(true);
        			
    			}
    			else{ System.out.println(h.name + " starts going back");
    				goBack(h);
    			}
        		
    			numberOfServices.release();
    		}
    		break;
    		case SHAVING : 
    		{ 
    			if(!waitingRoom.getShavingQueue().isEmpty()){
        			occupyChair(h);
        			h.setWorking(true);
    			}
    			else{System.out.println(h.name + " starts going back");
    				goBack(h);
    			}
        		
    			numberOfServices.release();
    		}
    		break;
    		case STYLING : {
    			if(!waitingRoom.getStylingQueue().isEmpty()){
        			if(!h.getWorking())
        			occupyChair(h);
        			h.setWorking(true);
        			
        			//posadz klienta
    			}
    			else{System.out.println(h.name + " starts going back");
    				goBack(h);
    			}
    			
    			numberOfServices.release();
    		}
    		break;
    		}
    	}finally{
    		
    		switch(h.getServiceType()){
    		case HAIRCUTTING : checkForClientToHaircut.unlock();   		
    		break;
    		case SHAVING : checkForClientToShaveLock.unlock();
    		break;
    		case STYLING : checkForClientToStyle.unlock();
    		break;
    		}
    	}
    	
    	
    }
	
	public void occupyChair(Hairdresser h){
		System.out.println(h.name + " Occupying chair");
		h.changeDirection(Direction.LEFT);
		JPanel chair =null;
		do{
		for(Entry<JPanel, Integer> entry : chairRoomChairs.entrySet()){
			occupyChairLock.lock();
			try{
				if((Math.abs(entry.getKey().getX()+10 - h.getPosition().getX()) < 10) && entry.getValue() !=1){
					h.changeDirection(Direction.TOP);
					chair = entry.getKey();
					entry.setValue(1);
					h.setOccupiedChair(chair);
				}
				}finally{
					occupyChairLock.unlock();
				}
			
		}	
		}while(h.getPosition().getY()>90);
		chair.setBackground(Color.BLACK);
		
		System.out.println(h.name + " Chair Occupied");
		//occupyChairLock.lock();
		//try{
		h.changeDirection(Direction.STOP);
			switch(h.getServiceType()){
			case HAIRCUTTING : waitingRoom.leaveHaircutQueue(h,chair);
			break;
			case SHAVING : waitingRoom.leaveShavingQueue(h,chair);
			break;
			case STYLING : waitingRoom.leaveStylingQueue(h,chair);
			break;
			}
		//}finally{
			//occupyChairLock.unlock();
		//}
		System.out.println(h.name + "Chair occupied");
	}
	
	public void goToItsPosition(Hairdresser h){
		
		h.setDone(false);
		System.out.println(h.name+" skonczy³");
		releaseChair(h);
		h.changeDirection(Direction.BOTTOM);
		System.out.println(h.name+" zwalnia stanowisko");
		//chair
		while(Math.abs((h.getPosition().getY()-20)-h.getInitialPosition().getY())>15){
			System.out.print("");
		}
		
		goBack(h);
	}
	
	public void goBack(Hairdresser h){

		h.changeDirection(Direction.LEFT);
		System.out.println(h.name +  " went LEFT");
		do{
			System.out.print("");
		}while(h.getPosition().getX()>10);
		System.out.println(h.name +  " went back");
		h.changeDirection(Direction.STOP);
		emptySeats.release();
		h.setWorking(false);
		
	}
	
	public void releaseChair(Hairdresser h){
		h.getOccupiedChair().setBackground(Color.PINK);
		
		for(Entry<JPanel, Integer> entry : chairRoomChairs.entrySet()){
			if(entry.getKey() == h.getOccupiedChair())
				entry.setValue(0);
		}
		//emptySeats.release();
		
	}

	
}