package model;

public class Edge implements Comparable{
	private int from , to  ;
	double gain ;
	public Edge( int f , int t , double g ){
		setFrom(f);
		setTo(t);
		setGain(g);
	}

	private void setGain(double g) {
		gain = g ;
	}

	private void setTo(int t) {
		to = t ;
	}

	private void setFrom(int f) {
		from = f ;
	}
	
	public int getFrom(){
		return from ;
	}
	
	public int getTo(){
		return to ;
	}
	
	public double getGain(){
		return gain ;
	}
	
	public void reverse(){
		int temp = to ;
		to = from ;
		from = temp ;
	}

	@Override
	public int compareTo(Object arg) {
		Edge e2 = (Edge)arg ;
		if( getGain() == e2.getGain() && getFrom() == e2.getFrom() && getTo() == e2.getTo() ){//equals
			return 0 ;
		}
		return 1 ;
	}
	
	public boolean equals(Object arg){
		Edge e2 = (Edge)arg ;
		return compareTo(e2) == 0 ;
	}
	
	public int hashCode(){
		int h = getTo();
		h = h*33 + getFrom();
		h = (int) (h*33 + getGain());
		return h & 0x7fffffff ;
	}
}
