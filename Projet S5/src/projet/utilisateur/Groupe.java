package projet.utilisateur;

import java.util.ArrayList;
import java.util.List;

public class Groupe {

	protected String nom;
	protected List<Utilisateur> utilisateurs = new ArrayList<>();

	public Groupe(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}
	
	public void supprimerUtilisateur(Utilisateur u) {
		utilisateurs.remove(u); 
	}

	public void ajouterUtilisateur(Utilisateur usr) {
		utilisateurs.add(usr);
	}
	
	public String toString() {
		return nom; 
	}
}
