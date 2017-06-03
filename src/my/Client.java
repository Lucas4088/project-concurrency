package my;



public class Client implements Runnable {
	private int serviceType;
	private int timeForService;
	private Position pos;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public Client(int x,int y) {
		pos = new Position(x,y);

	}
	
	public void setPos(int x,int y){
		pos.setPosition(x, y);
	}
	
	public Position getPosition(){
		return pos;
	}

	public void move(){
		
	}
	
}
