package my;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class WaitingRoom {
	private final int MAX_AVAILABLE_SEATS = 5;
	private int freeSeats;
	private List<Client> shavingQueue;
	private List<Client> haircutQueue;
	private List<Client> stylingQueue;
	private Map<JPanel,Integer> waitingRoomChairs = new HashMap<>();
	private final Lock checkSeatsLock;
    private final Semaphore availableSeats;

    private final Lock shavingLock;
    private final Lock haircutLock;
    private final Lock stylingLock;
    
	public WaitingRoom(JPanel[] tab) {
		
		shavingQueue = new LinkedList<>();
		haircutQueue = new LinkedList<>();
		stylingQueue = new LinkedList<>();
		checkSeatsLock = new ReentrantLock(true);
		availableSeats = new Semaphore(MAX_AVAILABLE_SEATS,true);
		
		shavingLock = new ReentrantLock();
		haircutLock = new ReentrantLock();
		stylingLock = new ReentrantLock();
		
		for(int i = 0;i<MAX_AVAILABLE_SEATS ;i++ )
			waitingRoomChairs.putIfAbsent(tab[i], 0);
		
		for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
			System.out.println(entry.getKey().getHeight());
			}
	}
	public int acquireWaitingRoomChair(Client cl){
		if(checkSeatsLock.tryLock()){
			try{
				if(availableSeats.tryAcquire()){
					cl.changeDirection(Direction.TOP);
					switch(cl.getServiceType()){
						case  HAIRCUTTING : joinHaircutQueue(cl);
						break;
						case SHAVING : joinShavingQueue(cl);
						break;
						case STYLING : joinStylingQueue(cl);
						break;
					}
					System.out.println("nabyty");
					return 1;
				}
			}finally{
				checkSeatsLock.unlock();
			}
		}else{
			return -1;
		}
		return 0;
	}
	
	public void seatOnChair(Client client){
		
		for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
			if((Math.abs(entry.getKey().getY() - client.getPosition().getY()) < 40) && entry.getValue() !=1){
				client.changeDirection(Direction.RIGHT);

				entry.setValue(1);
			}
			if((Math.abs(entry.getKey().getX() - client.getPosition().getX()+18) < 10) )
				client.changeDirection(Direction.STOP);
			
			
		}		
	}
	public void joinShavingQueue(Client cl){
		shavingLock.lock();
		try{
			System.out.println("shavingQueue");
			shavingQueue.add(cl);
		}finally{
			shavingLock.unlock();
		}
	}
	
	public void joinHaircutQueue(Client cl){
		haircutLock.lock();
		try{
			System.out.println("haircutQueue");
			haircutQueue.add(cl);
		}finally{
			haircutLock.unlock();
		}
	}
	
	public void joinStylingQueue(Client cl){
		stylingLock.lock();
		try{
			System.out.println("stylingQueue");
			stylingQueue.add(cl);
		}finally {
			stylingLock.unlock();
		}
	}
	
	public void leaveShavingQueue(){
		shavingLock.lock();
		try{
		shavingQueue.get(0);
		}finally{
			shavingLock.unlock();
		}
		
	}
	
	public void leaveHaircutQueue(){
		
	}
	
	public void leaveStylingQueue(){

	}
	
	public int getChairCentreX(JPanel panel){
		return panel.getY()+25;
	}
	
}
