package my;


public class Test {
	public static void main(String[] args) {
		Thread t = new Thread(new GUI());
		t.start();
	
		Client cl1 = new Client(100,100);
		SharedData.getInstance().addClient(cl1);
		new Thread(cl1).start();
		
		
		Hairdresser h1 = new Hairdresser(200, 200);
		SharedData.getInstance().addHairdresser(h1);
		new Thread(h1).start();
		
		
	}
	
	
}
