package projet.serveur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONException;

import projet.commun.Statut;
import projet.utilisateur.Groupe;
import projet.utilisateur.Message;
import projet.utilisateur.Ticket;
import projet.utilisateur.Utilisateur;

public class ServeurApplication {

	protected Connection con;

	protected Integer nbUtilisateurs;

	protected Integer nbTickets;

	protected Integer nbMessages;

	protected List<Utilisateur> utilisateurs = new ArrayList<>();

	protected List<Groupe> groupes = new ArrayList<>();

	protected TreeSet<Ticket> tickets = new TreeSet<Ticket>(new ComparateurTicket());

	public ServeurApplication() {
		connexionDB();
		utilisateurs = getUtilisateursDB();
		groupes = getGroupe();
		nbUtilisateurs = utilisateurs.size();
		tickets = getTickets();
		nbTickets = tickets.size();
		nbMessages = nombreMessage();

//		System.out.println("Serveur : \n" + this.toString());
//
//		for (Utilisateur user : getUtilisateurs()) {
//			System.out.println("Utilisateur " + user.getIdentite() + " : " + user.getGroupes());
//		}
	}

	public ServeurApplication(int n) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Groupe> getGroupes() {
		return groupes;
	}

	public Utilisateur rechargerUtilisateur(String identifiant) {
		utilisateurs = getUtilisateursDB();
		groupes = getGroupe();
		nbUtilisateurs = utilisateurs.size();
		tickets = getTickets();
		nbTickets = tickets.size();
		nbMessages = nombreMessage();

		boolean trouve = false;
		Utilisateur utilisateur = null;
		int i = 0;

//		System.out.println(utilisateurs);
//		System.out.println("id :" + identifiant + "mdp :" + mdp);

		while (i < utilisateurs.size() && !trouve) {
			if (utilisateurs.get(i).getIdentifiant().equals(identifiant)) {
				utilisateur = utilisateurs.get(i);
				trouve = true;
			}
			i++;
		}

		if (utilisateur == null) {
			return null;
		}

		modificationConnexion(utilisateur.getIdentifiant());

		utilisateur = associerGroupe(utilisateur);

		utilisateur.setTickets(rechercherTickets(utilisateur));

		return utilisateur;

	}

