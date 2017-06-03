package my;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.*;

public class SharedData {
    private static SharedData ourInstance = new SharedData();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Hairdresser> hairdressers = new ArrayList<>();
    private GUI gui = new GUI();

    private ChairRoom chairRoom = new ChairRoom(gui.getChairRoomChairs());
    private WaitingRoom waitingRoom = new WaitingRoom(gui.getWaitingChairs());

    

    private int maxWindowHeight =500;
    private int maxWindowWidth = 1000;
    

    
    public static SharedData getInstance() {
        return ourInstance;
    }

    private SharedData() {
    }
    
    public GUI getGui(){
    	return gui;
    }
   
    public void addClient(Client c){
    	clients.add(c);
    }
    
    public void addHairdresser(Hairdresser h){
    	hairdressers.add(h);
    }
    
    public ArrayList<Client> getClients(){
    	return clients;
    }
    
    public ArrayList<Hairdresser> getHairdressers(){
    	return hairdressers;
    }
    
    public void setChairRoom(ChairRoom cR){
    	chairRoom = cR;
    }
    
    public ChairRoom getChairRoom(){
    	return chairRoom;
    }
 
    public void setWaitingRoom(WaitingRoom wR){
    	waitingRoom = wR;
    }
    
    public WaitingRoom getWaitingRoom(){
    	return waitingRoom;
    }
    
    public int getWindowWidth(){
    	return maxWindowWidth;
    }
    
    public int getWindowHeight(){
    	return maxWindowHeight;
    }
    

}
