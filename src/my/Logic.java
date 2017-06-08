package my;

import java.util.Random;


import javax.swing.*;


public class Logic {
	
	public static void main(String[] args) {

		Random myRand = new Random();

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 SharedData.getInstance().getGui();
	        	 SharedData.getInstance().getGui().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         }
	      });

		/*for(int j=1; j<6;j+=2){
				(new Hairdresser(10,100+j*25,Service.HAIRCUTTING,)).start();
		}*/
		new Thread((new Hairdresser(10,100+1*25,Service.HAIRCUTTING,"Jeden"))).start();
		new Thread((new Hairdresser(10,100+3*25,Service.HAIRCUTTING,"Dwa"))).start();
		new Thread((new Hairdresser(10,100+5*25,Service.HAIRCUTTING,"Trzy"))).start();
		/*for(int j=0; j<4;j+=2){
			(new Hairdresser(10,280+j*25,Service.SHAVING)).start();
		}*/
		new Thread((new Hairdresser(10,280+0*25,Service.SHAVING,"Cztery"))).start();
		new Thread((new Hairdresser(10,280+2*25,Service.SHAVING,"Piec"))).start();
		/*for(int j=0; j<4;j+=2){
			(new Hairdresser(10,380+j*25,Service.STYLING)).start();
		}*/
		new Thread((new Hairdresser(10,380+0*25,Service.STYLING,"Szesc"))).start();
		new Thread((new Hairdresser(10,380+2*25,Service.STYLING,"Siedem"))).start();
		
		while(true){
			
				new Thread((new Client(100,525,Service.values()[myRand.nextInt(3)]))).start();
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
}
