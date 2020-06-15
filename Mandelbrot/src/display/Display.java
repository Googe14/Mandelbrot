package display;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

import display.shader.DisplayShader;
import renderer.Image;

public class Display extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private DisplayShader display = new DisplayShader();
	
	public Display(int[] dims, GLCanvas canvas) {
		this.setSize(dims[0], dims[1]);
		this.add(canvas);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init() {
		display.buildShader();
	}
	
	public void display(Image img) {
		display.paint(img.getID());
	}
	
}
