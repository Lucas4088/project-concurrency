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
    	while(waitingRoom.isEmpty() != false){
			try {
				synchronized(SharedData.getInstance().getIsClient()){
				SharedData.getInstance().getIsClient().wait();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
	   
    	try {
    		numberOfServices.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 try {
 			emptySeats.acquire();
 		} catch (InterruptedException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
    	switch(h.getServiceType()){
    		case HAIRCUTTING :{ checkForClientToHaircut.lock();/*;System.out.println("haircutting locked");*/};
    		break;
    		case SHAVING : {checkForClientToShaveLock.lock();/*System.out.println("shaving locked");*/};
    		break;
    		case STYLING :{ checkForClientToStyle.lock();/*System.out.println("styling locked");*/};
    		break;
    	}
    	try{
    		
    		if(h.getPosition().getX()<700)
    			h.changeDirection(Direction.RIGHT);
    		
    		do{
    			
    			System.out.print("");
    			if(Math.abs(h.getPosition().getX()-700) <=55)
        			h.changeDirection(Direction.STOP);
    		}while(Math.abs(h.getPosition().getX()-700) >55);
    		
    		switch(h.getServiceType()){
    		case HAIRCUTTING : {
    			if(!waitingRoom.getHaircutQueue().isEmpty()){
        			waitingRoom.leaveHaircutQueue();
        			h.changeDirection(Direction.LEFT);
        			h.setWorking(true);
        			System.out.println("working");
    			}
    			else goBack(h);
        			//h.changeDirection(Direction.TOP);
        			numberOfServices.release();
    		}
    		break;
    		case SHAVING : 
    		{
    			if(!waitingRoom.getShavingQueue().isEmpty()){
        			waitingRoom.leaveShavingQueue();
        			h.changeDirection(Direction.LEFT);
        			h.setWorking(true);
    			}
    			else goBack(h);
        			//h.changeDirection(Direction.TOP);
        			numberOfServices.release();
    		}
    		break;
    		case STYLING : {
    			if(!waitingRoom.getStylingQueue().isEmpty()){
        			waitingRoom.leaveStylingQueue();
        			h.changeDirection(Direction.LEFT);
        			h.setWorking(true);
        			//posadz klienta
    			}
    			else goBack(h);
        			
        			
        			numberOfServices.release();
    		}
    		break;
    	}
    		
    		
    	}finally{
    		System.out.println("here we are");
    		switch(h.getServiceType()){
    		case HAIRCUTTING :{ checkForClientToHaircut.unlock();System.out.println("haircutting unlocked");};   		
    		break;
    		case SHAVING :{ checkForClientToShaveLock.unlock();System.out.println("shaving unlocked");};
    		break;
    		case STYLING :{ checkForClientToStyle.unlock();System.out.println("styling unlocked");};
    	}
    	}
    	
    }
	
	public void occupyChair(Hairdresser h){
		
		System.out.println("Zajmuje krzeslo");
		do{
		for(Entry<JPanel, Integer> entry : chairRoomChairs.entrySet()){
			occupyChairLock.lock();
			try{
				if((Math.abs(entry.getKey().getX() - h.getPosition().getX()) < 20) && entry.getValue() !=1){
					h.changeDirection(Direction.TOP);
					System.out.println("kieruje sie na gore");
					entry.setValue(1);
				}
			}finally{
				occupyChairLock.unlock();
			}
			if((Math.abs(entry.getKey().getY() - h.getPosition().getY()+30) < 70) )
				h.changeDirection(Direction.STOP);
			
		}	
		}while(h.getPosition().getY()>90);
	}
	
	public void goBack(Hairdresser h){
		h.changeDirection(Direction.LEFT);
		
		do{
			System.out.println("Czy wracam?");
		//if(h.getPosition().getX()<20){
			
		//}
		}while(h.getPosition().getX()>20);
		
		h.changeDirection(Direction.STOP);
		emptySeats.release();
		synchronized(SharedData.getInstance().getIsClient()){
			try {
				SharedData.getInstance().getIsClient().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void releaseChair(){
		
	}
	
	public void enterHairdressingRoom(){
		
	}
	
}
