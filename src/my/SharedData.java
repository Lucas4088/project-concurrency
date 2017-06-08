package my;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedData {
    private static SharedData ourInstance = new SharedData();
    private List<Client> clients = new CopyOnWriteArrayList<>();
    private ArrayList<Hairdresser> hairdressers = new ArrayList<>();
    private GUI gui = new GUI();
    private Object isHaircutClient = new Object();
    private Object isShavingClient = new Object();
    private Object isStylingClient = new Object();

    private WaitingRoom waitingRoom = new WaitingRoom(gui.getWaitingChairs());
    private ChairRoom chairRoom = new ChairRoom(gui.getChairRoomChairs(),waitingRoom);
    

    private int maxWindowHeight = 500;
    private int maxWindowWidth = 1000;
    

    
    public static SharedData getInstance() {
        return ourInstance;
    }

    private SharedData() {
    }
    
    public GUI getGui(){
    	return gui;
    }
   
    public synchronized Object getIsHaircutClient(){
    	return isHaircutClient;
    }
    
    public synchronized Object getIsShavingClient(){
    	return isShavingClient;
    }
    
    public synchronized Object getIsStylingClient(){
    	return isStylingClient;
    }
    
    public synchronized void addClient(Client c){
    	clients.add(c);
    }
    
    public synchronized void removeClient(Client c){
    	clients.remove(c);
    }
    
    public void addHairdresser(Hairdresser h){
    	hairdressers.add(h);
    }
    
    public List<Client> getClients(){
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
