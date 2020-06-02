package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class VNode {
	private Ellipse2D.Double myCircle ;
	private int centerX , centerY , radius ;
	private Rectangle2D frame ;
	private Color fill , stroke ;
	
	
	public VNode( int cX , int cY ){
		radius = 23 ;
		centerX = cX ;
		centerY = cY ;
		myCircle = new Ellipse2D.Double();
        myCircle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
        setFillColor( Color.BLUE );
        setStrokeColor( Color.DARK_GRAY );
	}
	
	public Shape dashRectangle(){
		float[] dash = { 1F, 7F };  
		Stroke dashedStroke = new BasicStroke( 2F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 3F, dash, 0F ); 
		return dashedStroke.createStrokedShape( getFrame() );
	}
	
	public Shape getBounded() {
		setFrame( myCircle.getBounds2D() ) ;
		return dashRectangle();
	}
	
	public  Rectangle2D getFrame(){
		return frame ;
	}
	
	public Ellipse2D.Double getShape(){
		return myCircle;
	}
	
	public boolean contains(int x, int y) {
		return myCircle.contains(x, y);
	}
	
	protected void setFrame( Rectangle2D arg ){
		frame = arg ;
	}
	
	public Color getStrokeColor(){
		return stroke ;
	}
	
	public GradientPaint getFillColor(){
		return new GradientPaint(centerX,centerY,fill,centerX+10,centerY+10,Color.white,true) ;
	}
	
	public void setStrokeColor( Color s ){
		stroke = s ;
	}
	
	public void setFillColor( Color f ){
		fill = f ;
	}
	
	public void move(int dx, int dy) {
		centerX += dx;
		centerY += dy;
		myCircle.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
	}
	
	public int getX(){
		return centerX ;
	}
	
	public int getY(){
		return centerY ;
	}
}
