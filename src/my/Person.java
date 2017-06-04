package my;

public interface Person {
	
	public void setPosition(int x, int y);
	public Position getPosition();
	public void move();
	public void changeDirection(Direction dir);

}
