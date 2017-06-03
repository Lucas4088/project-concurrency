package my;

import java.util.ArrayList;

public class SharedData {
    private static SharedData ourInstance = new SharedData();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Hairdresser> hairdressers = new ArrayList<>();
    
    
    private ChairRoom chairRoom;
    private WaitingRoom waitingRoom;
    
    public static SharedData getInstance() {
        return ourInstance;
    }

    private SharedData() {
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
    
}
