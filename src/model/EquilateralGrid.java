package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.Polygon2D;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class EquilateralGrid { // this is essentially a Proxy for a Triangle2D

	static final float cos120;
	static final float negcos120;
	static final float sin120;
	static final float negsin120;
	static final PApplet p = new PApplet();

	static {
		cos120 = p.cos(p.radians(120));
		sin120 = p.sin(p.radians(120));
		negcos120 = p.cos(p.radians(-120));
		negsin120 = p.sin(p.radians(-120));
	}

	private int res;
	private Triangle2D tri;
	private ArrayList<EquilateralGrid> subGrids;
	Orientation orientation;

	// private Vec3D

	public static enum Orientation {
		UP(), DOWN();
	}

	private EquilateralGrid(int resolution) {
		res = resolution;

		tri = new Triangle2D();
		subGrids = new ArrayList<EquilateralGrid>();
		orientation = Orientation.UP;
	}

	/**
	 * add an equilateral grid to this grid
	 * 
	 * @param location
	 *            the location of the sub grid within the parent. The
	 *            coordinates are specified as un-normalized integer values in
	 *            terms of the parent triangles Resolution. (@see getRes())
	 * @param size
	 *            the number of grid points in the parent the subgrid should
	 *            span
	 * @return the equilateral grid created
	 */
	public EquilateralGrid addGrid(Vec3D location, int resolution, Orientation o) {
		EquilateralGrid result = constructGrid(resolution, o);

		result.setPositionRelativeTo(this, location);
		// float sidelen = 1.0f * resolution / this.res;
		// // scale this in relation to the parent
		// Vec3D baryvertex0 = location.scale(1.0f/getRes());
		// Vec3D baryvertex1 = baryvertex0.add(new Vec3D(-sidelen, sidelen, 0));
		// Vec3D baryvertex2 = baryvertex0.add(new Vec3D(-sidelen, 0, sidelen));
		//
		// // get the coordinates of the subtriangle in X,Y of the parent
		// Vec2D A = this.tri.fromBarycentric(baryvertex0);
		// Vec2D B = this.tri.fromBarycentric(baryvertex1);
		// Vec2D C = this.tri.fromBarycentric(baryvertex2);
		//
		// result.setVerts(A, B, C);
		subGrids.add(result);

		return result;
	}

	public Vec2D getCentroid() {
		return tri.computeCentroid();
	}

	public ArrayList<Vec3D> getGridPoints() {
		ArrayList<Vec3D> result = new ArrayList<Vec3D>();

		for (int i = 0; i <= res; i++) {
			float t = 1.0f / (res);
			Vec3D b1 = new Vec3D(1 - i * t, t * i, 0.0f); // going down the left
															// side (AB)
			Vec3D b2 = new Vec3D(1 - i * t, 0.0f, t * i); // going down the
															// right side (AC)

			// The number of intervals between b1 and b2
			Vec3D dB = b2.sub(b1).scale(1 / t);

			try {
				assert (dB.y == dB.z);
			} catch (Exception ex) {
				p.println("*** ERROR *** intervals for barycentric coord y and barycentric coord z do not equal.");
				ex.printStackTrace();
			}

			int count = p.abs((int) dB.y);
			for (int j = 0; j <= count; j++) {
				// figure out how many point are at this level of the triangle
				float dt = count > 0 ? 1.0f / count : 0.f;
				Vec3D barycentric = b1.interpolateTo(b2, j * dt);
				result.add(barycentric);
			}
		}

		return result;
	}

	/**
	 * return the non normalized barycentric coordinate in parent space
	 * 
	 * @param g
	 * @return
	 */
	public Vec3D getApexInParentSpace(EquilateralGrid g) {
		return g.toBarycentric(tri.a);
	}

	/**
	 * move this grid relative to the coordinate system of another grid. The
	 * method assumes that the current barycentric coordinates of the grid are
	 * in terms of the g.
	 * 
	 * @param g
	 *            The parent grid to move relative to
	 * @param location
	 *            Non-normalized BaryCentric coordinates of the destination for
	 *            the grids Apex
	 */
	public void moveRelativeTo(EquilateralGrid g, Vec3D location) {
		Vec2D destination = g
				.fromBarycentric(location.scale(1.0f / g.getRes()));
		Vec2D dPos = destination.sub(tri.a);

		tri.a.addSelf(dPos);
		tri.b.addSelf(dPos);
		tri.c.addSelf(dPos);
	}

	public void setPositionRelativeTo(EquilateralGrid g, Vec3D location) {
		float sidelen = 1.0f * this.res / g.getRes();
		Vec3D baryvertex0, baryvertex1, baryvertex2;
		Vec2D a, b, c;

		if (orientation == Orientation.UP) {
			// scale this in relation to the parent
			baryvertex0 = location.scale(1.0f / g.getRes());
			baryvertex1 = baryvertex0.add(new Vec3D(-sidelen, sidelen, 0));
			baryvertex2 = baryvertex0.add(new Vec3D(-sidelen, 0, sidelen));

			// get the coordinates of the subtriangle in X,Y of the parent
			a = this.tri.fromBarycentric(baryvertex0);
			b = this.tri.fromBarycentric(baryvertex1);
			c = this.tri.fromBarycentric(baryvertex2);
		} else {
			// scale this in relation to the parent
			baryvertex0 = location.scale(1.0f / g.getRes());
			baryvertex1 = baryvertex0.add(new Vec3D(sidelen, -sidelen, 0));
			baryvertex2 = baryvertex0.add(new Vec3D(0, -sidelen, sidelen));

			// get the coordinates of the subtriangle in X,Y of the parent
			a = this.tri.fromBarycentric(baryvertex0);
			b = this.tri.fromBarycentric(baryvertex1);
			c = this.tri.fromBarycentric(baryvertex2);			
		}
		
		this.setVerts(a, b, c);
	}

	public int getRes() {
		return res;
	}

	public ArrayList<EquilateralGrid> getSubGrids() {
		return subGrids;
	}

	public Triangle2D getTri() {
		return tri;
	}

	public Vec3D toBarycentric(Vec2D p) {
		return tri.toBarycentric(p);
	}

	public Vec2D fromBarycentric(Vec3D b) {
		return tri.fromBarycentric(b);
	}

	private void setVerts(Vec2D a, Vec2D b, Vec2D c) {
		tri.set(a, b, c);
	}

	/**
	 * construct an grid where [0,-1] defines the Apex. Vertices B and C are
	 * rotate 120 and -120 degrees from the Apex.
	 * 
	 * @param resolution
	 * @return
	 */
	public static EquilateralGrid constructGrid(int resolution, Orientation o) {
		EquilateralGrid result = new EquilateralGrid(resolution);
		result.orientation = o;
		Vec2D a, b, c;

		if (result.orientation == Orientation.UP) {
			a = new Vec2D(0, -1);
			b = new Vec2D(a.x * cos120 + a.y * sin120, -a.x * sin120 + a.y
					* cos120);
			c = new Vec2D(a.x * negcos120 + a.y * negsin120, -a.x * negsin120
					+ a.y * negcos120);
		} else {
			a = new Vec2D(0, 1);
			c = new Vec2D(a.x * cos120 + a.y * sin120, -a.x * sin120 + a.y
					* cos120);
			b = new Vec2D(a.x * negcos120 + a.y * negsin120, -a.x * negsin120
					+ a.y * negcos120);
		}
		// this just does a rotation of the v1 vector positive 120 to get v2 and
		// negative 120 to get v3
		result.setVerts(a, b, c);

		return result;
	}

}