	public void modificationConnexion(String identifiant) {

		System.out.println("Modification connexion " + identifiant);
		if (tickets != null && tickets.size() > 0) {
			for (Ticket t : tickets) {
				if (t.getMessages() != null && t.getMessages().size() > 0) {
					for (Message m : t.getMessages()) {
						try {
							System.out.println("message id : " + m.getIdentifiant());
							Statement stmt = con.createStatement();

							ResultSet messagesAttentesConnexion = stmt.executeQuery(
									"SELECT identifiantMessage, identifiantUtilisateur FROM projet.messagesattente WHERE identifiantMessage = \'"
											+ m.getIdentifiant() + "\' AND " + " identifiantUtilisateur = \'"
											+ identifiant + "\'");

							Integer cp = 0;

							while (messagesAttentesConnexion.next()) {
								System.out.println(
										"Message : " + messagesAttentesConnexion.getString("identifiantMessage"));
								cp++;
							}

							if (cp == 0) {
							} else {
								Statement stmt2 = con.createStatement();
								stmt2.executeUpdate("DELETE FROM projet.messagesattente WHERE identifiantMessage = \'"
										+ m.getIdentifiant() + "\' AND " + " identifiantUtilisateur = \"" + identifiant
										+ "\"");
								Integer n = m.getNbReceptionManquant() - 1;
								m.setNbReceptionManquant(n);

								m.modifierRecuParToutLeMonde(n);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public Utilisateur connexionUtilisateur(String identifiant, String mdp) {
		Utilisateur utilisateur = rechercherUtilisateur(identifiant, mdp);

		if (utilisateur == null) {
			return null;
		}

		modificationConnexion(identifiant);

		utilisateur = associerGroupe(utilisateur);

		utilisateur.setTickets(rechercherTickets(utilisateur));

		return utilisateur;

	}

	public TreeSet<Ticket> rechercherTickets(Utilisateur usr) {
		TreeSet<Ticket> ticketsu = new TreeSet<Ticket>(new ComparateurTicket());

		Set<Groupe> groupeUser = usr.getGroupes();
		for (Ticket t : tickets) {
			// System.out.println(">>> ticket : " + t.toString());
			if (groupeUser.contains(t.getGroupeEmetteur()) || groupeUser.contains(t.getGroupeRecepteur())) {
				ticketsu.add(t);
			}
		}
		return ticketsu;
	}

	public void connexionDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/projet?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC",
					"root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Utilisateur associerGroupe(Utilisateur usr) {
		for (Groupe g : groupes) {
			List<Utilisateur> usrs = g.getUtilisateurs();
			if (usrs.contains(usr)) {
				usr.ajouterGroupe(g);
			}
		}
		return usr;
	}

	public Groupe groupeExist(String g) {
		Groupe gf = null;
		for (Groupe groupe : groupes) {
			if (groupe.getNom().equals(g)) {
				gf = groupe;
			}
		}

		return gf;
	}

	public Utilisateur ajouterUtilisateur(String identite, String mdp, Statut statut) {

		try {
			Statement stmt = con.createStatement();

			int identifiant = nbUtilisateurs + 1000001;

			// System.out.println("identite : " + identite + ", mdp :" + mdp);
			String s = "VALUES (" + "'U" + identifiant + "'," + "'" + mdp + "'," + "'" + identite + "'," + "'"
					+ statut.toString() + "')";
			stmt.executeUpdate("INSERT INTO utilisateurs " + s);

			nbUtilisateurs++;
			String n = "U" + identifiant;
			Utilisateur u = new Utilisateur(identite, n, mdp, statut);

			Groupe g = groupeExist(statut.toString());
			if (g != null) {
				ajouterUtilisateurGroupe(u, g);
				u.ajouterGroupe(g);
			} else {
				g = ajouterGroupe(statut.toString());
				ajouterUtilisateurGroupe(u, g);
				u.ajouterGroupe(g);
			}
			utilisateurs.add(u);

			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public Groupe ajouterGroupe(String nom) {
		try {
			Statement stmt = con.createStatement();

			String s = "VALUES (" + "'" + nom + "')";
			stmt.executeUpdate("INSERT INTO listegroupes " + s);

			Groupe g = new Groupe(nom);
			groupes.add(g);

			return g;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean lectureMessage(List<String> liste) {
		for (Ticket t : tickets) {

			if (t.getIdentifiant().equals(liste.get(0))) {

				for (Message m : t.getMessages()) {
					try {
						Statement stmt = con.createStatement();

						ResultSet messagesAttentesConnexion = stmt.executeQuery(
								"SELECT identifiantMessage, identifiantUtilisateur FROM projet.messagesattentevues WHERE identifiantMessage = \'"
										+ m.getIdentifiant() + "\' AND " + " identifiantUtilisateur = \"" + liste.get(1)
										+ "\"");

						Integer cp = 0;

						while (messagesAttentesConnexion.next()) {
							cp++;
						}

						if (cp == 0) {
						} else {
							Statement stmt2 = con.createStatement();
							stmt2.executeUpdate("DELETE FROM projet.messagesattentevues WHERE identifiantMessage = \'"
									+ m.getIdentifiant() + "\' AND " + " identifiantUtilisateur = \"" + liste.get(1)
									+ "\"");

							Integer n = cp--;

							m.modifierVuesParToutLeMonde(n);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;

	}

	public boolean ajouterUtilisateurGroupe(Utilisateur usr, Groupe groupe) {
		try {

			if (!usr.getGroupes().contains(groupe)) {

				Statement stmt = con.createStatement();

				String s = "VALUES (" + "'" + groupe.getNom() + "'," + "'" + usr.getIdentifiant() + "')";
				stmt.executeUpdate("INSERT INTO projet.groupeutilisateur " + s);

				for (Groupe g : groupes) {
					if (g.getNom().equals(groupe.getNom())) {
						g.ajouterUtilisateur(usr);
						for (Utilisateur u : utilisateurs) {
							if (u.getIdentifiant().equals(usr.getIdentifiant())) {
								u.ajouterGroupe(g);
							}
						}
					}
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Statut retourneStatut(String statut) {
		Statut[] status = Statut.values();
		// System.out.println(statut);
		Statut statutfinal = null;
		for (Statut s : status) {
			if (s.toString().equals(statut)) {
				statutfinal = s;
			}
		}
		return statutfinal;
	}

	public Utilisateur rechercherUtilisateur(String identifiant, String mdp) {
		boolean trouve = false;
		Utilisateur utilisateur = null;
		int i = 0;

//		System.out.println(utilisateurs);
//		System.out.println("id :" + identifiant + "mdp :" + mdp);

		while (i < utilisateurs.size() && !trouve) {
			if (utilisateurs.get(i).getIdentifiant().equals(identifiant) && utilisateurs.get(i).getMdp().equals(mdp)) {
				utilisateur = utilisateurs.get(i);
				trouve = true;
			}
			i++;
		}

		// System.out.println("utilisateur : " + utilisateur.toString());
		return utilisateur;

	}

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public List<Utilisateur> getUtilisateursDB() {
		List<Utilisateur> list = new ArrayList<>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT*FROM projet.utilisateurs");

			while (rst.next()) {
				String identifiant = rst.getString("identifiant");
				String mdp = rst.getString("mdp");
				String identite = rst.getString("identite");
				Statut statut = retourneStatut(rst.getString("statut"));

				Utilisateur usr = new Utilisateur(identite, identifiant, mdp, statut);
				list.add(usr);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<Groupe> getGroupe() {
		List<Groupe> list = new ArrayList<>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT*FROM projet.listegroupes");

			while (rst.next()) {
				String nom = rst.getString("nomGroupe");
				Groupe g = new Groupe(nom);
				list.add(g);
			}

			ResultSet rst2 = stmt.executeQuery("SELECT*FROM projet.groupeutilisateur");

			while (rst2.next()) {
				String identifiantUtilisateur = rst2.getString("identifiantUtilisateur");
				String nomGroupe = rst2.getString("nomGroupe");

				for (Groupe g : list) {
					if (g.getNom().equals(nomGroupe)) {
						for (Utilisateur u : utilisateurs) {
							if (u.getIdentifiant().equals(identifiantUtilisateur)) {
								g.ajouterUtilisateur(u);
								u.ajouterGroupe(g);
							}
						}
					}

				}

			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@SuppressWarnings("deprecation")
	public TreeSet<Ticket> getTickets() {
		TreeSet<Ticket> list = new TreeSet<Ticket>(new ComparateurTicket());

		try {
			Statement stmt = con.createStatement();

			ResultSet baseMessages = stmt.executeQuery("SELECT*FROM projet.messages");

			Map<String, List<Message>> messages = new TreeMap<String, List<Message>>();

			while (baseMessages.next()) {
				String identifiantT = baseMessages.getString("identifiantTicket");
//				Timestamp timestamp = baseMessages.getTimestamp("date");
//				Date date = null;
//				if (timestamp != null) {
//					date = new java.util.Date(timestamp.getTime());
//				}
				SimpleDateFormat dateFormatM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRANCE);
				// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
				Date parsedDateM;
				Timestamp dateM = null;

				try {
					String date_string = baseMessages.getString("date");
					String substring = (date_string).substring(11, 13);
					// System.out.println("substring : " + substring);
					parsedDateM = dateFormatM.parse(date_string);
					// System.out.println("Date 1 message : " + parsedDateM.getHours());
					Timestamp timestampM = new java.sql.Timestamp(parsedDateM.getTime());
					dateM = timestampM;
					// System.out.println("Date 2 message : " + dateM);
					if (substring.equals("12")) {
						dateM.setHours(12);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

//				DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE); 
//				Timestamp dateM = baseMessages.getTimestamp("date");

				String identite = baseMessages.getString("identite");
				String message = baseMessages.getString("message");
				// System.out.println("Timestamp : " + dateM.toString() + " pour message : " +
				// message);

//				System.out.println("Message : " + message + " du ticket " + identifiantT);
				Statement stmt2 = con.createStatement();
				String identifiant_message = baseMessages.getString("identifiantMessage");
				ResultSet messagesAttentesVues = stmt2.executeQuery(
						"SELECT identifiantMessage FROM projet.messagesattentevues WHERE identifiantMessage = \'"
								+ identifiant_message + "\'");

				Integer nbVuesManquant = 0;

				while (messagesAttentesVues.next()) {
					nbVuesManquant++;
				}

				ResultSet messagesAttentes = stmt2.executeQuery(
						"SELECT identifiantMessage FROM projet.messagesattente WHERE identifiantMessage = \'"
								+ identifiant_message + "\'");

				Integer nbRecueManquant = 0;

				while (messagesAttentes.next()) {
					nbRecueManquant++;
				}

				Message m = new Message(identifiant_message, identite, dateM, message, nbVuesManquant, nbRecueManquant);
				List<Message> listm = new ArrayList<Message>();
				listm.add(m);

				if (messages.containsKey(identifiantT)) {
					List<Message> listm2 = messages.get(identifiantT);
					listm2.addAll(listm);
					messages.remove(identifiantT);
					messages.put(identifiantT, listm2);
				} else {
					messages.put(identifiantT, listm);
				}
			}

			// System.out.println(">>>>>> messages " + messages.toString());

			ResultSet rst = stmt.executeQuery("SELECT*FROM projet.tickets");

			if (rst != null) {
				while (rst.next()) {
					String identifiant = rst.getString("identifiantTicket");
					String titre = rst.getString("titre");
					// System.out.println("titre ticket : " + titre);
					Groupe groupeEmetteur = rechercherGroupe(rst.getString("groupeEmetteur"));
					Groupe groupeRecepteur = rechercherGroupe(rst.getString("groupeRecepteur"));
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRANCE);
					// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
					Date parsedDate;
					Timestamp date = null;

					try {
						String date_string = rst.getString("date");
						if (date_string != null) {
							String substring = (date_string).substring(11, 13);
							// System.out.println("substring : " + substring);
							parsedDate = dateFormat.parse(date_string);
							// System.out.println("Date 1 message : " + parsedDateM.getHours());
							Timestamp timestampM = new java.sql.Timestamp(parsedDate.getTime());
							date = timestampM;
							// System.out.println("Date 2 message : " + date);
							if (substring.equals("12")) {
								date.setHours(12);
							}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					List<Message> m = messages.get(identifiant);
					TreeSet<Message> treeMessage = new TreeSet<>(new ComparateurMessage());
					if (m != null) {
						// System.out.println(">> messages de l'utilisateur" + m.toString());

						treeMessage.addAll(m);

					}
					Ticket t = new Ticket(identifiant, titre, groupeEmetteur, groupeRecepteur, treeMessage, date);
					// System.out.println("ticket t :" + t.toString());
					list.add(t);
					// System.out.println("liste 1 :" + list.toString());
				}
			}
			// System.out.println("liste 2 :" + list.toString());

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Groupe rechercherGroupe(String nom) {
		Groupe g = null;
		for (Groupe groupe : groupes) {
			if (groupe.getNom().equals(nom)) {
				g = groupe;
			}
		}
		return g;
	}

	@SuppressWarnings("deprecation")
	public Ticket creerTicket(String titre, String groupeE, String groupeR) {

		try {
			Statement stmt = con.createStatement();

			int identifiantTickets = nbTickets + 1000001;

			String s = "VALUES (" + "'T" + identifiantTickets + "'," + "'" + titre + "'," + "" + "NOW()" + "," + "'"
					+ groupeE + "'," + "'" + groupeR + "')";

			stmt.executeUpdate(
					"INSERT INTO tickets (identifiantTicket, titre, date, groupeEmetteur, groupeRecepteur)" + s);

			Statement stmt2 = con.createStatement();

			ResultSet rst = stmt2
					.executeQuery("SELECT identifiantTicket, date FROM projet.tickets WHERE identifiantTicket = \'T"
							+ identifiantTickets + "\'");
			SimpleDateFormat dateFormatM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRANCE);
			// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
			Date parsedDateM;
			Timestamp dateM = null;

			try {
				rst.next();
				String date_string = rst.getString("date");
				if (date_string != null) {
					String substring = (date_string).substring(11, 13);
					// System.out.println("substring : " + substring);
					parsedDateM = dateFormatM.parse(date_string);
					// System.out.println("Date 1 message : " + parsedDateM.getHours());
					Timestamp timestampM = new java.sql.Timestamp(parsedDateM.getTime());
					dateM = timestampM;
					// System.out.println("Date 2 message : " + dateM);
					if (substring.equals("12")) {
						dateM.setHours(12);
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Groupe groupeEmetteur = rechercherGroupe(groupeE);
			Groupe groupeRecepteur = rechercherGroupe(groupeR);

			Ticket t = new Ticket("T" + identifiantTickets, titre, groupeEmetteur, groupeRecepteur, null, dateM);

			nbTickets++;
			tickets.add(t);
			return t;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public Ticket rechercherTicketListe(String identifiant) {
		Ticket t = new Ticket();
		for (Ticket ticket : tickets) {
			if (identifiant.equals(ticket.getIdentifiant())) {
				t = ticket;
			}
		}
		return t;
	}

	public int nombreMessage() {
		Statement stmt;
		try {
			int cp = 0;
			stmt = con.createStatement();
			ResultSet rst2 = stmt.executeQuery("SELECT * FROM projet.messages");

			while (rst2.next()) {
				cp++;
			}

			return cp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	@SuppressWarnings("deprecation")
	public Message creerMessage(String identifiant_ticket, String emetteur_message, String message) {
		try {
			Statement stmt = con.createStatement();

			int identifiantMessage = nbMessages + 1000001;

			String s = "VALUES (" + "'M" + identifiantMessage + "'," + "'" + identifiant_ticket + "'," + "" + "NOW()"
					+ "," + "'" + emetteur_message + "'," + "'" + message + "')";
			stmt.executeUpdate("INSERT INTO messages " + s);

			Ticket t = rechercherTicketListe(identifiant_ticket);

			Groupe e = t.getGroupeEmetteur();
			Groupe r = t.getGroupeRecepteur();

			Integer nb = 0;

			for (Utilisateur u : e.getUtilisateurs()) {
				String u_string = "VALUES (" + "'M" + identifiantMessage + "'," + "'" + u.getIdentifiant() + "')";
				stmt.executeUpdate("INSERT INTO messagesattente " + u_string);
				nb++;
				String u_string2 = "VALUES (" + "'M" + identifiantMessage + "'," + "'" + u.getIdentifiant() + "')";
				stmt.executeUpdate("INSERT INTO messagesattentevues " + u_string2);
				nb++;
			}

			for (Utilisateur u : r.getUtilisateurs()) {
				String u_string = "VALUES (" + "'M" + identifiantMessage + "'," + "'" + u.getIdentifiant() + "')";
				stmt.executeUpdate("INSERT INTO messagesattente " + u_string);
				nb++;
				String u_string2 = "VALUES (" + "'M" + identifiantMessage + "'," + "'" + u.getIdentifiant() + "')";
				stmt.executeUpdate("INSERT INTO messagesattentevues " + u_string2);
				nb++;
			}

			if (t != null && t.getIdentifiant() != null) {
				// System.out.println("ticket t : " + t.toString());
				ResultSet rst = stmt.executeQuery(
						"SELECT identifiantMessage, date FROM projet.messages WHERE identifiantMessage = \'M"
								+ identifiantMessage + "\'");

				SimpleDateFormat dateFormatM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRANCE);
				// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
				Date parsedDateM;
				Timestamp dateM = null;

				try {
					rst.next();
					String date_string = rst.getString("date");
					String substring = (date_string).substring(11, 13);
					// System.out.println("substring : " + substring);
					parsedDateM = dateFormatM.parse(date_string);
					// System.out.println("Date 1 message : " + parsedDateM.getHours());
					Timestamp timestampM = new java.sql.Timestamp(parsedDateM.getTime());
					dateM = timestampM;
					// System.out.println("Date 2 message : " + dateM);
					if (substring.equals("12")) {
						dateM.setHours(12);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				tickets.remove(t);
				nbMessages++;
				Message m = new Message("M" + identifiantMessage, emetteur_message, dateM, message, nb, nb);
				t.ajouterMessage(m);
				tickets.add(t);
				return m;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String toString() {
		String s = new String();
		s = s + "Liste utilisateurs : \n";
		for (Utilisateur u : utilisateurs) {
			s = s + " - " + u.toString() + "\n";
		}

		s = s + "\n";
		s = s + "Liste groupe : \n";
		for (Groupe g : groupes) {
			s = s + " - " + g.getNom() + " : " + g.getUtilisateurs().toString() + "\n";
		}

		s = s + "\n";
		s = s + "Liste de Tickets : \n";
		for (Ticket t : tickets) {
			s = s + " - " + t.toString() + "\n";
		}

		return s;
	}

	public Utilisateur rechercherUtilisateur(String identifiant) {
		boolean trouve = false;
		Utilisateur utilisateur = null;
		int i = 0;

//		System.out.println(utilisateurs);
//		System.out.println("id :" + identifiant + "mdp :" + mdp);

		while (i < utilisateurs.size() && !trouve) {
			if (utilisateurs.get(i).getIdentifiant().equals(identifiant)) {
				utilisateur = utilisateurs.get(i);
				trouve = true;
			}
			i++;
		}

		// System.out.println("utilisateur : " + utilisateur.toString());
		return utilisateur;

	}

	public boolean supprimerUtilisateur(String identifiant) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("DELETE FROM projet.utilisateurs where identifiant = " + "\'" + identifiant + "\'");
			stmt.execute(
					"DELETE FROM projet.groupeutilisateur where identifiantUtilisateur = " + "\'" + identifiant + "\'");
			Utilisateur u = rechercherUtilisateur(identifiant);
			if (u != null) {
				utilisateurs.remove(u);
				for (Groupe g : groupes) {
					g.supprimerUtilisateur(u);
				}
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean supprimerGroupe(String groupe) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("DELETE FROM projet.listegroupes where nomGroupe = " + "\'" + groupe + "\'");
			stmt.execute("DELETE FROM projet.groupeutilisateur where nomGroupe = " + "\'" + groupe + "\'");
			Groupe g = rechercherGroupe(groupe);
			if (g != null) {
				groupes.remove(g);
				for (Utilisateur u : utilisateurs) {
					u.supprimerGroupe(g);
				}

				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean supprimerUtilisateurGroupe(String identifiant, String groupe) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("DELETE FROM projet.groupeutilisateur where nomGroupe = \'" + groupe
					+ "\' AND identifiantUtilisateur = " + "\'" + identifiant + "\'");
			Groupe g = rechercherGroupe(groupe);
			Utilisateur u = rechercherUtilisateur(identifiant);
			if (g != null && u != null) {
				g.supprimerUtilisateur(u);

				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean modifierMDPUtilisateur(String identifiant, String nouveau_mdp) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("UPDATE projet.utilisateurs SET mdp = \'" + nouveau_mdp + "\' WHERE identifiant = " + "\'"
					+ identifiant + "\'");
			Utilisateur u = rechercherUtilisateur(identifiant);
			utilisateurs.remove(u);
			if (u != null) {
				u.setMdp(nouveau_mdp);
				utilisateurs.add(u);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean dropTables() {
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("TRUNCATE projet.utilisateurs");
			stmt.executeUpdate("TRUNCATE projet.tickets");
			stmt.executeUpdate("TRUNCATE projet.messages");
			stmt.executeUpdate("TRUNCATE projet.listegroupes");
			stmt.executeUpdate("TRUNCATE projet.groupeutilisateur");
			stmt.executeUpdate("TRUNCATE projet.messagesattente");
			stmt.executeUpdate("TRUNCATE projet.messagesattentevues");

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deconnexion() {
		try {
			nbUtilisateurs = 0;
			nbTickets = 0;
			utilisateurs = new ArrayList<>();
			groupes = new ArrayList<>();
			tickets = new TreeSet<Ticket>(new ComparateurTicket());
			Class.forName("com.mysql.jdbc.Driver");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void createDatabase() {
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("CREATE DATABASE projet");

			stmt.executeUpdate(
					"CREATE TABLE `projet`.`utilisateurs` ( `identifiant` CHAR(255) NOT NULL , `mdp` CHAR(255), `identite` TEXT NOT NULL,  `statut` CHAR(255)) ENGINE = MyISAM; ");

			stmt.executeUpdate(
					"CREATE TABLE `projet`.`tickets` ( `identifiantTicket` CHAR(255) NOT NULL , `titre` TEXT NOT NULL, `date` TIMESTAMP NOT NULL,  `groupeEmetteur` CHAR(255) NOT NULL,  `groupeRecepteur` CHAR(255) NOT NULL) ENGINE = MyISAM; ");

			stmt.executeUpdate(
					"CREATE TABLE `projet`.`messages` ( `identifiantMessage` CHAR(255) NOT NULL, `identifiantTicket` CHAR(255) NOT NULL, `date` TIMESTAMP NOT NULL,  `identite` CHAR(255) NOT NULL, `message` TEXT NOT NULL) ENGINE = MyISAM; ");

			stmt.executeUpdate(
					"CREATE TABLE `projet`.`listegroupes` ( `nomGroupe` CHAR(255) NOT NULL) ENGINE = MyISAM; ");

			stmt.executeUpdate(
					"CREATE TABLE `projet`.`groupeutilisateur` ( `nomGroupe` CHAR(255) NOT NULL, `identifiantUtilisateur` CHAR(255) NOT NULL) ENGINE = MyISAM; ");
			
			stmt.executeUpdate(
					"CREATE TABLE `projet`.`messagesattente` ( `identifiantMessage` CHAR(255) NOT NULL, `identifiantUtilisateur` CHAR(255) NOT NULL) ENGINE = MyISAM; ");
			
			stmt.executeUpdate(
					"CREATE TABLE `projet`.`messagesattentevues` ( `identifiantMessage` CHAR(255) NOT NULL, `identifiantUtilisateur` CHAR(255) NOT NULL) ENGINE = MyISAM; ");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropDatabase() {
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DROP DATABASE projet");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
