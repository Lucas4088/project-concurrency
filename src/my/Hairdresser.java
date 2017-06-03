package my;

public class Hairdresser extends Thread implements Person {
	private Service serviceType;
	Position pos;
	private Direction dir;
	
	public Hairdresser(int x,int y, Service sType) {
		pos = new Position(x,y);
		dir = Direction.RIGHT;
		serviceType = sType;
		SharedData.getInstance().addHairdresser(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void checkForClient(){
		
	}
	
	public void setPosition(int x, int y){
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
		case RIGHT : if ( pos.getX() < SharedData.getInstance().getWindowWidth()-55) pos.setX(pos.getX()+4);
		break;
		case STOP : ;
		}
	}

	@Override
	public void changeDirection(Direction dir) {
		this.dir = dir;
	}

	public void stopMoving() {
		dir = Direction.STOP;
	}
	
}
