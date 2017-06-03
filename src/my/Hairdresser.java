package my;

public class Hairdresser implements Runnable {
	private int serviceType;
	Position pos;

	public Hairdresser(int x,int y) {
		pos = new Position(x,y);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void checkForClient(){
		
	}
	
	public void setPos(int x, int y){
		pos.setPosition(x, y);
	}
	
	public Position getPosition(){
		return pos;
	}

	
}
