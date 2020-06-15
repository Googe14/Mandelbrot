package display;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Loader {

public int[] loadQuad() {
		
		float[] pos = {
			1, 1, 0,
			-1, 1, 0,
			-1, -1, 0,
			
			1, 1, 0,
			-1, -1, 0,
			1, -1, 0
		};

		float[] tex = {
			1, 1,
			0, 1,
			0, 0,
			
			1, 1,
			0, 0,
			1, 0
		};
		
		return loadToVAO(pos, tex);
		
	}	
	
	
	//Create a model object and load all necessary data into a vao, ready to render
		public int[] loadToVAO(float[] positions, float[] tex) {
			
			int[] vao = new int[1];
			int[] vbo = new int[2];
			
			createVAO(vao); //Create a vao object
			createVBO(vbo); //Create the vbos
			
			storeInAttributeList(vbo, 0, 3, positions); //Store position data
			
			storeTexture(vbo, tex); //Store texture coordinates
			
			unbindVAO(); //Unbind vao
			
			return vao; //Return model that has a full vao
		}
		
		
		private int[] createVAO(int[] vao) {   //Create vao
			GL4 gl = (GL4)GLContext.getCurrentGL();
			
			gl.glGenVertexArrays(vao.length, vao, 0); //Create vao object
			gl.glBindVertexArray(vao[0]); //Bind it for later use
			
			return vao;
		}
		
		private void storeTexture(int[] vbo, float[] tex) {    //Store texture coordinates
			storeInAttributeList(vbo, 1, 2, tex);   //Store texture coordinates
			//First number is 1 because I have defined 1 to be the buffer to store texture data
			//Second number is 2 because they are only x,y coordinates, not x,y,z like the positions
		}
		
		private int[] createVBO(int[] vbo) {
			GL4 gl = (GL4)GLContext.getCurrentGL();

			gl.glGenBuffers(vbo.length, vbo, 0); //Create 5 vbos
			return vbo;
		}
		
		private void storeInAttributeList(int[] vbo, int attributeNumber, int dimensions, float[] data) {
			GL4 gl = (GL4)GLContext.getCurrentGL();
			
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[attributeNumber]);  //Bind current vbo
			FloatBuffer dataBuf = Buffers.newDirectFloatBuffer(data); //Convert array into Float buffer
			gl.glBufferData(GL_ARRAY_BUFFER, dataBuf.limit()*4, dataBuf, GL_STATIC_DRAW); //Store data into VBO
			
			gl.glVertexAttribPointer(attributeNumber, dimensions, GL_FLOAT, false, 0, 0); //Define the vbo 
			gl.glEnableVertexAttribArray(attributeNumber);	//Enable this vbo

		}
		
		private void unbindVAO() {
			GL4 gl = (GL4)GLContext.getCurrentGL();
			gl.glBindVertexArray(0); //Bind null vao
		}
	
}
