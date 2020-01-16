package projet.serveur;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.commun.Statut;
import projet.utilisateur.ComprateurGroupe;
import projet.utilisateur.Groupe;
import projet.utilisateur.Message;
import projet.utilisateur.Ticket;
import projet.utilisateur.Utilisateur;

public class ConvertJson {

	ServeurApplication serveurApplication;

	public ConvertJson() {
		serveurApplication = new ServeurApplication();
	}

	public String groupesToJson(Set<Groupe> groupes) {
		String s = new String();
		Iterator<Groupe> iterg = groupes.iterator();
		s = s + "[";
		while (iterg.hasNext()) {
			Groupe g = iterg.next();
			s = s + "\"" + g.getNom() + "\"";
			if (iterg.hasNext()) {
				s = s + ",";
			}
		}
		s = s + "]";
		return s;
	}

	public String messagesToJson(TreeSet<Message> messages) {
		String s = new String();

		Iterator<Message> iterM = messages.iterator();

		s = s + "[";
		while (iterM.hasNext()) {
			Message t = iterM.next();
			s = s + "{" + "\"identifiantMessage\" : \"" + t.getIdentifiant() + "\",";
			s = s + "\"identite\" : \"" + t.getIdentite() + "\",";
			s = s + "\"date\": \'" + t.getDate() + "\',";
			s = s + "\"nbVuesManquant\": " + t.getNbVuesManquant() + ",";
			s = s + "\"nbReceptionManquant\": " + t.getNbReceptionManquant() + ",";
			s = s + "\"vueParToutLeMonde\": " + t.isVueParToutLeMonde() + ",";
			s = s + "\"recueParToutLeMonde\": " + t.isRecueParToutLeMonde() + ",";
			s = s + "\"message\": \"" + t.getMessage() + "\"";

			if (iterM.hasNext()) {
				s = s + "},";
			} else {
				s = s + "}";
			}

		}
		s = s + "]";
		return s;
	}

	public String ticketsToJson(TreeSet<Ticket> tickets) {
		String s = new String();

		Iterator<Ticket> iterT = tickets.iterator();

		s = s + "[";
		while (iterT.hasNext()) {
			Ticket t = iterT.next();
			s = s + "{" + "\"identifiant\" : \"" + t.getIdentifiant() + "\",";
			s = s + "\"titre\": \"" + t.getTitre() + "\",";
			s = s + "\"groupeEmetteur\": \"" + t.getGroupeEmetteur().getNom() + "\",";
			s = s + "\"groupeRecepteur\": \"" + t.getGroupeRecepteur().getNom() + "\",";
			s = s + "\"date\": \'" + t.getCreation() + "\',";
			s = s + "\"messages\":" + messagesToJson(t.getMessages());

			if (iterT.hasNext()) {
				s = s + "},";
			} else {
				s = s + "}";
			}

		}
		s = s + "]";
		return s;
	}

	public String utilisateurToJson(Utilisateur usr) {
		String s = new String();
		// System.out.println(usr.getIdentifiant());

		s = s + "{\"identifiant\": " + "\"" + usr.getIdentifiant() + "\"" + ",\n";
		s = s + "\"identite\": " + "\"" + usr.getIdentite() + "\"" + ",\n";
		s = s + "\"mdp\": " + "\"" + usr.getMdp() + "\"" + ",\n";
		s = s + "\"statut\": " + "\"" + usr.getStatut().toString() + "\"" + ",\n";
		s = s + "\"groupes\": " + groupesToJson(usr.getGroupes()) + ",\n";
		s = s + "\"tickets\": " + ticketsToJson(usr.getTickets()) + "}\n";

		// System.out.println(usr.getIdentifiant());

		return s;
	}

	public Utilisateur jsonToUtilisateurBase(String json) {
		// System.out.println(json);
		JSONObject obj = new JSONObject(json);
		String identifiant = obj.getString("identifiant");
		String mdp = obj.getString("mdp");

		Utilisateur usr = new Utilisateur(identifiant, mdp);

		return usr;
	}

