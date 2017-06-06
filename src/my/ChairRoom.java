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
	   //jeden z przedstawicieli ka¿dego typu us³ugi sprawdza czego potrzebuje klient
    	try {
    		numberOfServices.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//zajmujê krzes³o
    	 try {
 			emptySeats.acquire();
 		} catch (InterruptedException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
    	 
    	switch(h.getServiceType()){
    		case HAIRCUTTING : checkForClientToHaircut.lock();
    		break;
    		case SHAVING : checkForClientToShaveLock.lock();
    		break;
    		case STYLING : checkForClientToStyle.lock();
    		break;
    	}
    	try{
    		
    		if(h.getPosition().getX()<700)
    			h.changeDirection(Direction.RIGHT);
    		do{
    			System.out.print("");
    			//if(Math.abs(h.getPosition().getX()-700) <=55)
        			
    		}while(Math.abs(h.getPosition().getX()-700) >=55);
    		h.changeDirection(Direction.STOP);
    		//h.changeDirection(Direction.LEFT);
    		switch(h.getServiceType()){
    		case HAIRCUTTING : {
    			if(!waitingRoom.getHaircutQueue().isEmpty()){
    				//if(h.getWorking())
    				occupyChair(h);
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
        			//
        			//if(!h.getWorking())
        			occupyChair(h);
        			//while(h.getOccupiedChair()==null)
        			//
        			h.setWorking(true);
    			}
    			else goBack(h);
        			//h.changeDirection(Direction.TOP);
        		numberOfServices.release();
    		}
    		break;
    		case STYLING : {
    			if(!waitingRoom.getStylingQueue().isEmpty()){
        			//waitingRoom.leaveStylingQueue();
        			if(!h.getWorking())
        			occupyChair(h);
        			h.setWorking(true);
        			//posadz klienta
    			}
    			else
    				goBack(h);
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
		h.changeDirection(Direction.LEFT);
		JPanel chair =null;
		do{
		for(Entry<JPanel, Integer> entry : chairRoomChairs.entrySet()){
			occupyChairLock.lock();
			try{
				if((Math.abs(entry.getKey().getX() - h.getPosition().getX()) < 20) && entry.getValue() !=1){
					h.changeDirection(Direction.TOP);
					//System.out.println("kieruje sie na gore");
					chair = entry.getKey();
					entry.setValue(1);
					
				}
					/**/
				}finally{
					occupyChairLock.unlock();
				}
					//h.setOccupiedChair(entry.getKey());
/*			if((Math.abs(entry.getKey().getY() - h.getPosition().getY()+30) < 70) )
				h.changeDirection(Direction.STOP);*/
			
		}	
		}while(h.getPosition().getY()>90);
		chair.setBackground(Color.BLACK);
		occupyChairLock.lock();
		try{
		h.changeDirection(Direction.STOP);
			switch(h.getServiceType()){
			case HAIRCUTTING : waitingRoom.leaveHaircutQueue(h,chair);
			break;
			case SHAVING : waitingRoom.leaveShavingQueue(h,chair);
			break;
			case STYLING : waitingRoom.leaveStylingQueue(h,chair);
			break;
			}
		}finally{
			occupyChairLock.unlock();
		}
		
		
		//waitingRoom.followHairdresser(h.getOccupiedChair(), waitingRoom.leaveShavingQueue(h));
	}
	
	public void goBack(Hairdresser h){
		
		h.changeDirection(Direction.LEFT);
		do{
			//System.out.println("Czy wracam?");
		//if(h.getPosition().getX()<20){
			System.out.print("");
		//}
		}while(h.getPosition().getX()>12);
		
		h.changeDirection(Direction.STOP);
		
		h.setWorking(false);
		synchronized(SharedData.getInstance().getIsClient()){
			try {
				SharedData.getInstance().getIsClient().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		emptySeats.release();
	}
	
	public void releaseChair(){
		
	}
	
	public void enterHairdressingRoom(){
		
	}
	
}