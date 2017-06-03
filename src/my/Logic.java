package my;

import java.util.Random;

import javax.swing.JPanel;

public class Logic {
	
	public static void main(String[] args) {

		Random myRand = new Random();

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 SharedData.getInstance().getGui();

	         }
	      });
		

		int i =0;
		(new Hairdresser(50,20+100,Service.HAIRCUTTING)).start();
		
		for(int j=1; j<6;j+=2){
				(new Hairdresser(50,100+j*25,Service.HAIRCUTTING)).start();
		}
		
		for(int j=0; j<4;j+=2){
			(new Hairdresser(50,280+j*25,Service.SHAVING)).start();
		}
		
		for(int j=0; j<4;j+=2){
			(new Hairdresser(50,380+j*25,Service.STYLING)).start();
		}
			
		while(true){
			for( i =0;i<10;i++);
				(new Client(100,525,Service.values()[myRand.nextInt(3)])).start();

			System.out.println("");
		}
	}
	
	
}
