package utils;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

public class ShaderUtils {
	
	public static int getUniform(GL4 gl, int program, String name) {
		return gl.glGetUniformLocation(program, name); //Get ocation of a uniform variable
	}
	
	public static void loadFloat(int uniform, float data, GL4 gl) {
		gl.glUniform1f(uniform, data);
	}
	
	public static void loadBoolean(int uniform, boolean data, GL4 gl) {
		float toLoad = 0;
		if(data) {
			toLoad = 1;
		}
		gl.glUniform1f(uniform,  toLoad);
	}
	
	public static void loadVector(int location, Vector3f vector, GL4 gl) {
		gl.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	public static void loadVector2f(int location, Vector2f vector, GL4 gl) {
		gl.glUniform2f(location, vector.x, vector.y);
	}
	public static void loadVector2d(int location, Vector2d vector, GL4 gl) {
		gl.glUniform2d(location, vector.x, vector.y);
	}
	
	public static void loadMatrix(int location, Matrix4f mat, GL4 gl) {
		FloatBuffer vals = Buffers.newDirectFloatBuffer(16); //Create float buffer for matrix
		gl.glUniformMatrix4fv(location, 1, false, mat.get(vals));
	}
	
	public static void loadInt(int location, int in, GL4 gl) {
		gl.glUniform1i(location, in);
	}
	
	public static void loadDouble(int uniform, double data, GL4 gl) {
		gl.glUniform1d(uniform, data);
	}
	
}
