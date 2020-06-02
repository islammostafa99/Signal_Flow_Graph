package Control;

import java.util.ArrayList;

import model.*;
import view.*;

public class Control {

	private Start view ;
	private Core core ;
	public void setView(Start v )
	{
		view = v ;
	}
	
	public void solve( ArrayList<VEdge> edges ,ArrayList<VNode> nodes , VNode source , VNode sink , int n )
	{
		int s=0 , t=0  , from = 0 , to=0 ;		
		ArrayList<Edge>[] g = new ArrayList[nodes.size()];
		for (int i = 0; i < g.length; i++) {
			g[i]= new ArrayList<Edge>();
		}
		for (VEdge e : edges) {
			for (int i = 0; i < nodes.size(); i++) {
				if(e.getFrom()==nodes.get(i))
					from = i ;
				else if(e.getTo()==nodes.get(i))
					to = i ;
				
				if(source == nodes.get(i))
					s=i;
				else if (sink== nodes.get(i))
					t = i ;
			}
			g[from].add(new Edge(from,to,e.getGain()));
		}
		
		ArrayList<String> result = getResult(g,s,t);
		view.showResult(result);
		
	}
	
	private ArrayList getResult(ArrayList<Edge>[] g , int s ,int t)
	{
		core = new Core (g);
		ArrayList<String> result = new ArrayList<String>() ;
		double TF = core.calcTF(s,t);
		double[] deltas = core.getDeltas();
		ArrayList<ArrayList<Edge>> cycles = core.getCycles();
		ArrayList<ArrayList<Edge>> forwardPaths = core.getForwardPaths();
		ArrayList<ArrayList<int[]>> allCombin = core.getCombinLoops();
		
		result.add("____________________________Results______________________________\n");
		result.add(" - Forward Paths : \n------------------------\n");
		
		for (int i = 0; i < forwardPaths.size(); i++) {
			String p = " ";
			int j ;
			for ( j = 0; j < forwardPaths.get(i).size(); j++) {
				p= p+forwardPaths.get(i).get(j).getFrom()+" -> ";
			}
			p = p+forwardPaths.get(i).get(j-1).getTo()+"\n";
			result.add(p);
		}
		result.add("________________________________________________________________________");		
		result.add("\n - Individual Loops : \n--------------------------\n");
		
		for (int i = 0; i < cycles.size(); i++) {
			String c ="L"+(i+1)+": ";
			int j ;
			for ( j = 0; j < cycles.get(i).size(); j++) 
				c = c+ cycles.get(i).get(j).getFrom() + " -> " ;
			
			c = c+ cycles.get(i).get(j-1).getTo()+"\n"; 
			result.add(c);
		}
		result.add("________________________________________________________________________");
		result.add("\n - Combination of n non-touching loops :\n---------------------------------------------------\n");
		
		for (int i = 0; i < allCombin.size(); i++) {
			
			String com = "n="+(i+2)+" : ";
			for (int j = 0; j < allCombin.get(i).size(); j++) {
				int[] temp = allCombin.get(i).get(j);
				com +="(";
				for (int j2 = 0; j2 < temp.length; j2++) {
					com += "L"+(temp[j2]+1)+" ";
				}
			
					com += ")  ";
			}
			com+="\n";
			result.add(com);
			
		}
		
		result.add("________________________________________________________________________");
		result.add("\n - Deltas :\n-------------\n");
		
		result.add("Delta = "+deltas[0]+"\n");
		for (int i = 1; i < deltas.length; i++) {
			result.add("Delta "+i+" = "+deltas[i]+"\n");
		}
		result.add("________________________________________________________________________");
		result.add("\n - Overall system transfer function =  "+TF);

		return result ;
	}
	
		
}
