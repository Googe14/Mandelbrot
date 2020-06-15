package renderer;

import org.joml.Vector2d;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import utils.ErrorChecker;
import utils.ShaderUtils;
import utils.Utils;

public class Renderer {
	
	public Renderer() {
		shader = Utils.createComputeShaderProgram("renderer/mandelbrot.glsl");
	}
	
	public double zoom = 1;
	public double x = 0;
	public double y = 0;
	
	private int shader;
	
	public int maxSamples = 100;
	
	public void renderSample(Image img) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glUseProgram(shader);
		
		img.bindImage();
		
		bindData(img);
		
		gl.glDispatchCompute(img.getWidth()/32, img.getHeight(), 32);
		gl.glMemoryBarrier(GL4.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
		
		ErrorChecker.printProgramLog(shader);
		ErrorChecker.checkOpenGLError();
		
	}
	
	private void bindData(Image img) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		int uzoom = ShaderUtils.getUniform(gl, shader, "zoom");
		int ux = ShaderUtils.getUniform(gl, shader, "xd");
		int uy = ShaderUtils.getUniform(gl, shader, "yd");

		ShaderUtils.loadDouble(uzoom, zoom, gl);
		ShaderUtils.loadDouble(ux, x, gl);
		ShaderUtils.loadDouble(uy, y, gl);
		
		int res = ShaderUtils.getUniform(gl, shader, "res");
		ShaderUtils.loadVector2d(res, new Vector2d(img.getWidth(), img.getHeight()), gl);

		int samples = ShaderUtils.getUniform(gl, shader, "samples");
		ShaderUtils.loadInt(samples, maxSamples, gl);
	}
	
}
