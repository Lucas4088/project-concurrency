package my;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class ChairRoom {
	public final int MAX_AVAILABLE_SEATS = 6;
    private Map<JPanel,Integer> chairRoomChairs = new HashMap<>();
    
    public ChairRoom(JPanel[] tab) {
    	for(int i = 0;i<MAX_AVAILABLE_SEATS ;i++ )
    	chairRoomChairs.putIfAbsent(tab[i], 0);
    }
	
	
	public void occupyChair(){
		
	}
	
	public void releaseChair(){
		
	}
	
	
}
