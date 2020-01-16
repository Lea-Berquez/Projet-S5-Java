package test;

import projet.serveur.ServeurApplication;

public class CreateDatabase {

	public CreateDatabase() {
	}

	public static void main(String[] args) {
		
		ServeurApplication serveur = new ServeurApplication(4); 
		
		serveur.createDatabase(); 
		
//		TestServeur t = new TestServeur(); 
//		
//		t.run();
	}

}
