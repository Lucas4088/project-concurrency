package my;

import javax.swing.*;
import java.awt.Canvas;

public class GUI extends JFrame implements Runnable {
	public GUI() {
		getContentPane().setLayout(null);
		
		Canvas canvas = new Canvas();
		canvas.setBounds(0, 0, 434, 261);
		getContentPane().add(canvas);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
