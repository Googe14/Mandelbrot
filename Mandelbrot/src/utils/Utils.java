package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.Vector;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.TextureIO;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import textures.Texture;
import textures.TextureData;

public class Utils {

	public static void printWorkGroupSizes() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		int[][] workGroupCount = new int[3][1];
		
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0, workGroupCount[0], 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1, workGroupCount[1], 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2, workGroupCount[2], 0);
		
		System.out.printf("max global (total) work group size x:%d y:%d z:%d\n",
				  workGroupCount[0][0], workGroupCount[1][0], workGroupCount[2][0]);
		
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0, workGroupCount[0], 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1, workGroupCount[1], 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2, workGroupCount[2], 0);
		
		System.out.printf("max global (total) work group size x:%d y:%d z:%d\n",
				  workGroupCount[0][0], workGroupCount[1][0], workGroupCount[2][0]);
		
		int num[] = new int[1];
		gl.glGetIntegerv(GL4.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS, num, 0);
		System.out.printf("max local work group invocations %d\n", num[0]);
	}
	private static String[] src = {
		"#version 430 \n",
	
		"layout(local_size_x = 1,local_size_y = 1) in; \n",
	
		"layout(rgba32f, binding = 0) uniform image2D img_out; \n",
	
		"void main(void) { \n",
	
			"vec2 pixCoords = ivec2(gl_GlobalInvocationID.xy); \n",
	
			"vec4 pixel = vec4(1, 0, 1, 1); \n",
	
			"imageStore(img_out, pixCoords, pixel); \n",
		"}"
	};

	
	public static int createComputeShaderProgram(String cShaderFile) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		String[] computeShaderSrc = readShaderSource(cShaderFile);
		
		int computeShader = gl.glCreateShader(GL4.GL_COMPUTE_SHADER);
		gl.glShaderSource(computeShader, computeShaderSrc.length, computeShaderSrc, null);
		gl.glCompileShader(computeShader);
		
		int shaderProg = gl.glCreateProgram();
		gl.glAttachShader(shaderProg, computeShader);
		gl.glLinkProgram(shaderProg);
		
		gl.glDeleteShader(computeShader);
		
		ErrorChecker.printShaderLog(computeShader);
		ErrorChecker.checkOpenGLError();
		
		return shaderProg;
	}
	
	public static int createShaderProgram(String vShaderFile, String fShaderFile) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		//Get source-code of shaders
		String vShaderSource[] = readShaderSource(vShaderFile);
		String fShaderSource[] = readShaderSource(fShaderFile);
		
		//Create and compile shaders from source
		int vShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
		gl.glCompileShader(vShader);
		
		int fShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
		gl.glShaderSource(fShader, fShaderSource.length, fShaderSource, null, 0);
		gl.glCompileShader(fShader);
		
		//Attach shaders to the program
		int vfProgram = gl.glCreateProgram();
		gl.glAttachShader(vfProgram, vShader);
		gl.glAttachShader(vfProgram, fShader);
		gl.glLinkProgram(vfProgram);
		
		//Clear shaders once done
		gl.glDeleteShader(vShader);
		gl.glDeleteShader(fShader);
		
		//Return ponter to program data
		return vfProgram;
	}
	
	public static String[] readShaderSource(String filename) {
		
		//Stores lines of code from program file
		Vector<String> lines = new Vector<String>();
		Scanner sc;
		//Array to store actual program
		String[] program;
		
		try {
			//Open file
			sc = new Scanner(new File("src/"+filename));
			while(sc.hasNext()) {
				//Add each line read to the 
				lines.addElement(sc.nextLine());
			}
			//Create program String[]
			program = new String[lines.size()];
			for(int i = 0; i < lines.size(); i++) {
				//Add all the recorded lines to it
				program[i] = (String) lines.elementAt(i) + "\n";
			}
			
		} catch(IOException e) {
			System.err.println("IOException reading file: " + e);
			return null;
		}
		//Close file
		sc.close();
		
		return program;
	}
	
	//Load texture from a file
	public static int loadTexture(String textureFileName) {
		GL4 gl = (GL4)GLContext.getCurrentGL();
		//Create objects to hold texture data
		com.jogamp.opengl.util.texture.Texture tex;
		File textureFile = new File(textureFileName);
		//Check if texture is actually there
		if(!textureFile.exists()) {
			System.out.println("No texture file found.");
			System.exit(0);
		}
			try {
				//Read texture from file
				tex = TextureIO.newTexture(textureFile, false);
				//Create pointer to texture data
				int textureID = tex.getTextureObject();
				//Bind texture to the GPU
				gl.glBindTexture(GL4.GL_TEXTURE_2D, textureID);
				//Return pointer to texture
				return textureID;

			//Otherwise, throw error.
			} catch (Exception e) {System.out.println("Failed");}
		//If texture wasn't found, print it
		//If no texture was loaded, return 0 pointer
		return 0;
	}
			
			public static Texture loadCubeMap(String[] rightLeftTopBottomBackFront) {
				String[] textureFiles = rightLeftTopBottomBackFront;
				GL4 gl = (GL4)GLContext.getCurrentGL();
				
				gl.glEnable(GL4.GL_TEXTURE_CUBE_MAP);
				
				int[] textures = new int[1];
				gl.glGenTextures(1, textures, 0);
				
				TextureData data;
				
				gl.glBindTexture(GL4.GL_TEXTURE_CUBE_MAP, textures[0]);
			    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
			    
				data = decodeTextureFile(textureFiles[0]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());

				data = decodeTextureFile(textureFiles[1]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());
				
				data = decodeTextureFile(textureFiles[2]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());
				
				data = decodeTextureFile(textureFiles[3]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());
				
				data = decodeTextureFile(textureFiles[4]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());
				
				data = decodeTextureFile(textureFiles[5]);
				gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());

				/*
				for(int i=0; i<textureFiles.length; i++) {
					TextureData data = decodeTextureFile(textureFiles[i]);
					gl.glTexImage2D(GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL4.GL_RGBA, data.getWidth(),data.getHeight(), 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data.getBuffer());
				}
				*/
				gl.glTexParameteri(GL4.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
				gl.glTexParameteri(GL4.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
				
				return new Texture(textures[0]);
			}
			
			public static Texture loadCubeMap(String fileName) {
				return loadCubeMap(getSkies(fileName));
			}
			
			public static String[] getSkies(String file) {
				String[] skies = {
						file+"Right.png",
						file+"Left.png",
						file+"Top.png",
						file+"Bottom.png",
						file+"Back.png",
						file+"Front.png"
				};
				return skies;
			}
			
			private static TextureData decodeTextureFile(String fileName) {
				int width = 0;
				int height = 0;
				ByteBuffer buffer = null;
				try {
					FileInputStream in = new FileInputStream("resources/textures/"+fileName);
					PNGDecoder decoder = new PNGDecoder(in);
					width = decoder.getWidth();
					height = decoder.getHeight();
					buffer = ByteBuffer.allocateDirect(4 * width * height);
					decoder.decode(buffer, width * 4, Format.RGBA);
					buffer.flip();
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Tried to load texture " + fileName + ", didn't work");
					System.exit(-1);
				}
				return new TextureData(buffer, width, height);
			}
			
}
