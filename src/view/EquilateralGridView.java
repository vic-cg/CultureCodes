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
	
	public void draw(ToxiclibsSupport gfx, float size) {		
		p.scale(size);
		p.colorMode(p.HSB,1.0f);
		float bright = 1.0f;
		p.fill(0.0f,0.8f,bright);
		gfx.triangle(grid.getTri());		
		for(EquilateralGrid e : grid.getSubGrids()) {			
			bright *= 0.9f;
			p.fill(0.0f,0.8f,bright);
			gfx.triangle(e.getTri());
		}
	}
}
