package my;


import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class WaitingRoom {
	private final int MAX_AVAILABLE_SEATS = 5;

	private List<Client> shavingQueue;
	private List<Client> haircutQueue;
	private List<Client> stylingQueue;
	private Map<JPanel,Integer> waitingRoomChairs = new HashMap<>();
	private final Lock checkSeatsLock;
    private final Semaphore availableSeats;
    private boolean isEmpty;
    

    private final Lock shavingLock;
    private final Lock haircutLock;
    private final Lock stylingLock;
    
	public WaitingRoom(JPanel[] tab) {
		
		isEmpty = true;
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
		
	}
	public int acquireWaitingRoomChair(Client cl){
				
					if(availableSeats.tryAcquire()){
				
					cl.changeDirection(Direction.TOP);
					isEmpty = false;
					switch(cl.getServiceType()){
						case  HAIRCUTTING :{ joinHaircutQueue(cl);seatOnChair(cl);};
						break;
						case SHAVING :{ joinShavingQueue(cl);seatOnChair(cl);};
						break;
						case STYLING :{ joinStylingQueue(cl);seatOnChair(cl);};
						break;
					}
					System.out.println("nabyty");
					return 1;
					}else{
						return -1;
					}
	}
	
	public void seatOnChair(Client client){
		
		do{
			for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
				checkSeatsLock.lock();
				try{
					if((Math.abs(entry.getKey().getY()+30 - client.getPosition().getY()) < 10) && entry.getValue() !=1){
						client.changeDirection(Direction.RIGHT);
						entry.setValue(1);
						entry.getKey().setBackground(Color.BLACK);
					}
				}finally{
					checkSeatsLock.unlock();
				}
				if((Math.abs(entry.getKey().getX() - client.getPosition().getX()+18) < 10) )
					client.changeDirection(Direction.STOP);
			}	
			switch(client.getServiceType()){
			case HAIRCUTTING : {
				synchronized(SharedData.getInstance().getIsHaircutClient()){
					SharedData.getInstance().getIsHaircutClient().notifyAll();
				}
			}
			case SHAVING : {
				synchronized(SharedData.getInstance().getIsShavingClient()){
					SharedData.getInstance().getIsShavingClient().notifyAll();
				}
			}
			case STYLING : {
				synchronized(SharedData.getInstance().getIsStylingClient()){
					SharedData.getInstance().getIsStylingClient().notifyAll();
				}
			}
			}
		}while(client.getDirection() != Direction.STOP);
		
	}
	public void joinShavingQueue(Client cl){
		shavingLock.lock();
		try{
			shavingQueue.add(cl);
		}finally{
			shavingLock.unlock();
		}
	}
	
	public void joinHaircutQueue(Client cl){
		haircutLock.lock();
		try{
			haircutQueue.add(cl);
		}finally{
			haircutLock.unlock();
		}
	}
	
	public void joinStylingQueue(Client cl){
		stylingLock.lock();
		try{
			stylingQueue.add(cl);
		}finally {
			stylingLock.unlock();
		}
	}
	
	public void leaveShavingQueue(Hairdresser h, JPanel chair){
		Client cl =null;
		
		shavingLock.lock();
		try{
		
		cl = shavingQueue.remove(0);
		
		cl.setHairdresserToFollow(h);
		cl.setChairToSeat(chair);		
		cl.setCalled(true);
		
		}finally{
			shavingLock.unlock();
		}
		
		
	}
	
	public  void leaveHaircutQueue(Hairdresser h, JPanel chair){
		Client cl =null;
		cl = haircutQueue.remove(0);
		cl.setHairdresserToFollow(h);
		cl.setChairToSeat(chair);
		cl.setCalled(true);
		
	}
	
	public  void leaveStylingQueue(Hairdresser h, JPanel chair){
		Client cl =null;
		
		cl = stylingQueue.remove(0);
		
		cl.setHairdresserToFollow(h);
		cl.setChairToSeat(chair);
		cl.setCalled(true);
		
	}
	
	public  void followHairdresser( Client cl){
		
		cl.changeDirection(Direction.LEFT);
		checkSeatsLock.lock();
		try{
			for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
				if(Math.abs(entry.getKey().getY()+50-cl.getPosition().getY())<30){
						entry.setValue(0);
						entry.getKey().setBackground(Color.PINK);
				}
				
				
			}
		}finally {
			checkSeatsLock.unlock();
		}
		availableSeats.release();
		do{
			System.out.print("");
		}while(Math.abs(cl.getChairToSeat().getX()-cl.getPosition().getX())>60);
		
		cl.changeDirection(Direction.TOP);
		do{
			System.out.print("");
		}while(Math.abs(cl.getChairToSeat().getY()-cl.getPosition().getY())>40);
		cl.changeDirection(Direction.LEFT);
		do{
			System.out.print("");
		}while(Math.abs(cl.getChairToSeat().getX()-cl.getPosition().getX())>15);
		cl.changeDirection(Direction.STOP);
		cl.setCalled(false);
		//cl.setServicing(true);
		
		cl.sleep();
		
		cl.getHairdresserToFollow().setDone(true);
		leave(cl);
	}
	
	public void leave(Client cl){
		cl.changeDirection(Direction.BOTTOM);
		do{
			System.out.print("");
		}while(Math.abs(cl.getPosition().getY() - 460)>15);
		cl.changeDirection(Direction.RIGHT);
		do{
			System.out.print("");
		}while(Math.abs(cl.getPosition().getX()-800) > 20);
		cl.changeDirection(Direction.BOTTOM);
		do{
			System.out.print("");
		}while(Math.abs(cl.getPosition().getY()-530)>10);
		cl.changeDirection(Direction.RIGHT);
	}
	
	public boolean isEmpty(){
		return isEmpty;
	}
	public List<Client> getHaircutQueue(){
		return haircutQueue;
	}
	
	public List<Client> getShavingQueue(){
		return shavingQueue;
	}
	
	public List<Client> getStylingQueue(){
		return stylingQueue;
	}

}
