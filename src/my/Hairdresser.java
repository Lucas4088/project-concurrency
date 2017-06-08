package my;

import javax.swing.JPanel;

public class Hairdresser extends Person implements Runnable {
	private Service serviceType;
	private ChairRoom chairRoom;
	private boolean working;
	private boolean done;
	private Position initialPosition;
	private JPanel occupiedChair;
	public String name;
	
	public Hairdresser(int x,int y, Service sType, String n) {
		name = n;
		working = false;
		pos = new Position(x,y);
		initialPosition = new Position(x,y);
		dir = Direction.STOP;
		serviceType = sType;
		SharedData.getInstance().addHairdresser(this);
		chairRoom = SharedData.getInstance().getChairRoom();
		done = false;
	}
	
	@Override
	public void run() {
		    
		// TODO Auto-generated method stub
		while(true){
			if(!working){
			System.out.println(name + " goes for a client");
			chairRoom.checkItsQueue(this);
			System.out.println(name + " after checking queue");
			}else if(done){
				System.out.println(name + " goes back to its position");
				chairRoom.goToItsPosition(this);
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setOccupiedChair(JPanel ch){
		occupiedChair = ch;
	}
	
	public JPanel getOccupiedChair(){
		return occupiedChair;
	}
	
	public Service getServiceType(){
		return serviceType;
	}
	
	public void setWorking(boolean w){
		working = w;
	}
	
	public boolean getWorking(){
		return working;
	}
	
	public void setDone(boolean d){
		done = d;
	}
	
	public boolean getDone(){
		return done;
	}
	
	public Position getInitialPosition(){
		return initialPosition;
	}
}
