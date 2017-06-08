package my;

import java.util.Random;

import javax.swing.JPanel;

public class Client extends Person implements Runnable {
	private Service serviceType;
	private int timeForService;
	private boolean entered;
	private boolean called;
	private WaitingRoom waitingRoom;
	private Hairdresser hairdresserToFollow;
	private JPanel chairToSeat;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(pos.getX() < SharedData.getInstance().getWindowWidth()-40){
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Math.abs(pos.getX()-800) <20 && !entered){
				//dir = Direction.TOP;
				waitingRoom.acquireWaitingRoomChair(this);
			}
			
			if(called){
				entered = false;
				waitingRoom.followHairdresser(this);
				//System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
			}
			
		}
		SharedData.getInstance().removeClient(this);
	}
	
	public Client(int x,int y, Service sType) {
		
		SharedData.getInstance().addClient(this);
		waitingRoom = SharedData.getInstance().getWaitingRoom();
		pos = new Position(x,y);
		dir = Direction.RIGHT;
		entered = false;
		called = false;
		serviceType = sType;
		timeForService = 7000 + (new Random()).nextInt(10000);
		try {
			Thread.sleep((new Random()).nextInt(300));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sleep(){
		//this.sleep(time);
		try {
			Thread.currentThread();
			Thread.sleep(timeForService);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Service getServiceType(){
		return serviceType;
	}
	
	public void setCalled(boolean call){

		called = call;
	}
	
	public void setChairToSeat(JPanel chair){
		chairToSeat = chair;
	}
	
	public void setHairdresserToFollow(Hairdresser h){
		hairdresserToFollow = h;
	}
	
	public JPanel getChairToSeat(){
		return chairToSeat;
	}
	
	public Hairdresser getHairdresserToFollow(){
		return hairdresserToFollow;
	}
	public void setEntered(boolean enter){
		entered = enter;
	}
	public boolean getCalled(){
		return called;
	}
	
	public int getTimeForSerivce(){
		return timeForService;
	}
	
}
