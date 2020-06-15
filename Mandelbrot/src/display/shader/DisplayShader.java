package display.shader;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import display.Loader;
import utils.ErrorChecker;
import utils.Utils;

public class DisplayShader {

	int shader;
	private int[] vao;
	private static final int vertexCount = 6;
	
	public static final String vShaderStr = "display/shader/v_display.glsl";
	public static final String fShaderStr = "display/shader/f_display.glsl";
	
	public void paint(int texture) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glUseProgram(shader);
		
		gl.glClearColor(0.5f, 0.5f, 0.5f, 0f);
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL4.GL_DEPTH_BUFFER_BIT);
		
		gl.glBindVertexArray(vao[0]); //Tell OpenGL to use this vao
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		
		//Set texture
		gl.glActiveTexture(GL4.GL_TEXTURE0);
		gl.glBindTexture(GL4.GL_TEXTURE_2D, texture);
		
		gl.glDisable(GL4.GL_CULL_FACE);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount);
		ErrorChecker.printProgramLog(shader);
		
		gl.glUseProgram(0);
	}
	
	public void buildShader() {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		shader = Utils.createShaderProgram(vShaderStr, fShaderStr);
		
		Loader load = new Loader();
		vao = load.loadQuad();
	}
	
	
	
}
