package projet.utilisateur;

import java.sql.Timestamp;
import java.util.TreeSet;

import projet.serveur.ComparateurMessage;

public class Ticket {

	protected String identifiant;
	protected String titre;
	protected Groupe groupeEmetteur;
	protected Groupe groupeRecepteur;
	protected Timestamp creation;
	protected TreeSet<Message> messages = new TreeSet<>(new ComparateurMessage());

	public Ticket() {

	}

	public Ticket(String identifiant, String titre, Groupe groupeEmetteur, Groupe groupeRecepteur,
			TreeSet<Message> messages, Timestamp date) {
		this.identifiant = identifiant;
		this.titre = titre;
		this.groupeEmetteur = groupeEmetteur;
		this.groupeRecepteur = groupeRecepteur;
		this.messages = messages;
		this.creation = date;
	}

	public Ticket(String titre, Groupe groupeEmetteur, Groupe groupeRecepteur, TreeSet<Message> messages,
			Timestamp date) {
		this.titre = titre;
		this.groupeEmetteur = groupeEmetteur;
		this.groupeRecepteur = groupeRecepteur;
		this.messages = messages;
		this.creation = date;
	}

	public void ajouterMessage(Message m) {
		if (messages == null) {
			messages = new TreeSet<>(new ComparateurMessage());
		}
		messages.add(m);
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public Timestamp getCreation() {
		return creation;
	}

	public void setCreation(Timestamp creation) {
		this.creation = creation;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Groupe getGroupeEmetteur() {
		return groupeEmetteur;
	}

	public void setGroupeEmetteur(Groupe groupeEmetteur) {
		this.groupeEmetteur = groupeEmetteur;
	}

	public Groupe getGroupeRecepteur() {
		return groupeRecepteur;
	}

	public void setGroupeRecepteur(Groupe groupeRecepteur) {
		this.groupeRecepteur = groupeRecepteur;
	}

	public TreeSet<Message> getMessages() {
		return messages;
	}

	public void setMessages(TreeSet<Message> messages) {
		this.messages = messages;
	}

	public String toString() {
		return titre + " : " + groupeEmetteur + " to " + groupeRecepteur;
	}
}
