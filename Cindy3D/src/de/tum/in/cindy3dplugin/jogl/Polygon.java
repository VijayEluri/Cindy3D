package de.tum.in.cindy3dplugin.jogl;

import java.awt.Color;

import org.apache.commons.math.geometry.Vector3D;

public class Polygon extends Primitive {
	Vector3D positions[];
	Vector3D normals[];

	public Polygon(double[][] positions, double[][] normals,
			Color color, double opacity) {
		super(color, opacity);
		this.positions = new Vector3D[positions.length];
		this.normals = new Vector3D[positions.length];

		for (int i = 0; i < positions.length; ++i) {
			this.positions[i] = new Vector3D(positions[i][0], positions[i][1],
					positions[i][2]);
		}
		
		if (normals != null) {
			for (int i = 0; i < positions.length; ++i) {
				this.normals[i] = new Vector3D(normals[i][0], normals[i][1],
						normals[i][2]);
			}
		} else {
			Vector3D v1 = this.positions[2].subtract(this.positions[0]);
			Vector3D v2 = this.positions[1].subtract(this.positions[0]);
			Vector3D normal = Vector3D.crossProduct(v2, v1).normalize();

			for (int i = 0; i < this.normals.length; ++i) {
				this.normals[i] = normal;
			}
		}
	}

	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < positions.length; ++i)
			str += positions[i].toString();
		str += "]";
		return str;
	}
}