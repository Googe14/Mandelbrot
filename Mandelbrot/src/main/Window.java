package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import org.joml.Vector2i;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import display.shader.DisplayShader;
import renderer.Image;
import renderer.Renderer;

public class Window extends JFrame implements GLEventListener {

	private GLCanvas canvas;
	private DisplayShader display;
	private Renderer rend;
	
	private Image img = new Image();
	
	private int samples;
	
	public Window(int[] res, int samples) {
		this.samples = samples;
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		
		canvas.setPreferredSize(new Dimension(res[0], res[1]));
		this.add(canvas);
		this.pack();

		canvas.addMouseWheelListener(list);
		
		init = false;
		canvas.display();
	}
	
	private boolean init = true;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		while(init) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		display = new DisplayShader();
		display.buildShader();
		
		img.init(canvas.getWidth(), canvas.getHeight());
		
		rend = new Renderer();
		rend.maxSamples = samples;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
		rend.renderSample(img);

		display.paint(img.getID());
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		
	}

	public MouseWheelListener list = new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			int diff = arg0.getWheelRotation();
			
			int dir = -1;
			
			double mod = 0.9;
			if(diff > 0) {
				mod = 1.1;
				dir = 1;
				if(rend.zoom > 2) return;
			} else {
				if(rend.zoom < 1E-15) return;
			}
			
			double mov = 0.25*rend.zoom;
			
			double offset = mod * (double)Math.abs(diff);
			rend.zoom *= offset;
			
			Vector2i mouse = getPosition(canvas);
			Vector2i centre = new Vector2i(canvas.getWidth()/2, canvas.getHeight()/2);
			centre.sub(mouse);
			
			rend.x += ((double)centre.x/(double)canvas.getWidth())*mov*dir;
			rend.y += ((double)centre.y/(double)canvas.getHeight())*mov*-dir;
			
			canvas.display();
		}
		
	};
	
	public static Vector2i getPosition(Component comp) {
		Point mPos = MouseInfo.getPointerInfo().getLocation();
		Point cPos = comp.getLocationOnScreen();
		
		return new Vector2i(mPos.x - cPos.x, mPos.y - cPos.y);
	}
	
}
