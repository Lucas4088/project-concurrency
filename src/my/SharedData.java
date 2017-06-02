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
    
}
