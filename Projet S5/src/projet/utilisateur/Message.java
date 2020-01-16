package projet.utilisateur;

import java.sql.Timestamp;

public class Message implements Comparable<Message> {

	protected String identifiant;
	protected String identite;
	protected Timestamp date;
	protected String message;

	protected Integer nbVuesManquant;
	protected Integer nbReceptionManquant;

	protected boolean vueParToutLeMonde;
	protected boolean recueParToutLeMonde;

	public Message(String identifiant, String utilisateur, Timestamp date, String message) {
		this.identite = utilisateur;
		this.date = date;
		this.message = message;
		this.identifiant = identifiant;
		this.vueParToutLeMonde = false; 
		this.recueParToutLeMonde = false; 
		// System.out.println("Date message creer : " + this.date);
	}

	public Message(String identifiant, String utilisateur, Timestamp date, String message, Integer nbVuesManquant,
			Integer receptionManquant) {
		this.identite = utilisateur;
		this.date = date;
		this.message = message;
		this.identifiant = identifiant;
		this.nbVuesManquant = nbVuesManquant;
		this.vueParToutLeMonde = (nbVuesManquant == 0);
		this.nbReceptionManquant = receptionManquant;
		this.recueParToutLeMonde = (nbReceptionManquant == 0);
		// System.out.println("Date message creer : " + this.date);
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getIdentite() {
		return identite;
	}

	public void setIdentite(String identite) {
		this.identite = identite;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		String s = "Message : " + message + ", date :" + date + "\n";
		return s;
	}

	@Override
	public int compareTo(Message o) {
		return this.date.compareTo(o.getDate());
	}

	public Integer getNbVues() {
		return nbVuesManquant;
	}

	public void setNbVues(Integer nbVues) {
		this.nbVuesManquant = nbVues;
	}

	public boolean isVueParToutLeMonde() {
		return vueParToutLeMonde;
	}

	public void setVueParToutLeMonde(boolean vueParToutLeMonde) {
		this.vueParToutLeMonde = vueParToutLeMonde;
	}

	public Integer getNbVuesManquant() {
		return nbVuesManquant;
	}

	public void setNbVuesManquant(Integer nbVuesManquant) {
		this.nbVuesManquant = nbVuesManquant;
	}

	public Integer getNbReceptionManquant() {
		return nbReceptionManquant;
	}

	public void setNbReceptionManquant(Integer nbReceptionManquant) {
		this.nbReceptionManquant = nbReceptionManquant;
	}

	public boolean isRecueParToutLeMonde() {
		return recueParToutLeMonde;
	}

	public void setRecueParToutLeMonde(boolean recueParToutLeMonde) {
		this.recueParToutLeMonde = recueParToutLeMonde;
	}
	
	public void modifierRecuParToutLeMonde(int n) {
		this.setRecueParToutLeMonde(n == 0); 
	}
	
	public void modifierVuesParToutLeMonde(int n) {
		this.setVueParToutLeMonde(n == 0); 
	}

}
