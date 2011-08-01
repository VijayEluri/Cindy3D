package de.tum.in.cindy3dplugin.jogl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.math.geometry.Vector3D;
import org.apache.commons.math.linear.RealMatrix;

import com.jogamp.opengl.util.glsl.ShaderCode;

public class Util {
	public static float[] matrixToFloatArray(RealMatrix m) {
		int rows = m.getRowDimension();
		int cols = m.getColumnDimension();
		
		float[] result = new float[rows*cols];
		double[][] data = m.getData();
		int offset = 0;
		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < cols; ++col, ++offset) {
				result[offset] = (float) data[row][col];
			}
		}
		
		return result;
	}

	public static float[] matrixToFloatArrayTransposed(RealMatrix m) {
		int rows = m.getRowDimension();
		int cols = m.getColumnDimension();
		
		float[] result = new float[rows*cols];
		double[][] data = m.getData();
		int offset = 0;
		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < cols; ++col, ++offset) {
				result[offset] = (float) data[col][row];
			}
		}
		
		return result;
	}
	
	public static double[] vectorToDoubleArray(Vector3D v) {
		return new double[] {v.getX(), v.getY(), v.getZ()};
	}
	
	private static String shaderLightFillIn = "";
		
	public static void readShaderSource(ClassLoader context, URL url,
			StringBuffer result) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#pragma include ")) {
					String includeFile = line.substring(16).trim();
					// Try relative path first
					URL nextURL = null;
					try {
						nextURL = new URL(url, includeFile);
					} catch (MalformedURLException e) {
					}
					if (nextURL == null) {
						// Try absolute path
						try {
							nextURL = new URL(includeFile);
						} catch (MalformedURLException e) {
						}
					}
					if (nextURL == null) {
						// Fail
						throw new FileNotFoundException(
								"Can't find include file " + includeFile);
					}
					readShaderSource(context, nextURL, result);
				} else if (line.startsWith("#pragma lights")) {
					result.append(shaderLightFillIn + "\n");
				} else {
					result.append(line + "\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final String SHADER_PATH = "/de/tum/in/cindy3dplugin/resources/shader/";

	public static ShaderCode loadShader(int type, String name) {
		StringBuffer buffer = new StringBuffer();
		URL url = Util.class.getResource(SHADER_PATH + name);
		readShaderSource(Util.class.getClassLoader(), url, buffer);
		ShaderCode shader = new ShaderCode(type, 1,
				new String[][] { { buffer.toString() } });
		return shader;
	}
	
	public static Color toColor(double[] vec) {
		
		if (vec.length != 3) {
			return null;
		}
		return new Color(
				(float)Math.max(0, Math.min(1, vec[0])),
				(float)Math.max(0, Math.min(1, vec[1])),
				(float)Math.max(0, Math.min(1, vec[2])));
	}
	
	public static Vector3D toVector(double[] vec) {
		if (vec.length != 3) {
			return null;
		}
		return new Vector3D(vec[0], vec[1], vec[2]);
	}
	
	public static Color toColor(ArrayList<Double> vec) {
		if (vec.size() != 3) {
			return null;
		}
		return new Color(
				(float)Math.max(0, Math.min(1, vec.get(0))),
				(float)Math.max(0, Math.min(1, vec.get(1))),
				(float)Math.max(0, Math.min(1, vec.get(2))));
	}

	public static void setShaderLightFillIn(String shaderLightFillIn) {
		Util.shaderLightFillIn = shaderLightFillIn;
		
	}
}
