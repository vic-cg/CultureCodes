package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.EquilateralGrid.Orientation;

import toxi.geom.Vec3D;

public class Weave {
	private ArrayList<EquilateralGrid> grids;
	
	public Weave() {
		grids = new ArrayList<EquilateralGrid>();
	}
	
	public Iterator<EquilateralGrid> iterator() {
		return grids.iterator();
	}
	
	public static Weave createWeave(EquilateralGrid g, int resolution) {
		Weave w = new Weave();
		int count = g.getRes()/resolution;
		
		for(int i=0; i <= count; ++i) {
			EquilateralGrid weaveGrid = g.addGrid(new Vec3D(i,g.getRes()-i,0), resolution, EquilateralGrid.Orientation.UP);
			w.grids.add(weaveGrid);
		}
		
		return w;
	}
}
