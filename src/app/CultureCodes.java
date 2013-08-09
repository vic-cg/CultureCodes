package app;

import processing.core.PApplet;
import toxi.geom.*;
import toxi.processing.*;
import view.EquilateralGridView;

import java.util.ArrayList;
import java.util.Iterator;

import model.EquilateralGrid;
import model.Weave;


public class CultureCodes extends PApplet {
	public static final long serialVersionUID = 11234;
	
	ToxiclibsSupport gfx;

	int RES = 5;
	float triHeight;
	ArrayList<Vec3D> bs = new ArrayList<Vec3D>();	
	EquilateralGrid grid, babyGrid, destGrid;
	EquilateralGridView gridView;
	Weave w;
	
	@Override
	public void setup() {
		size(800,600);
		noStroke();
		gfx=new ToxiclibsSupport(this);
		
		triHeight = height / 1.5f;
		
		grid = EquilateralGrid.constructGrid(RES, EquilateralGrid.Orientation.UP);
		bs = grid.getGridPoints();
		gridView = new EquilateralGridView(this, grid);
		
		
		babyGrid = grid.addGrid(new Vec3D(0,4,1), floor(1), EquilateralGrid.Orientation.DOWN);
//		destGrid = grid.addGrid(new Vec3D(19,1,0), floor(2));
		
		w = Weave.createWeave(grid, 1);
		
	}
	
	@Override
	public void draw() {
		background(255);
		fill(200,100,100,100);
//		translate(0.5f*width - grid.getCentroid().getComponent(0), 0.67f*height - grid.getCentroid().getComponent(1));
		translate(0.5f * width, 0.667f*height);
		
//		Vec3D p = new Vec3D(10,0,0);
//		Vec3D end = new Vec3D(-5,15,0);
//		Vec3D t = p.interpolateTo(end, frameCount % 400 / 400.0f ); 
//		babyGrid.moveRelativeTo(grid, t);
		
//		float t = map(mouseX,0,width,0,1.0f);
//		Vec3D current = babyGrid.getApexInParentSpace(grid);
//		
//		Vec3D beg = new Vec3D(16,4,0);
//		Vec3D end = new Vec3D(19,1,0);
//		Vec3D newApex =  beg.interpolateTo(end, t);
//		babyGrid.moveRelativeTo(grid, newApex);
//		
//		if(frameCount % 90 == 0) {
//			int a = floor(random(0,grid.getRes()));
//			int b = floor(random(0,grid.getRes() - a));
//			int c = max(grid.getRes() - a - b, 0);
//			Vec3D move = new Vec3D(a,b,c);
//			
//			babyGrid.moveRelativeTo(grid, move);
//		}
		
		fill(200,200,0);
		pushMatrix();
		pushStyle();
		scale(triHeight); 
		gridView.draw(gfx);
		popStyle();
		popMatrix();
		
		//draw grid points
		fill(100,0,0);		
		noStroke();
		scale(triHeight);
		for( Vec3D b : bs) {
			Vec2D pt = grid.fromBarycentric(b);
			ellipse(pt.x, pt.y,10.f/triHeight, 10.f/triHeight);
		}

//
//		translate(.866f / 1.5f * height, -.333333f*height );		
//		fill(0,100);
//		pushMatrix();
//		scale(-triHeight);
//		noStroke();
//		gridView.draw(gfx);
//		popMatrix();
//		ellipse(0,0,3,3);
		
//		noLoop();
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[]{"app.CultureCodes"});
	}

}
