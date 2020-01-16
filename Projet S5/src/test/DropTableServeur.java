package test;

import projet.serveur.ServeurApplication;

public class DropTableServeur implements Runnable {

	public static void main(String[] args) {
		ServeurApplication serveur = new ServeurApplication();

		serveur.connexionDB();
		
		
		serveur.dropTables(); 
	}

	@Override
	public void run() {
		ServeurApplication serveur = new ServeurApplication();
		
		
		serveur.dropTables(); 
		
		serveur.deconnexion();
	}

}
