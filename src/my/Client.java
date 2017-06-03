package my;

import java.util.Random;

public class Client extends Thread implements Person {
	private Service serviceType;
	private int timeForService;
	private Position pos;
	private Direction dir;
	private boolean entered;
	private WaitingRoom waitingRoom;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(pos.getX() < SharedData.getInstance().getWindowWidth()-40){
			
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Math.abs(pos.getX()-800) <20 && !entered){
				//dir = Direction.TOP;
				if(waitingRoom.acquireWaitingRoomChair(this)==1){
					entered = true;
				}
			}
			
			if(entered)
				waitingRoom.seatOnChair(this);
		}
	}
	
	public Client(int x,int y, Service sType) {
		SharedData.getInstance().addClient(this);
		waitingRoom = SharedData.getInstance().getWaitingRoom();
		pos = new Position(x,y);
		dir = Direction.RIGHT;
		entered = false;
		serviceType = sType;
		try {
			sleep((new Random()).nextInt(3000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPosition(int x,int y){
		pos.setPosition(x, y);
	}
	
	public Position getPosition(){
		return pos;
	}

	public void move(){
		switch(dir){
		case TOP : if ( pos.getY() > 40) pos.setY(pos.getY()-5);
		break;
		case BOTTOM : if ( pos.getY() < SharedData.getInstance().getWindowHeight()+50) pos.setY(pos.getY()+5);
		break;
		case LEFT : if ( pos.getX() > 10) pos.setX(pos.getX()-5);
		break;
		case RIGHT : if ( pos.getX() < SharedData.getInstance().getWindowWidth()) pos.setX(pos.getX()+5);
		break;
		case STOP : ;
		break;
		}
	}

	@Override
	public void changeDirection(Direction dir) {
		this.dir = dir;
	}

	
	public void stopMoving() {
		dir = Direction.STOP;
	}
	
	public Service getServiceType(){
		return serviceType;
	}
	
}
