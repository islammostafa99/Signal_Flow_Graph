package view;

import java.util.*;
import java.awt.geom.*;
import java.awt.*;

import javax.swing.*;

public class VEdge {
	
	private VNode from , to ;
	private double gain ;
	private QuadCurve2D.Double myArc ;
	private Random generator ;
	private int rX , rY , wX , wY , mX , mY ;
	private int midX ;
	private int midY ;
	
	public VEdge( VNode s , VNode e , double w){
		from = s ;
		to = e ;
		generator = new Random();
		rY = generator.nextInt()%150  ;
		rX = generator.nextInt()%150  ;
		gain = w ;
		
		refresh();
	}
	
	public int getMidX(){
		return mX ;
	}
	
	public int getMidY(){
		return mY ;
	}
	
	public int getB4MidX(){
		calculate();
		return wX ;
	}
	
	private void calculate() {
		PathIterator pit = myArc.getPathIterator(null, 1);
		int count = 0 ;
		double[] coords = new double[2];
	    while(!pit.isDone()) {
	    	++count ;
	        pit.next();
	    }
	    PathIterator x = myArc.getPathIterator(null, 1);
	    int a = count/2 ; int b = a+1 ;
	    count = 0 ;
	    while(!x.isDone()) {
	    	++count ;
	    	x.currentSegment(coords);
	    	if( count == a ){
	    		mX = (int)coords[0];
	    		mY = (int)coords[1];
	    	}
	    	else if( count == b ){
	    		wX = (int)coords[0];
	    		wY = (int)coords[1];
	    	}
	        x.next();
	    }
	}
	
	public Shape getBound(){
		return myArc.getBounds();
	}

	public int getB4MidY(){
		calculate();
		return wY ;
	}
	
	public VNode getFrom(){
		return from ;
	}
	
	public VNode getTo(){
		return to ;
	}
	
	void setGaint( int arg ){
		gain = arg ;
	}
	
	public double getGain(){
		return gain ;
	}
	
	public void refresh(){
		
		midX = (from.getX()+to.getX())/2+rX ;
		midY = (from.getY()+to.getY())/2+rY ;
		
		myArc = new QuadCurve2D.Double(from.getX(),from.getY(),midX,midY,to.getX(),to.getY());
		
		calculate();
	}
	
	public void move( int x  , int y ){
		myArc = new QuadCurve2D.Double(from.getX(),from.getY(),x,y,to.getX(),to.getY());
	}
	
	public QuadCurve2D.Double getShape(){
		return myArc ;
	}
}
