package test;

public class lancer {
	
	static DropTableServeur d = new DropTableServeur(); 
	static TestServeur t = new TestServeur(); 
	
	public static void main(String[] args) {
		d.run(); 
		t.run(); 
		
		
	}

}
