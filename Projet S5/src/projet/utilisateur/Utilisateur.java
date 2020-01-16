package projet.utilisateur;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import projet.commun.Statut;
import projet.serveur.ComparateurTicket;

public class Utilisateur {

	protected String identite;
	protected String identifiant;
	protected String mdp;
	protected Statut statut;
	protected Set<Groupe> groupes = new TreeSet<Groupe>(new ComprateurGroupe());
	protected TreeSet<Ticket> tickets = new TreeSet<Ticket>(new ComparateurTicket());
	protected Ticket ticketSelectionné;
	protected Map<Ticket, Integer> nbMessageNonLus;

	public Utilisateur(String identite, String identifiant, String mdp, Statut statut) {
		super();
		this.identite = identite;
		this.identifiant = identifiant;
		this.mdp = mdp;
		this.statut = statut;
	}

	public Utilisateur(String identite, String identifiant, String mdp, Statut statut, Set<Groupe> groupes,
			TreeSet<Ticket> tickets) {
		super();
		this.identite = identite;
		this.identifiant = identifiant;
		this.mdp = mdp;
		this.statut = statut;
		this.groupes = groupes;
		this.tickets = tickets;
	}

	public void ajouterMessageToTicket(String identifiantTicket, Message m) {
		for (Ticket t : tickets) {
			if (t.getIdentifiant().equals(identifiantTicket)) {
				t.ajouterMessage(m);
			}
		}
	}

	public TreeSet<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(TreeSet<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Utilisateur(String identifiant) {
		this.identifiant = identifiant;
	}

	public Utilisateur(String identifiant, String mdp) {
		this.identifiant = identifiant;
		this.mdp = mdp;
	}

	public String getIdentite() {
		return identite;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public Statut getStatut() {
		return statut;
	}

	public String toString() {
		return identite;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Utilisateur)) {
			return false;
		}
		Utilisateur u = (Utilisateur) o;

		return u.getIdentifiant() == this.getIdentifiant() && u.getMdp() == this.getMdp();
	}

	public void ajouterGroupe(Groupe g) {
		// System.out.println("J'ajoute " + g.getNom() + " a " + this.getIdentite());
		// System.out.println(groupes.add(g));
		// System.out.println("Groupes : " + groupes.toString());
		groupes.add(g);
	}

	public void supprimerGroupe(Groupe g) {
		groupes.remove(g);
	}

	public Set<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(TreeSet<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Ticket getTicketSelectionné() {
		return ticketSelectionné;
	}

	public void setTicketSelectionné(Ticket ticketSelectionné) {
		this.ticketSelectionné = ticketSelectionné;
	}

	public void ajouterTicket(Ticket ticket) {
		if (groupes.contains(ticket.getGroupeEmetteur())) {
			tickets.add(ticket);
		}
	}

}
