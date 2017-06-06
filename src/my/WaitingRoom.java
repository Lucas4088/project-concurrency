package my;


import java.awt.Color;
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

	private List<Client> shavingQueue;
	private List<Client> haircutQueue;
	private List<Client> stylingQueue;
	private Map<JPanel,Integer> waitingRoomChairs = new HashMap<>();
	private final Lock checkSeatsLock;
	private final Lock uncheckSeatsLock;
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
		uncheckSeatsLock = new ReentrantLock(true);
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
		
		//System.out.println("Tying to seat");
		do{
			for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
				checkSeatsLock.lock();
				try{
					if((Math.abs(entry.getKey().getY()+30 - client.getPosition().getY()) < 10) && entry.getValue() !=1){
						client.changeDirection(Direction.RIGHT);
						//System.out.println("occupying chair");
						entry.setValue(1);
						entry.getKey().setBackground(Color.BLACK);
						
					}
				}finally{
					checkSeatsLock.unlock();
				}
				if((Math.abs(entry.getKey().getX() - client.getPosition().getX()+18) < 10) )
					client.changeDirection(Direction.STOP);
				
			}	
			synchronized(SharedData.getInstance().getIsClient()){
				SharedData.getInstance().getIsClient().notifyAll();
			}
		}while(client.getDirection() != Direction.STOP);
		
	}
	public void joinShavingQueue(Client cl){
		shavingLock.lock();
		try{
			//System.out.println("shavingQueue");
			shavingQueue.add(cl);
		}finally{
			shavingLock.unlock();
		}
	}
	
	public void joinHaircutQueue(Client cl){
		haircutLock.lock();
		try{
			//System.out.println("haircutQueue");
			haircutQueue.add(cl);
		}finally{
			haircutLock.unlock();
		}
	}
	
	public void joinStylingQueue(Client cl){
		stylingLock.lock();
		try{
			//System.out.println("stylingQueue");
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
		//System.out.println("Leaving ShavingQueue");
		/*for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
			
			if(Math.abs(entry.getKey().getY()+40-cl.getPosition().getY())<20){
				entry.setValue(0);
			}
			System.out.println(entry.getValue());
		}*/
		
		}finally{
			shavingLock.unlock();
		}
		
		
	}
	
	public  void leaveHaircutQueue(Hairdresser h, JPanel chair){
		Client cl =null;
		
		/*haircutLock.lock();
		//chairToSeat = h.getOccupiedChair();
		try{*/
		
		cl = haircutQueue.remove(0);
		cl.setHairdresserToFollow(h);
		cl.setChairToSeat(chair);
		cl.setCalled(true);
		
		/*for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
			if(Math.abs(entry.getKey().getY()+40-cl.getPosition().getY())<30){
				entry.setValue(0);
			}
			System.out.println(entry.getValue());
		}*/
		
		//System.out.println("ZWolnione zosta³o miejsce haircut");
		//cl.changeDirection(Direction.LEFT)
		
		
		/*}finally{
			haircutLock.unlock();
		}*/
	}
	
	public  void leaveStylingQueue(Hairdresser h, JPanel chair){
		Client cl =null;
		
		/*stylingLock.lock();
		//chairToSeat = h.getOccupiedChair();
		try{*/
		cl = stylingQueue.remove(0);
		
		cl.setHairdresserToFollow(h);
		cl.setChairToSeat(chair);
		cl.setCalled(true);
		
		
		
		//System.out.println("ZWolnione zosta³o miejsce styling");
		//cl.changeDirection(Direction.LEFT);
		
		
		/*
		}finally{
			stylingLock.unlock();
		}*/
	}
	
	public  void followHairdresser( Client cl){
		
		cl.changeDirection(Direction.LEFT);
		/*do{
			System.out.print("");
		}while(Math.abs(900 - cl.getPosition().getX())>30);*/
		checkSeatsLock.lock();
		try{
			for(Entry<JPanel, Integer> entry : waitingRoomChairs.entrySet()){
				if(Math.abs(entry.getKey().getY()+50-cl.getPosition().getY())<30){
						entry.setValue(0);
						entry.getKey().setBackground(Color.PINK);
					//System.out.println(entry.getKey()+", "+entry.getValue());
				}
				
				System.out.println(entry.getValue());
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
		try {
			cl.sleep(cl.getTimeForSerivce());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
