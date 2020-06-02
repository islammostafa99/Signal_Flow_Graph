package view;

import javax.swing.*;

import Control.Control;

import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

public class Start {
	private JFrame Frame ;
	private int action = 1 ;
	final JLabel curText = new JLabel("Current Action : Select");
	private static final int selectAction = 1 ;
	private static final int addNodeAction = 2 ;
	private static final int addEdgeAction = 3 ;
	private Control cont ;
	Start(Control control){
		cont = control ;
		Frame = new JFrame();
		Frame.setSize(1100, 600);
		Frame.setResizable(false);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.getContentPane().setLayout(null);
		Frame.setLocationRelativeTo(null);
		
		final DrawingBoard myBoard = new DrawingBoard();
		Frame.getContentPane().add(myBoard);
		
		curText.setBounds(21, 11, 173, 22);
		Frame.getContentPane().add(curText);
		
		JButton btnNewButton = new JButton("Clear");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myBoard.clear();
				myBoard.repaint();
				curText.setText("Current Action : Select");
				setCurrentAction( selectAction );
			}
		});
		btnNewButton.setBounds(21, 45, 153, 30);
		Frame.getContentPane().add(btnNewButton);
		
		JButton btnAddNode = new JButton("Select");
		btnAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curText.setText("Current Action : Select");
				setCurrentAction( selectAction );
			}
		});
		btnAddNode.setBounds(21, 95, 153, 30);
		Frame.getContentPane().add(btnAddNode);
		
		JButton btnAddNode_1 = new JButton("Add Node");
		btnAddNode_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curText.setText("Current Action : Add Node");
				setCurrentAction( addNodeAction );
			}
		});
		btnAddNode_1.setBounds(21, 146, 153, 30);
		Frame.getContentPane().add(btnAddNode_1);
		
		JButton btnAddEdge = new JButton("Add Edge");
		btnAddEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curText.setText("Current Action : Add Edge");
				setCurrentAction( addEdgeAction );
			}
		});
		btnAddEdge.setBounds(21, 196, 153, 30);
		Frame.getContentPane().add(btnAddEdge);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentAction(selectAction);
				curText.setText("Current Action : Select");
				myBoard.removeCur();
				myBoard.repaint();
			}
		});
		btnRemove.setBounds(21, 247, 153, 30);
		Frame.getContentPane().add(btnRemove);
		
		JButton btnSetInputNode = new JButton("Set Input Node");
		btnSetInputNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myBoard.setInputNode();
				myBoard.repaint();
			}
		});
		btnSetInputNode.setBounds(21, 298, 153, 30);
		Frame.getContentPane().add(btnSetInputNode);
		
		JButton btnSetOutputNode = new JButton("Set Output Node");
		btnSetOutputNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myBoard.setOutputNode();
				myBoard.repaint();
			}
		});
		btnSetOutputNode.setBounds(21, 347, 153, 30);
		Frame.getContentPane().add(btnSetOutputNode);
		
		JButton solve = new JButton("Solve");
		solve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(myBoard.inputSelected && myBoard.outputSelected){
					cont.solve(myBoard.getEdges(),myBoard.getNodes(), myBoard.getInputNode(), myBoard.getOutputNode(),myBoard.getNumOfNodes());
				}
			}
		});
		solve.setBounds(21, 396, 153, 30);
		Frame.getContentPane().add(solve);

		Frame.setEnabled(true);
		Frame.setVisible(true);
	}
	
	public void showResult(ArrayList<String> result)
	{
		
		JFrame res = new JFrame();
		res.setSize(500,600);
		res.setResizable(false );
		res.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		res.getContentPane().setLayout(null);
		res.setLocationRelativeTo(null);
		JTextArea results = new JTextArea();
		results.setBounds(0, 0, 500, 600);
		results.setFont(new Font("Serif",  Font.BOLD, 16));
		results.setEditable(false);
		
		JScrollPane scrol1 = new JScrollPane(results);
		scrol1.setBounds(0, 0, 500, 600);
		scrol1.setAutoscrolls(true);
		scrol1.setVisible(true);
		scrol1.setWheelScrollingEnabled(true);
		
		res.getContentPane().add(scrol1);
		
		for (int i = 0; i < result.size(); i++) {
			results.append(result.get(i));
		}
		
		
		res.setVisible(true);
	}
	
	private void setCurrentAction( int a ){
		action = a ;
		if( action == selectAction ){
			curText.setText("Current Action : Select");
		}
		else if( action == addEdgeAction ){
			curText.setText("Current Action : Add Edge");
		}
		else if( action == addNodeAction ){
			curText.setText("Current Action : Add Node");
		}
	}
	
	private int getCurrentAction(){
		return action ;
	}
	
	private class DrawingBoard extends JComponent{
		
		ArrayList<VNode> nodes ;
		public ArrayList<VNode> getNodes() {
			return nodes;
		}
		ArrayList<VEdge> edges ;
		ArrayList<VNode> selectedNodes ;
		boolean groupFlag = false;
		Shape groupFrame ;
		
		int x , y , width , height ;
		Point last ;
		Point start , end ;
		
		boolean firstAdded = false ;
		
		VNode curNode ;boolean curNodeFlag = false ;
		VEdge curEdge ;boolean curEdgeFlag = false ;
		
		VNode inputNode ;boolean inputSelected = false ;
		VNode outputNode ;boolean outputSelected = false ;
		VNode ss , ee ;
		
		DrawingBoard(){
			nodes = new ArrayList<VNode>();
			edges = new ArrayList<VEdge>();
			selectedNodes = new ArrayList<VNode>();
			x = 200 ;y = 8 ;
			width = 883 ;height = 556 ;
	        setLocation(x, y);
	        setSize(width, height);
	        
	        
	        addMouseListener(new MouseAdapter(){
	        	
	        	public void mousePressed( MouseEvent e){
	        		
	    			selectedNodes = new ArrayList<VNode>();
	        		
	        		switch(action){
	        		case selectAction:
	        			last = e.getPoint();
	        			curNodeFlag = false ;
	        			curEdgeFlag = false ;
	        			groupFlag = false ;
	        			firstAdded = false ;
	        			
	        			for( VEdge edge : edges ){
	        				
	        			}
	        			
	        			for( VNode node : nodes ){
	        				if( node.contains(e.getX(), e.getY()) ){
	        					curNodeFlag = true ;
	        					curNode = node ;
	        					repaint();
	        					break ;
	        				}
	        			}
	        			
	        			
	        				//don't set group flag
	        				start = e.getPoint();
	        				end = start ;
	        			
	        			
	        			repaint();
	        			break;
	        		case addNodeAction:
	        			firstAdded = false ;
	        			nodes.add( new VNode(e.getX(),e.getY()) );
	        			repaint();
	        			break ;
	        		case addEdgeAction:
	        			if( !firstAdded ){
	        				ss = fetchNode(e.getPoint());
	        				if( ss != null ){
	        					firstAdded = true ;
	        				}
	        				else{
	        					setCurrentAction(selectAction);
	        				}
	        			}
	        			else{
	        				ee = fetchNode(e.getPoint());
	        				if( ee != null ){
	        					String input = JOptionPane.showInputDialog(Frame, "Enter Weight for the Edge :");
	        					double w ;
	        					try{
	        						
	        						w= Double.parseDouble(input);
	        					}catch(Exception ex)
	        					{
	        						w = 0 ;
	        					}
	        					
	        					if(w!=0)
	        					{	
	        						connect( ss , ee , w);
	        						repaint();
	        					}
	        				}
	        				setCurrentAction( selectAction );
	        				firstAdded = false ;
	        			}
	        			curNodeFlag = false ;
	        			break ;
	        		default :
	        		}
	        	}//end mousePressed
	        	
	        	private void connect(VNode ss, VNode ee , double w) {
					VEdge edge = new VEdge(ss,ee , w );
					edges.add(edge);
					// i don't need to add this edge to the node :D
				}

				private VNode fetchNode( Point p ) {
	        		VNode ret = null ;
					for( VNode node : nodes ){
						if( node.contains(p.x, p.y) )return node ;
					}
					return ret ;
				}

				public void mouseReleased(MouseEvent e){
	        		
	        		if( end!= null && start != null ){
	        			groupFrame = getRectangle(start.x,start.y,end.x,end.y);
	        			int xl = 100000 , xr = -1 , yd = -1 , yu = 100000 ;
	        			end = null ;
	        			start = null ;
	        			for( VNode node : nodes ){
	        				if( groupFrame.getBounds2D().contains( node.getShape().getBounds2D() ) ){	
	        					groupFlag = true ;
	        					selectedNodes.add(node);
	        					xl = Math.min( (int)node.getShape().getBounds2D().getX() , xl ) ;
	        					xr = Math.max( (int)node.getShape().getBounds2D().getX()+ node.getBounded().getBounds().width , xr ) ;
	        					yd = Math.max( (int)node.getShape().getBounds2D().getY()+ node.getBounded().getBounds().height , yd ) ;
	        					yu = Math.min( (int)node.getShape().getBounds2D().getY() , yu ) ;
	        				}
	        			}
	        			if( groupFlag ){
	        				groupFrame = getRectangle(xl,yu,xr,yd);
	        			}

	        			repaint();
	        		}
        			end = null ;
        			start = null ;
	        		repaint();
	        	}//end mouse released
	        	
	        	
	        	
	        	
	        	
	        });//end Mouse Listener
	        
	        
	        addMouseMotionListener( new MouseMotionAdapter(){
	        	public void mouseDragged( MouseEvent e ){
	        		switch(action){
	        		case selectAction:
	        			if( curNodeFlag ){//move node //edges must be handled
	        				int dx = (int) (e.getX() - last.getX());
	        				int dy = (int) (e.getY() - last.getY());
	        				last = e.getPoint();
	        				curNode.move(dx, dy);
	        				for(VEdge edge : edges)edge.refresh();
	        				repaint();
	        			}
	        			else if( curEdgeFlag ){//modify edge
	        				
	        			}
	        			else{//select group
	        				end = e.getPoint();
	        				repaint();
	        			}
	        			break;
	        		default:
	        			
	        		}
	        	}//end mouse dragged
	        	
	        });//end mouse motion listener
	        
		}
		
		public void setOutputNode() {
			if( curNodeFlag ){
				outputNode = curNode ;//special case if we remove this Node ;
				outputSelected = true ;
				if( inputSelected && outputNode == inputNode ){
					inputSelected = false ;
				}
				curNode.setFillColor(Color.red);
				refreshColors();
			}
		}

		public void setInputNode() {
			if( curNodeFlag ){
				inputNode = curNode ;//special case if we remove this Node ;
				inputSelected = true ;
				if( outputSelected && outputNode == inputNode ){
					outputSelected = false ;
				}
				curNode.setFillColor(Color.green);
				refreshColors();
			}
		}

		private void refreshColors() {
			for( VNode node : nodes ){
				if( inputSelected && node == inputNode ){
					continue ;
				}
				if( outputSelected && node == outputNode ){
					continue ;
				}
				node.setFillColor(Color.blue);
			}
		}

		public void removeCur() {
			if( curNodeFlag ){
				curNodeFlag = false ;
    			for( VNode node : nodes ){
    				if( node == curNode ){
    					nodes.remove(node);
    					if( node == inputNode ){
    						inputSelected = false ;
    					}
    					if( node == outputNode ){
    						outputSelected = false ;
    					}
    					for( int i=0 ; i<edges.size() ; ++i ){
    						VEdge edge = edges.get(i);
    						if( curNode == edge.getTo() || curNode == edge.getFrom() ){
    							edges.remove(i);
    							--i ;
    						}
    					}
    					break ;
    				}
    			}
			}
			else if( curEdgeFlag ){
				
			}
			else if( groupFlag ){
				removeGroup();
			}
			
		}

		public void paintComponent(Graphics gg){
			
	        super.paintComponent(gg);
	        Graphics2D g = (Graphics2D)gg ;
	        g.setColor(Color.white);
	        g.fillRect(3, 2, width-5, height-5);
	        g.setStroke( new BasicStroke(2) );
	        g.setColor(Color.black);
	        g.drawRect(3, 2, width-5, height-5);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke( new BasicStroke(3) );
			
			g.setColor(Color.black);
			for( VEdge edge : edges ){
				g.draw(edge.getShape());
				if( edge.getTo() != edge.getFrom() )
					drawArrow(g,edge.getMidX(),edge.getMidY(),edge.getB4MidX(),edge.getB4MidY());
			}
			
			
			for( VNode node : nodes ){
				g.setPaint( node.getFillColor() );
				g.fill( node.getShape() );
				g.setColor( node.getStrokeColor() );
				g.draw( node.getShape() );
			}
			
			if( curNodeFlag ){
        		g.setStroke( new BasicStroke(1));
        		g.setPaint(Color.gray);
        		g.draw(curNode.getBounded());
        		g.setStroke( new BasicStroke(3));
			}
			
			if( end != null && start != null ){
        		g.setStroke( new BasicStroke(1));
        		g.setPaint(Color.gray);
        		g.draw(getRectangle(start.x,start.y,end.x,end.y));
        		g.setStroke( new BasicStroke(3));
			}
			
			if( groupFlag ){
        		g.setStroke( new BasicStroke(1));
        		g.setPaint(Color.gray);
        		g.draw(groupFrame);
        		g.setStroke( new BasicStroke(3));
			}
		}//end paint 
		
		private Shape getRectangle( int x1 , int y1 , int x2 , int y2 ){
			int xx = Math.min(x1,x2);
			int yy = Math.min(y1,y2);
			int hh = Math.abs(y1-y2);
			int ww = Math.abs(x1-x2);
			
			float[] dash = { 1F, 7F };  
			Stroke dashedStroke = new BasicStroke( 2F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 3F, dash, 0F ); 
			return dashedStroke.createStrokedShape( new Rectangle2D.Float(xx,yy,ww,hh) );
		}
		
		
		public void clear(){
			curNodeFlag = false ;
			inputSelected = false ;
			outputSelected = false ;
			groupFlag = false ;
			selectedNodes = new ArrayList<VNode>();
			edges = new ArrayList<VEdge>();
			nodes = new ArrayList<VNode>();
		}
		
		public void removeGroup(){
			for(int i=0 ; i<nodes.size() ; ++i){
				if( selectedNodes.contains( nodes.get(i) ) ){
					if( nodes.get(i) == inputNode ){
						inputSelected = false ;
					}
					if( nodes.get(i) == outputNode ){
						outputSelected = false ;
					}
					for(int j =0 ; j<edges.size() ; ++j){
						VEdge edge = edges.get(j);
						if( edge.getTo() == nodes.get(i) || edge.getFrom()== nodes.get(i) ){
							edges.remove(j);
							--j ;
						}
					}
					nodes.remove(i);
					--i ;
				}
			}
			groupFlag = false ;
			selectedNodes = new ArrayList<VNode>();
			repaint();
		}
		
		
		private void drawArrow( Graphics2D g, int x, int y, int xx, int yy )
		  {
		    float arrowWidth = 10.0f ;
		    float theta = 0.423f ;
		    int[] xPoints = new int[ 3 ] ;
		    int[] yPoints = new int[ 3 ] ;
		    float[] vecLine = new float[ 2 ] ;
		    float[] vecLeft = new float[ 2 ] ;
		    float fLength;
		    float th;
		    float ta;
		    float baseX, baseY ;

		    xPoints[ 0 ] = xx ;
		    yPoints[ 0 ] = yy ;

		    // build the line vector
		    vecLine[ 0 ] = (float)xPoints[ 0 ] - x ;
		    vecLine[ 1 ] = (float)yPoints[ 0 ] - y ;

		    // build the arrow base vector - normal to the line
		    vecLeft[ 0 ] = -vecLine[ 1 ] ;
		    vecLeft[ 1 ] = vecLine[ 0 ] ;

		    // setup length parameters
		    fLength = (float)Math.sqrt( vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1] ) ;
		    th = arrowWidth / ( 2.0f * fLength ) ;
		    ta = arrowWidth / ( 2.0f * ( (float)Math.tan( theta ) / 2.0f ) * fLength ) ;

		    // find the base of the arrow
		    baseX = ( (float)xPoints[ 0 ] - ta * vecLine[0]);
		    baseY = ( (float)yPoints[ 0 ] - ta * vecLine[1]);

		    // build the points on the sides of the arrow
		    xPoints[ 1 ] = (int)( baseX + th * vecLeft[0] );
		    yPoints[ 1 ] = (int)( baseY + th * vecLeft[1] );
		    xPoints[ 2 ] = (int)( baseX - th * vecLeft[0] );
		    yPoints[ 2 ] = (int)( baseY - th * vecLeft[1] );

		    g.fillPolygon( xPoints, yPoints, 3 ) ;
		  }
		
		public ArrayList<VEdge> getEdges() {
			return edges;
		}

		public VNode getInputNode() {
			return inputNode;
		}

		public VNode getOutputNode() {
			return outputNode;
		}
		public int getNumOfNodes()
		{
			return nodes.size();
		}
		
	}//end private class
	
	
	public static void main(String[] args) {
		
		Control c = new Control();
		Start view = new Start(c);
		c.setView(view);
	}
}
