package my;

import java.awt.Color;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

public class GUI extends JFrame implements Runnable, ActionListener {

	public final int MAX_WAITING_CHAIRS = 5;
	public final int MAX_CHAIRROOM_CHAIRS = 6;
	public final static int ONE_SECOND = 1000;
	
	JFrame mainFrame;
	JPanel mainPanel;

	private JPanel[] chairRoomChairs;
	private JPanel[] waitingChairs;
	private JPanel doors;
	private Lock lock;
	private Lock lock2;
	Iterator<Client> iterator;
	public GUI() {
		
		lock = new ReentrantLock();
		lock2 = new ReentrantLock();
		waitingChairs = new JPanel[MAX_WAITING_CHAIRS];
		chairRoomChairs = new JPanel[MAX_CHAIRROOM_CHAIRS];
		
		setTitle("Project");
		setSize(1000, 600);
		getContentPane().setLayout(null);

		doors = new JPanel();
		doors.setBackground(Color.BLACK);
		doors.setBounds(771, 457, 61, 10);
		add(doors);
		addChairs((JPanel)getContentPane());
		JPanel waitingRoomFloor = new JPanel();
		waitingRoomFloor.setBackground(Color.YELLOW);
		waitingRoomFloor.setBounds(748, 68, 242, 399);
		add(waitingRoomFloor);
		
		setVisible(true);
		
		Timer timer = new Timer(20, new ActionListener(){
            public void actionPerformed(ActionEvent e) 
            {
            	
            	Iterator<Client> iter1 = SharedData.getInstance().getClients().iterator();
            	while(iter1.hasNext()){
            		lock.lock();
            		try{
            		iter1.next().move();
            		}finally{
            			lock.unlock();
            		}
            	}
            	
            	Iterator<Hairdresser> iter2 = SharedData.getInstance().getHairdressers().iterator();
            	while(iter2.hasNext()){
            		lock2.lock();
            		try{
            		iter2.next().move();
            		}finally{
            			lock2.unlock();
            		}
            	}
            	
        		repaint();
            }
        });
        timer.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub	Graphics g = null;
	}
	
	
	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		super.repaint();
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		Client cl = null;
		g.setColor(Color.GRAY);
		g.fillRect(0, 500, 1000, 100);
		try{
		 iterator = SharedData.getInstance().getClients().iterator();
		while(iterator.hasNext()){
			cl = iterator.next();
			switch(cl.getServiceType()){
			case HAIRCUTTING : g.setColor(new Color(102, 255, 102));
			break;
			case SHAVING : g.setColor(new Color(102, 178, 255));
			break;
			case STYLING : g.setColor(new Color(255, 169, 102));
			break;
			}
			g.fillOval(cl.getPosition().getX(), cl.getPosition().getY(), 40, 40);
			
		}
		}catch(ConcurrentModificationException e){
			e.printStackTrace();
		}
		
		
		
		for(Hairdresser hD : SharedData.getInstance().getHairdressers()){
			switch(hD.getServiceType()){
			case HAIRCUTTING : g.setColor(new Color(0,102,0));
			break;
			case SHAVING : g.setColor(new Color(0,0,153));
			break;
			case STYLING : g.setColor(new Color(255,111,0));
			break;
			}
			g.fillOval(hD.getPosition().getX(), hD.getPosition().getY(), 40, 40);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Position getChairPos(JPanel chair){
		return new Position(chair.getX()+chair.getWidth()/2,chair.getY()+chair.getHeight()/2);
	}
	private void addChairs(JPanel panel){

		JPanel chair1 = new JPanel();
		chair1.setBounds(50, 0, 50, 50);
		chair1.setBackground(Color.PINK);
		panel.add(chair1);
		
		JPanel chair2 = new JPanel();
		chair2.setBounds(150, 0, 50, 50);
		chair2.setBackground(Color.PINK);
		panel.add(chair2);
		
		JPanel chair3 = new JPanel();
		chair3.setBounds(250, 0, 50, 50);
		chair3.setBackground(Color.PINK);
		panel.add(chair3);
		
		
		JPanel chair4 = new JPanel();
		chair4.setBounds(350, 0, 50, 50);
		chair4.setBackground(Color.PINK);
		panel.add(chair4);
		
		
		JPanel chair5 = new JPanel();
		chair5.setBounds(450, 0, 50, 50);
		chair5.setBackground(Color.PINK);
		panel.add(chair5);
		
		
		JPanel chair6 = new JPanel();
		chair6.setBounds(550, 0, 50, 50);
		chair6.setBackground(Color.PINK);
		panel.add(chair6);
		
		// waiting room
		
		JPanel chair7 = new JPanel();
		chair7.setBounds(929, 332, 50, 50);
		add(chair7);
		chair7.setBackground(Color.PINK);
				
		JPanel chair11 = new JPanel();
		chair11.setBounds(929, 79, 50, 50);
		add(chair11);
		chair11.setBackground(Color.PINK);
		
		JPanel chair10 = new JPanel();
		chair10.setBounds(929, 145, 50, 50);
		add(chair10);
		chair10.setBackground(Color.PINK);
		
		JPanel chair9 = new JPanel();
		chair9.setBounds(929, 206, 50, 50);
		add(chair9);
		chair9.setBackground(Color.PINK);
		
		JPanel chair8 = new JPanel();
		chair8.setBounds(929, 267, 50, 50);
		add(chair8);
		chair8.setBackground(Color.PINK);
		
		chairRoomChairs[0] = chair1;
		chairRoomChairs[1] = chair2;
		chairRoomChairs[2] = chair3;
		chairRoomChairs[3] = chair4;
		chairRoomChairs[4] = chair5;
		chairRoomChairs[5] = chair6;
		
		waitingChairs[0] = chair7;
		waitingChairs[1] = chair8;
		waitingChairs[2] = chair9;
		waitingChairs[3] = chair10;
		waitingChairs[4] = chair11;
		

	}
	
	public JPanel[] getWaitingChairs(){
		return waitingChairs;
	}
	
	public JPanel[] getChairRoomChairs(){
		return chairRoomChairs;
	}
}
