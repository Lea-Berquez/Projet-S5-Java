package projet.serveur.interfaces;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import projet.serveur.ServeurApplication;

public class GestionnaireFenetre extends WindowAdapter {

	ServeurApplication serveur;

	public GestionnaireFenetre(ServeurApplication serveur) {
		super();
		this.serveur = serveur;
	}

	public void windowClosing(WindowEvent e) {
		serveur.deconnexion();
		System.exit(0);
	}
}