	public Groupe rechercherGroupe(String groupe, List<Groupe> groupes) {
		for (Groupe g : groupes) {
			if (g.getNom().equals(groupe)) {
				return g;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public Utilisateur jsonToUtilisateur(String json) {
		JSONObject obj = new JSONObject(json);
		String identifiant = obj.getString("identifiant");
		String mdp = obj.getString("mdp");
		String identite = obj.getString("identite");
		String s = obj.getString("statut");
		Statut statut = Statut.INCONNU;

		for (Statut t : Statut.values()) {
			if (s.equals(t.toString())) {
				statut = t;
			}
		}

		Set<Groupe> groupes = new TreeSet<Groupe>(new ComprateurGroupe());

		JSONArray groupes_string = obj.getJSONArray("groupes");
		for (Object g : groupes_string) {
			if (g instanceof String) {
				// System.out.println(">>> liste des groupes : " +
				// serveurApplication.getGroupe());
				// System.out.println(" groupe courant : " + g);
				Groupe groupe = serveurApplication.rechercherGroupe((String) g);
				groupes.add(groupe);
			}
		}

		TreeSet<Ticket> tickets = new TreeSet<Ticket>(new ComparateurTicket());

		JSONArray tickets_array = obj.getJSONArray("tickets");
		for (Object t : tickets_array) {
			if (t instanceof JSONObject) {
				// System.out.println(">>>>>>>>> OKAY :" + t.toString());
				String identifiant_ticket = ((JSONObject) t).getString("identifiant");
				// System.out.println(identifiant_ticket);
				String titre_ticket = ((JSONObject) t).getString("titre");
				String groupeEmetteur = ((JSONObject) t).getString("groupeEmetteur");
				String groupeRecepteur = ((JSONObject) t).getString("groupeRecepteur");

				// TODO Requete au serveur pour envoyer la liste des groupes et demander
				Groupe e = rechercherGroupe(groupeEmetteur, serveurApplication.getGroupe());
				Groupe r = rechercherGroupe(groupeRecepteur, serveurApplication.getGroupe());

				// Date date = (Date) ((JSONObject) t).getString("date");

//				Timestamp timestamp = null;
//				SimpleDateFormat formatter = new SimpleDateFormat("E M dd hh:mm:ss G yyyy");
//
//				String dateInString = ((JSONObject) t).getString("date");
//				Date date = null;
//				try {
//					date = formatter.parse(dateInString);
//					String formattedDateString = formatter.format(date);
//
//				} catch (ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.FRANCE);
				// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
				Date parsedDate;
				Timestamp date = null;

				try {
					String date_string = ((JSONObject) t).getString("date");
					String substring = (date_string).substring(11, 13);
					// System.out.println("substring : " + substring);
					parsedDate = dateFormat.parse(((JSONObject) t).getString("date"));
					// System.out.println("Date 1 message : " + parsedDateM.getHours());
					Timestamp timestampM = new java.sql.Timestamp(parsedDate.getTime());
					date = timestampM;
					// System.out.println("Date 2 message : " + date);
					if (substring.equals("12")) {
						date.setHours(12);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

//				if (timestamp != null) {
//					date = new java.util.Date(timestamp.getTime());
//				}

				TreeSet<Message> messages = new TreeSet<Message>(new ComparateurMessage());

				JSONArray messages_array = ((JSONObject) t).getJSONArray("messages");
				for (Object m : messages_array) {
					if (m instanceof JSONObject) {
						String identifiant_message = ((JSONObject) m).getString("identifiantMessage");
						String identite_message = ((JSONObject) m).getString("identite");
						// date = ((JSONObject) t).getString("date");
//						Timestamp timestampM = (Timestamp) ((JSONObject) t).get("date");
//						Date dateM = null;
//						if (timestampM != null) {
//							dateM = new java.util.Date(timestampM.getTime());
//						}

						SimpleDateFormat dateFormatM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.FRANCE);
						// DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
						Date parsedDateM;
						Timestamp dateM = null;

						try {
							String date_string = ((JSONObject) m).getString("date");
							String substring = (date_string).substring(11, 13);
							// System.out.println("substring : " + substring);
							parsedDateM = dateFormatM.parse(((JSONObject) m).getString("date"));
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

						Integer nbVues = ((JSONObject) m).getInt("nbVuesManquant");
						Integer nbRecue = ((JSONObject) m).getInt("nbReceptionManquant");

						String message = ((JSONObject) m).getString("message");

						Message message_final = new Message(identifiant_message, identite_message, dateM, message,
								nbVues, nbRecue);

						messages.add(message_final);

					}

				}

				Ticket ticket = new Ticket(identifiant_ticket, titre_ticket, e, r, messages, date);

				tickets.add(ticket);
			}
		}

		Utilisateur usr = new Utilisateur(identite, identifiant, mdp, statut, groupes, tickets);

		return usr;
	}

	public String jsonToAction(String input) {
		JSONObject obj = new JSONObject(input);
		String action = obj.getString("action");
		return action;
	}

	public String jsonToDonnees(String input) {
		JSONObject obj = new JSONObject(input);
		String donnees = obj.getJSONObject("donnees").toString();
		return donnees;
	}

	public Ticket jsonToTicket(String input) {
		// System.out.println("input : " + input);
		JSONObject obj = new JSONObject(input);
		// String identifiant = obj.getString("identifiantTicket");
		String titre = obj.getString("titre");
		String groupeEmetteur = obj.getString("groupeEmetteur");
		String groupeRecepteur = obj.getString("groupeRecepteur");

		Ticket t = serveurApplication.creerTicket(titre, groupeEmetteur, groupeRecepteur);
		return t;
	}

	public Ticket jsonToTicketFinal(String input) {
		JSONObject obj = new JSONObject(input);
		String titre = obj.getString("titre");
		String groupeEmetteur = obj.getString("groupeEmetteur");
		Groupe groupeE = rechercherGroupe(groupeEmetteur, serveurApplication.getGroupe());
		String groupeRecepteur = obj.getString("groupeRecepteur");
		Groupe groupeR = rechercherGroupe(groupeRecepteur, serveurApplication.getGroupe());
		// Date date = (Date) obj.get("date");
		// System.out.println("Date ticketFinal : " + date);

//		Timestamp timestamp = (Timestamp) obj.get("date");
//		Date date = null;
//		if (timestamp != null) {
//			date = new java.util.Date(timestamp.getTime());
//		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
		Date parsedDate;
		Timestamp date = null;

		try {
			parsedDate = dateFormat.parse(obj.getString("date"));
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			date = timestamp;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String identifiant = obj.getString("identifiant");

		TreeSet<Message> messages = new TreeSet<Message>(new ComparateurMessage());

		JSONArray messages_array = obj.getJSONArray("messages");
		for (Object m : messages_array) {
			if (m instanceof JSONObject) {
				String identite_message = ((JSONObject) m).getString("identite");
				// date = ((JSONObject) t).getString("date");

//				Timestamp timestampM = (Timestamp) ((JSONObject) m).get("date");
//				Date dateM = null;
//				if (timestampM != null) {
//					dateM = new java.util.Date(timestampM.getTime());
//				}

				Timestamp dateM = (Timestamp) ((JSONObject) m).get("date");

				String message = ((JSONObject) m).getString("message");
				String identifiant_message = ((JSONObject) m).getString("identifiant");

				Integer nbVues = ((JSONObject) m).getInt("nbVuesManquant");
				Integer nbRecue = ((JSONObject) m).getInt("nbReceptionManquant");

				Message message_final = new Message(identifiant_message, identite_message, dateM, message, nbVues,
						nbRecue);

				messages.add(message_final);

			}

		}

		Ticket t = new Ticket(identifiant, titre, groupeE, groupeR, messages, date);
		return t;
	}

	public String ticketToJson(Ticket ticket) {
		String s = new String();
		s = s + "{" + "\"identifiant\" : \"" + ticket.getIdentifiant() + "\",";
		s = s + "\"titre\": \"" + ticket.getTitre() + "\",";
		s = s + "\"groupeEmetteur\": \"" + ticket.getGroupeEmetteur().getNom() + "\",";
		s = s + "\"groupeRecepteur\": \"" + ticket.getGroupeRecepteur().getNom() + "\",";
		s = s + "\"date\":" + JSONObject.valueToString(ticket.getCreation()) + ",";
		if (ticket.getMessages() != null) {
			s = s + "\"messages\":" + messagesToJson(ticket.getMessages());
		} else {
			s = s + "\"messages\": []";
		}

		s = s + "}";
		return s;
	}

	public Message jsonToMessage(String input) {
		JSONObject obj = new JSONObject(input);
		String identite = obj.getString("identite");

//		Timestamp timestamp = (Timestamp) obj.get("date");
//		Date date = null;
//		if (timestamp != null) {
//			date = new java.util.Date(timestamp.getTime());
//		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
		Date parsedDate;
		Timestamp date = null;

		try {
			parsedDate = dateFormat.parse(obj.getString("date"));
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			date = timestamp;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String message = obj.getString("message");
		String identifiant_message = obj.getString("identifiantMessage");

		Integer nbVues = obj.getInt("nbVuesManquant");
		Integer nbRecue = obj.getInt("nbReceptionManquant");

		Message m = new Message(identifiant_message, identite, date, message, nbVues, nbRecue);
		return m;
	}

	public Message jsonToMessageBase(String input) {
		JSONObject obj = new JSONObject(input);
		String identifiant_ticket = obj.getString("identifiantTicket");
		String identite = obj.getString("identite");

		String message = obj.getString("message");

		Message m = serveurApplication.creerMessage(identifiant_ticket, identite, message);

		return m;
	}

	public String messageToJson(Message m) {
		String s = new String();

		s = s + "{" + "\"identifiantMessage\" : \"" + m.getIdentifiant() + "\",";
		s = s + "\"identite\" : \"" + m.getIdentite() + "\",";
		s = s + "\"date\":" + JSONObject.valueToString(m.getDate()) + ",";
		s = s + "\"nbVuesManquant\": " + m.getNbVuesManquant() + ",";
		s = s + "\"nbReceptionManquant\": " + m.getNbReceptionManquant() + ",";
		s = s + "\"vueParToutLeMonde\": " + m.isVueParToutLeMonde() + ",";
		s = s + "\"recueParToutLeMonde\": " + m.isRecueParToutLeMonde() + ",";
		s = s + "\"message\": \"" + m.getMessage() + "\"";

		s = s + "}";

		return s;

	}

	public String groupesListeToJson(List<Groupe> groupes) {
		String s = new String();
		Iterator<Groupe> iterg = groupes.iterator();
		s = s + "{ groupes : [";
		while (iterg.hasNext()) {
			Groupe g = iterg.next();
			s = s + "\"" + g.getNom() + "\"";
			if (iterg.hasNext()) {
				s = s + ",";
			}
		}
		s = s + "] }";
		return s;
	}

	public Set<Groupe> jsonToGroupes(String input) {
		Set<Groupe> groupe = new TreeSet<Groupe>(new ComprateurGroupe());
		JSONObject obj = new JSONObject(input);
		JSONArray list = obj.getJSONArray("groupes");

		for (Object l : list) {
			if (l instanceof String) {
				Groupe g = new Groupe((String) l);
				groupe.add(g);
			}
		}
		return groupe;
	}

	public List<String> jsonToDonneesLecture(String input) {
		JSONObject obj = new JSONObject(input); 
		
		List<String> list = new ArrayList<String>(); 
		
		String identifiantTicket = obj.getString("identifiantTicket"); 
		String identifiantLecteur = obj.getString("identifiantLecteur"); 
		
		list.add(identifiantTicket); 
		list.add(identifiantLecteur); 
		
		return list;
	}

}
