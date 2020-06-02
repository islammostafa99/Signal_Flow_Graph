package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class Core {
	
	private ArrayList<Edge>[] adjList ;
	private ArrayList<ArrayList<Edge>> cycles ;
	private ArrayList<ArrayList<Edge>> forwardPaths ;
	private Set< ArrayList<Integer> > tester ;
	private ArrayList<ArrayList<int[]>> combinLoops = new ArrayList<ArrayList<int[]>>() ;
	private double[] delta ;
	private boolean[] visited ;
	
	public Core( ArrayList<Edge>[] arg ){
		adjList = arg ;
		visited = new boolean[100];
	}
	
	public ArrayList<ArrayList<Edge>> getForwardPaths(){
		return forwardPaths ;
	}
	public ArrayList<ArrayList<Edge>> getCycles(){
		return cycles ;
	}
	public double[] getDeltas()
	{
		return delta;
	}
	public ArrayList<ArrayList<int[]>> getCombinLoops()
	{
		return combinLoops;
	}
	
	/******************************************* Forward Paths ***************************************************/
	private void generateForwardPaths(int s , int t)
	{
	    visited = new boolean[adjList.length];
	    Deque<Integer> q = new LinkedList<Integer>();
	    forwardPaths = new ArrayList<ArrayList<Edge>>();
    
	    q.add(s);
	    modiDFS(s,t,q);
	    
	    delta = new double[forwardPaths.size()+1];
	}
	
	private void modiDFS(int i ,int t , Deque<Integer> q)
	{
		if(i==t)
		{
			savePath(q);
			return ;
		}
		
		for (int j = 0; j < adjList[i].size(); j++) {
			
			int to = adjList[i].get(j).getTo();
			if(!visited[to])
			{	
				q.add(to);
				visited[to] =true ;
				modiDFS(to,t,q);
				visited[to] =false ;
				q.removeLast();
			}
		}
		
	}
	
	private void savePath(Deque<Integer> q)
	{
		forwardPaths.add(new ArrayList<Edge>());
		Iterator<Integer> i = q.iterator();
		int from , to =i.next();
		while(i.hasNext())
		{
			from = to;
			to = i.next();
			
			for (int j = 0; j < adjList[from].size(); j++) {
				
				if(adjList[from].get(j).getTo() == to)
					forwardPaths.get(forwardPaths.size()-1).add(adjList[from].get(j));
			}	
		}
	}
	
	
	private double pathGain(int i)
	{
		double gain = 1 ;
		for (int j = 0; j < forwardPaths.get(i).size(); j++) {
			gain *= forwardPaths.get(i).get(j).getGain();
		}
		
		return gain ;
	}
	/******************************************** Cycles ********************************************************/
	private void computeCycles(){
		Stack<Integer> s = new Stack<Integer>();
		cycles = new ArrayList<ArrayList<Edge>>();
		tester = new HashSet< ArrayList<Integer> >();
		
		for(int i=0 ; i<adjList.length ; ++i){
			if( visited[i] == false ){
				dfs( i , s );
			}
		}
	}
	
	private void dfs( int node , Stack<Integer> s ){
		visited[node] = true ;
		s.push(node);
		
		for(int i=0 ; i<adjList[node].size() ; ++i){
			int to = adjList[node].get(i).getTo() ;
			if( visited[to] ){
				generateCycle(to,s);
			}
			else{
				dfs( to , s );
			}
		}
		
		s.pop();
		visited[node] = false ;
	}
	
	private void generateCycle(int to, Stack<Integer> s) {
		Stack<Integer> temp = new Stack<Integer>() ;
		Stack<Integer> path = new Stack<Integer>() ;
		ArrayList<Integer> test = new ArrayList<Integer>() ;
		
		path.push(to);
		test.add(to);
		
		while( s.peek() != to ){
			test.add(s.peek());
			path.push(s.peek());
			temp.push(s.pop());
		}
		
		while(!temp.empty()){
			s.push( temp.pop() );
		}
		
		ArrayList<Edge> cycle = new ArrayList<Edge>();
		
		int f = -1 ;
		int t = path.pop(); ;
		int src = t ;
		
		while( !path.empty() ){
			f = t ;
			t = path.pop();
			
			for(int i=0 ; i<adjList[f].size() ; ++i){
				if( adjList[f].get(i).getTo() == t ){
					cycle.add(adjList[f].get(i));
					break ;
				}
			}
		}
		
		for(int i=0 ; i<adjList[t].size() ; ++i){
			if( adjList[t].get(i).getTo() == src ){
				cycle.add(adjList[t].get(i));
				break ;
			}
		}
		
		Collections.sort(test);
		
		if( !tester.contains(test) ){
			tester.add(test);
			cycles.add(cycle);
		}
	}
	
	private boolean isTwoCyclesTouch( ArrayList<Edge> c1 , ArrayList<Edge> c2 ){
		Set<Integer> nodes = new HashSet<Integer>();
		
		for(int i=0 ; i<c1.size() ; ++i){
			nodes.add(c1.get(i).getTo());
		}
		
		for(int i=0 ; i<c2.size() ; ++i){
			if( nodes.contains(c2.get(i).getTo())){
				return true ;
			}
		}
		
		return false ;
	}
	
	/******************************************* Computations ****************************************************/
	
	public double calcTF(int s , int t)
	{
		computeCycles();
		generateForwardPaths(s, t);
		
		double TF = 0 ;
		delta[0] = calcDelta(-1);
		
		for (int i = 1; i < delta.length; i++) {
			
			delta[i] = calcDelta(i-1);
			TF += delta[i]*pathGain(i-1);
		}
		
		return TF/delta[0];
	}
	private double calcDelta(int outPath)
	{	
		double delta = 1  , gain=0 ;
		int cnt = 1 ;
		boolean[] outCycles = new boolean[cycles.size()];
		int[] takenCycles ;
		
		if(outPath!=-1)
			outCycles = outCycles(outPath);
		do
		{
			takenCycles = new int [cnt];
			gain = loopsCombinsGain(0,0,outCycles,takenCycles);
			delta += Math.pow(-1, cnt)*gain;
			cnt++;
			
		}while(gain != 0) ;
		
		return delta ;
	}
	
    private double loopsCombinsGain(int index ,int p , boolean[] outCycles , int[] takenCycles)
    {
    	
    	if(index==takenCycles.length){
    		saveCombination(takenCycles);
    		return getGain(takenCycles);
    	}
    	
    	double sum  = 0 ;
    	for (int i = p ; i < cycles.size(); i++) {
			
    		if(!outCycles[i] && isNonTouching(i,takenCycles,index))
    		{
    			takenCycles[index] = i;
    			sum += loopsCombinsGain(index+1,i+1,outCycles,takenCycles);
    		}
    		 
		}
    	
    	return sum ;
    }
    
    private boolean isNonTouching(int cycle , int[] takenCycles , int index)
    {
    	for (int i = 0; i < index; i++) {
			
    		if(isTwoCyclesTouch(cycles.get(cycle),cycles.get(takenCycles[i])))
    			return false ;
		}
    	
    	return true ;
    	
    }
    
    private double getGain(int[] takenCycles)
    {
    	double gain  = 1 ;
    	
    	for (int i = 0; i < takenCycles.length; i++) {
    		for (int j = 0; j < cycles.get(takenCycles[i]).size(); j++) {
		
				gain *= cycles.get(takenCycles[i]).get(j).getGain();
			}
		}
    	return gain ;
    }
    
    private boolean[] outCycles(int outPath)
    {
    	boolean[] outNode = new boolean[adjList.length];
    	boolean[] outCycle = new boolean[cycles.size()];
    	
    	outNode[0]= true ;
    	for (int i = 0; i < forwardPaths.get(outPath).size(); i++) {	
    		outNode[forwardPaths.get(outPath).get(i).getTo()] = true ;
		}
    	
    	for (int i = 0; i < cycles.size(); i++) {
    		for (int j = 0; j < cycles.get(i).size(); j++) {
				
    			if(outNode[cycles.get(i).get(j).getTo()])
    			{
    				outCycle[i] = true ;
    				break;
    			}	
			}
		}
    	
    	return outCycle;
    }
	private void  saveCombination(int[] combination)
	{
		if(combination.length!=1){
			int[] c = new int[combination.length];
			for (int j = 0; j < c.length; j++) {
				c[j] = combination[j];
			}
			if(combinLoops.size() < combination.length-1)
				combinLoops.add(new ArrayList<int[]>());
			 
				combinLoops.get(combination.length-2).add(c);
	
		}
	}

    
    
   
}
