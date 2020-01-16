package projet.utilisateur.interfaces;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Timer;

import projet.utilisateur.Client;

public class GestionnaireFenetreUtilisateur extends WindowAdapter {
	Client client;
	Timer timer; 

	public GestionnaireFenetreUtilisateur(Client c, Timer t) {
		super();
		client = c;
		this.timer = t; 
	}

	public void windowClosing(WindowEvent e) {
		client.closeConnection();
		timer.stop(); 
		System.exit(0);
	}
}
