package model;

import java.util.ArrayList;

import processing.core.PApplet;
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

	private EquilateralGrid(int resolution) {
		res = resolution;

		
		tri = new Triangle2D();
		subGrids = new ArrayList<EquilateralGrid>();
		
	}

	/**
	 * add an equilateral grid to this grid
	 * 
	 * @param location
	 *            the location of the sub grid within the parent. The coordinates are specified
	 *            as un-normalized integer values in terms of the parent triangles Resolution. (@see getRes())
	 * @param size
	 *            the number of grid points in the parent the subgrid should
	 *            span
	 * @return the equilateral grid created
	 */
	public EquilateralGrid addGrid(Vec3D location, int resolution) {
		EquilateralGrid result = constructGrid(resolution);
		float sidelen = 1.0f * resolution / this.res;

		// scale this in relation to the parent
		Vec3D baryvertex0 = location.scale(1.0f/getRes());
		Vec3D baryvertex1 = baryvertex0.add(new Vec3D(-sidelen, sidelen, 0));
		Vec3D baryvertex2 = baryvertex0.add(new Vec3D(-sidelen, 0, sidelen));

		// get the coordinates of the subtriangle in X,Y of the parent
		Vec2D A = this.tri.fromBarycentric(baryvertex0);
		Vec2D B = this.tri.fromBarycentric(baryvertex1);
		Vec2D C = this.tri.fromBarycentric(baryvertex2);

		result.setVerts(A, B, C);
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
	 * move this grid relative to the coordinate system of another grid. The method assumes that
	 * the current barycentric coordinates of the grid are in terms of the g.
	 * 
	 * @param g
	 * 			The parent grid to move relative to
	 * @param loc
	 * 			BaryCentric coordinates in the parent grid system
	 */
	public void moveGridRelativeTo(EquilateralGrid g, Vec3D loc) {	
//		Vec3D dLoc = tri.getCentroid();
	}

	
	public Vec3D[] getNonNormalizedVertices() {
		float r = getRes();
		Vec3D[] result = { new Vec3D(r,0,0), new Vec3D(0,r,0), new Vec3D(0,0,r)};
		return result;
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

	public Vec2D fromBarycentric(Vec3D b) {
		return tri.fromBarycentric(b);
	}

	public void setVerts(Vec2D a, Vec2D b, Vec2D c) {
		tri.set(a, b, c);
	}

	/**
	 * construct an grid where [0,-1] defines the Apex. Vertices B and C are
	 * rotate 120 and -120 degrees from the Apex.
	 * 
	 * @param resolution
	 * @return
	 */
	public static EquilateralGrid constructGrid(int resolution) {
		EquilateralGrid result = new EquilateralGrid(resolution);

		Vec2D A = new Vec2D(0, -1);
		// this just does a rotation of the v1 vector positive 120 to get v2 and
		// negative 120 to get v3
		Vec2D B = new Vec2D(A.x * cos120 + A.y * sin120, -A.x * sin120 + A.y
				* cos120);
		Vec2D C = new Vec2D(A.x * negcos120 + A.y * negsin120, -A.x * negsin120
				+ A.y * negcos120);
		result.setVerts(A, B, C);		

		return result;
	}

}
