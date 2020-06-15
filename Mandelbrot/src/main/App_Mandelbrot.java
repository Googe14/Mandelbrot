package main;

import javax.swing.JFrame;

public class App_Mandelbrot {

	public static void main(String[] args) {
		
		int[] res = {
			512, 512	
		};
		int samples = 1000;
		
		Window frame = new Window(res, samples);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Mandelbrot");
		
		frame.setVisible(true);
		
	}
	
}
