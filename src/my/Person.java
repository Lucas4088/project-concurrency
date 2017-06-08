package my;

public class Person {
	protected  Position pos;
	protected Direction dir;
	
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
		case RIGHT : if ( pos.getX() < SharedData.getInstance().getWindowWidth()) pos.setX(pos.getX()+5);
		break;
		case STOP : ;
		break;
		}
	}
	public void changeDirection(Direction dir){
		this.dir = dir;
	}
	
	public Direction getDirection() {
		return dir;
	}

}
