package renderer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Image {

	private int imageID[] = new int[1];;
	
	private int width;
	private int height;
	
	public int getID() {
		return imageID[0];
	}
	
	public void bindImage() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
		gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA32F, width, height, 0, GL4.GL_RGBA, GL4.GL_FLOAT, null);
		gl.glBindImageTexture(0, imageID[0], 0, false, 0, GL4.GL_WRITE_ONLY, GL4.GL_RGBA32F);
		
		gl.glActiveTexture(GL4.GL_TEXTURE0);
		gl.glBindImageTexture(0, imageID[0], 0, false, 0, GL4.GL_WRITE_ONLY, GL4.GL_RGBA32F);

	}
	
	public void init(int x, int y) {
		width = x;
		height = y;
		
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glGenTextures(1, imageID, 0);
		gl.glActiveTexture(GL4.GL_TEXTURE0);
		gl.glBindTexture(GL4.GL_TEXTURE_2D, imageID[0]);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
		gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA32F, x, y, 0, GL4.GL_RGBA, GL4.GL_FLOAT, null);
		gl.glBindImageTexture(0, imageID[0], 0, false, 0, GL4.GL_WRITE_ONLY, GL4.GL_RGBA32F);
	}
	
	public void init(int[] size) {
		init(size[0], size[1]);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
