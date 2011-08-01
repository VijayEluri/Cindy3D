package de.tum.in.cindy3dplugin.jogl.lighting;

import javax.media.opengl.GL2;

import org.apache.commons.math.geometry.Vector3D;

import de.tum.in.cindy3dplugin.jogl.lighting.LightManager.LightType;

public class PointLight extends Light {
	
	private Vector3D position = new Vector3D(0, 0, 0);
	
	public void setPosition(Vector3D position) {
		this.position = position;
	}
	
	@Override
	public void setGLState(GL2 gl, int light) {
		super.setGLState(gl, light);
		
		gl.glLightfv(light, GL2.GL_POSITION,
				new float[]{(float) position.getX(), (float) position.getY(),
				(float) position.getZ(), 1.0f}, 0);
	}

	@Override
	public String getShaderFillIn(int light) {
		return "pointLight(" + light + ", normal, eye, ecPoint);";
	}

	@Override
	public LightType getType() {
		return LightType.POINT_LIGHT;
	}
}
