package utils;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.glu.GLU;

public class ErrorChecker {

	public static boolean checkOpenGLError() { //Check for errors
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		boolean foundError = false;
		//Create error checking object
		GLU glu = new GLU();
		int glErr = gl.glGetError();
		//While there's an error
		while(glErr != GL.GL_NO_ERROR) {
			//Print error
			System.out.println("glError: " + glu.gluErrorString(glErr));
			foundError = true;
			glErr = gl.glGetError();
		}
		if(!foundError) {
		}
		//Return if it found an error
		return foundError;
	}
	
	
	public static void printShaderLog(int prog) { //Check for shader errors and print log
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		IntBuffer intBuff = IntBuffer.allocate(1);
		gl.glGetShaderiv(prog, GL4.GL_COMPILE_STATUS, intBuff);
		
		if(intBuff.get(0) == GL4.GL_FALSE) {
			System.out.println("Shader couldn't compile");
		}
		else if(intBuff.get(0) == GL4.GL_TRUE) System.out.println("Shader Compiled Successfully");
		else System.out.println("Could not validate shader");
		
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[]log = null;
		
		//Get program info from program pointer
		gl.glGetShaderiv(prog,  GL4.GL_INFO_LOG_LENGTH, len, 0);
		//Check if there is a log from the last step
		if(len[0] > 0) {
			log = new byte[len[0]];
			//Get log from program
			gl.glGetShaderInfoLog(prog, len[0], chWrittn, 0, log, 0);
			//Write log to output
			System.out.println("Program Info Log: ");
			for(int i = 0; i<log.length; i++) {
				System.out.print((char)log[i]);
			}
			System.out.println();
		} else {}
	}
	
	
	public static void printProgramLog(int prog) { //Print program log
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		//
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[]log = null;
		
		//Get program info from program pointer
		gl.glGetProgramiv(prog,  GL4.GL_INFO_LOG_LENGTH, len, 0);
		//Check if there is a log from the last step
		if(len[0] > 0) {
			log = new byte[len[0]];
			//Get log from program
			gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0, log, 0);
			//Write log to output
			System.out.println("Program Info Log: ");
			for(int i = 0; i<log.length; i++) {
				System.out.print((char)log[i]);
			}
		} else {}
	}
	
}
