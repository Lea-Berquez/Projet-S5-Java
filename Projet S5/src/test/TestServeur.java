package test;

import projet.commun.Statut;
import projet.serveur.ServeurApplication;
import projet.utilisateur.Groupe;
import projet.utilisateur.Ticket;
import projet.utilisateur.Utilisateur;

public class TestServeur implements Runnable {

	public void run() {
		ServeurApplication serveur = new ServeurApplication();

		Utilisateur usr1 = serveur.ajouterUtilisateur("LaurentBerquez", "1234", Statut.ENSEIGNANT);
		Utilisateur usr2 = serveur.ajouterUtilisateur("LéaBerquez", "1234567", Statut.ETUDIANT);
		Utilisateur usr3 = serveur.ajouterUtilisateur("RémiDedieu", "12345", Statut.ETUDIANT);
		Utilisateur usr4 = serveur.ajouterUtilisateur("VictorCathala", "1235678", Statut.ETUDIANT);
		Utilisateur usr5 = serveur.ajouterUtilisateur("NadegeLamarque", "456", Statut.ADMINISTRATIF);
		Utilisateur usr6 = serveur.ajouterUtilisateur("FemmeDeMenage", "678", Statut.ENTRETIENT);

		Utilisateur u = serveur.connexionUtilisateur(usr1.getIdentifiant(), usr1.getMdp());

		Groupe g = serveur.ajouterGroupe("Sportif");
		serveur.ajouterUtilisateurGroupe(u, g);

		serveur.creerTicket("Probleme salle 215",
				Statut.ADMINISTRATIF.toString(), Statut.ENSEIGNANT.toString());

		serveur.creerTicket("Menage non fait",
				Statut.ENTRETIENT.toString(), Statut.ETUDIANT.toString());

		serveur.creerMessage("T1000002", "FemmeDeMenage", "cest fait");

		System.out.println("-------------");
		System.out.println(serveur.connexionUtilisateur("U1000002", "1234567").toString());

		System.out.println("-----------Serveur-------------");
		System.out.println(serveur.toString());

		for (Utilisateur user : serveur.getUtilisateurs()) {
			System.out.println("Utilisateur " + user.getIdentite() + " : " + user.getGroupes());
		}
		
		for(Ticket t : serveur.getTickets()) {
			System.out.println("Ticket : " + t.getTitre() + ", date : " + t.getCreation());
		}
		
		serveur.deconnexion(); 
//		serveur.supprimerUtilisateur("U1000001"); 
//		serveur.supprimerGroupe(Statut.ETUDIANT.toString()); 
//		serveur.modifierMDPUtilisateur("U1000005", Statut.ADMINISTRATIF.toString());
//		System.out.println("-----------Serveur après suppression-------------");
//		System.out.println(serveur.toString());
	}
	
//	public static void main(String args[]) {
//		TestServeur s = new TestServeur(); 
//		s.run(); 
//	}
}
