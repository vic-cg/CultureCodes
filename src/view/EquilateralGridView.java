package view;

import model.EquilateralGrid;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class EquilateralGridView {
	PApplet p;
	EquilateralGrid grid;
	
	public EquilateralGridView(PApplet p, EquilateralGrid e) {
		this.p = p;
		grid = e;
	}
	
	public void draw(ToxiclibsSupport gfx) {
		
		//ratio of side to height is 1.73:1.50; Centroid is .233 of the height or 1.73:1.00		
		p.colorMode(p.HSB,1.0f);
		float bright = 1.0f;
		p.fill(0.0f,0.8f,bright,.3f);
		gfx.triangle(grid.getTri());
		
		for(EquilateralGrid e : grid.getSubGrids()) {			
			bright *= 0.9f;
			p.fill(0.0f,0.8f,bright,0.8f);
			gfx.triangle(e.getTri());
		}
	}
}
