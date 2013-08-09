package app;

import processing.core.PApplet;
import toxi.geom.*;
import toxi.processing.*;
import view.EquilateralGridView;

import java.util.ArrayList;

import model.EquilateralGrid;


public class CultureCodes extends PApplet {
	public static final long serialVersionUID = 11234;
	
	ToxiclibsSupport gfx;

	int RES = 20;
	float triSize;
	ArrayList<Vec3D> bs = new ArrayList<Vec3D>();	
	EquilateralGrid grid, babyGrid;
	EquilateralGridView gridView;
	
	@Override
	public void setup() {
		size(1920,1080);
		noStroke();
		gfx=new ToxiclibsSupport(this);
		
		//magic number from geometry of equilateral triangle (height = 1.5; side = 1.73 )
		triSize = height * (1.73f/1.5f);
		
		println(triSize);
//		triSize =  height * .866f / 1.5f ;
		
		grid = EquilateralGrid.constructGrid(RES);
		bs = grid.getGridPoints();
		gridView = new EquilateralGridView(this, grid);
		
		
		grid.addGrid(new Vec3D(.8f,0.2f,0.f), floor(15));
		
		grid.addGrid(new Vec3D(.73f,0.27f,0.f), floor(random(1,10)));
//		grid.addGrid(new Vec3D(.8f,0.2f,0.f), floor(random(1,10)));
		
	}
	
	@Override
	public void draw() {
		background(255);
		fill(200,100,100,100);
//		translate(0.5f*width - grid.getCentroid().getComponent(0), 0.67f*height - grid.getCentroid().getComponent(1));
		translate(0.5f*width, 0.67f*height);		
		
		fill(200,200,0);
				
		gridView.draw(gfx,triSize);			
		
		fill(100,0,0);				
		for( Vec3D b : bs) {
			Vec2D pt = grid.fromBarycentric(b);
			ellipse(pt.x, pt.y,1.0f/triSize, 1.0f/triSize);
		}
		
		noLoop();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[]{"app.CultureCodes"});
	}

}
