package projet.utilisateur;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import projet.serveur.ConvertJson;

public class Client implements Runnable {
	private static final int PORT = 8952;
	private Socket socket = null;
	private PrintWriter output = null;
	private BufferedInputStream reader = null;

	private static int count = 0;
	private String name = "Client-";

	protected ConvertJson json = new ConvertJson();

	protected Utilisateur utilisateurCourant;
	protected Set<Groupe> groupes = new TreeSet<Groupe>(new ComprateurGroupe());

	public Client() {
		try {
			socket = new Socket(InetAddress.getLocalHost(), PORT);// "192.168.43.95", PORT);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client(String host, int port) {
		try {
			setName(getName() + (++count));
			socket = new Socket(host, port);
//			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		output.println(msg);
	}

	public void closeConnection() {
		output.println("{ \"action\" : \"Demande fermeture connexion\", \"donnees\" : "
				+ "{\"liberationConnexion\" : true }}");
		output.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean demandeConnexion(String identifiant, String mdp) {
		// Demande de connexion
		try {
			// output = new PrintWriter(new BufferedWriter(new
			// OutputStreamWriter(socket.getOutputStream())), true);
			output = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		System.out.println("Client " + identifiant + " demande de connexion et envoie de données\n");
		sendMessage("{ \"action\" : \"Demande connexion\", \"donnees\" : " + "{\"identifiant\" : \"" + identifiant
				+ "\", \"mdp\" : \"" + mdp + "\"}" + "}");
		// output.write("Demande connexion");
		// output.flush();

//		System.out.println("Envoie des données\n");
//		sendMessage("{\"identifiant\" : \"" + identifiant + "\", \"mdp\" : \"" + mdp + "\"}");
//		// output.write("{\"identifiant\" : \"U1000001\", \"mdp\" : \"1234\"}");
//		// output.flush();

		try {
			String utilisateur = read();
			if (utilisateur.equals("echec")) {
				System.out.println("Echec de connexion");
				// closeConnection();
				return false;
			} else {
				System.out.println("Connexion établie : " + utilisateur.toString());
				utilisateurCourant = json.jsonToUtilisateur(utilisateur);
				// System.out.println("mdp : " + utilisateurCourant.getMdp());
//				System.out.println("Utilisateur : \n" + utilisateurCourant.toString());
//				for(Ticket t : utilisateurCourant.getTickets()) {
//					System.out.println("Ticket : " + t.getTitre() + ", date : " + t.getCreation());
//				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean demandeRafraichissement(String identifiant, String mdp) {
		// Demande de connexion
		try {
			// output = new PrintWriter(new BufferedWriter(new
			// OutputStreamWriter(socket.getOutputStream())), true);
			output = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		System.out.println("Client " + identifiant + " demande de rafraichissement\n");
		sendMessage("{ \"action\" : \"Demande rafraichissement\", \"donnees\" : " + "{\"identifiant\" : \""
				+ identifiant + "\", \"mdp\" : \"" + mdp + "\"}" + "}");
		// output.write("Demande connexion");
		// output.flush();

//		System.out.println("Envoie des données\n");
//		sendMessage("{\"identifiant\" : \"" + identifiant + "\", \"mdp\" : \"" + mdp + "\"}");
//		// output.write("{\"identifiant\" : \"U1000001\", \"mdp\" : \"1234\"}");
//		// output.flush();

		try {
			String utilisateur = read();
			if (utilisateur.equals("echec")) {
				System.out.println("Echec de connexion");
				// closeConnection();
				return false;
			} else {
				System.out.println("Rafraichissement : " + utilisateur.toString());
				utilisateurCourant = json.jsonToUtilisateur(utilisateur);
//				System.out.println("Utilisateur : \n" + utilisateurCourant.toString());
//				for(Ticket t : utilisateurCourant.getTickets()) {
//					System.out.println("Ticket : " + t.getTitre() + ", date : " + t.getCreation());
//				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Ticket creationTicket(String identifiant, String titre, String groupeE, String groupeR) {
		System.out.println("Client " + identifiant + " demande création d'un ticket \n");
		sendMessage("{ \"action\" : \"Demande ticket\", \"donnees\" : " + "{\"titre\" : \"" + titre
				+ "\", \"groupeEmetteur\" : \"" + groupeE + "\", \"groupeRecepteur\" : \"" + groupeR + "\"}" + "}");

		try {
			System.out.println("Attente d'une réponse...");
			String ticket = readPetiteQuantite();
			System.out.println("Réponse reçue.");
			if (ticket.equals("echec")) {
				System.out.println("Echec de la création du ticket");
				return null;
			} else {
				System.out.println("Ticket créer : " + ticket.toString());
				Ticket t = json.jsonToTicketFinal(ticket);
				utilisateurCourant.ajouterTicket(t);
				return t;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean creationMessage(String identifiant, String identifiant_ticket, String emetteur, String message) {
		System.out.println(
				"Client " + identifiant + " demande création d'un message pour le ticket" + identifiant_ticket + "\n");
		sendMessage("{ \"action\" : \"Demande message\", \"donnees\" : " + "{\"identifiantTicket\" : \""
				+ identifiant_ticket + "\", \"identite\" : \"" + emetteur + "\", \"message\" : \"" + message + "\"}"
				+ "}");

		try {
			System.out.println("Attente d'une réponse...");
			String messageObtenu = readPetiteQuantite();
			System.out.println("Réponse reçue : " + messageObtenu);
			if (messageObtenu.equals("echec")) {
				System.out.println("Echec de la création du message");
				return false;
			} else {
				System.out.println("Message créer : " + messageObtenu.toString());
				Message m = json.jsonToMessage(messageObtenu);
				utilisateurCourant.ajouterMessageToTicket(identifiant_ticket, m);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean demandeGroupes() {
		System.out.println("Demande récupération des groupes");
		sendMessage(
				"{ \"action\" : \"Demande groupes\", \"donnees\" : " + "{\"demandeGroupe\" : " + "true" + "}" + "}");

		try {
			System.out.println("Attente d'une réponse...");
			String messageObtenu = readPetiteQuantite();
			System.out.println("Réponse reçue : " + messageObtenu);
			if (messageObtenu.equals("echec")) {
				System.out.println("Echec de la création du message");
				return false;
			} else {
				System.out.println("Message créer : " + messageObtenu.toString());
				groupes = json.jsonToGroupes(messageObtenu);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean lectureTicket(String identifiant_ticket, String identifiant_lecteur) {
		System.out.println("Notification lecture");
		sendMessage("{ \"action\" : \"Notification lecture\", \"donnees\" : " + "{\"identifiantTicket\" : \""
				+ identifiant_ticket + "\", \"identifiantLecteur\" : \"" + identifiant_lecteur + "\"}" + "}");

		try {
			System.out.println("Attente d'une réponse...");
			String messageObtenu = readPetiteQuantite();
			System.out.println("Réponse reçue : " + messageObtenu);
			if (messageObtenu.equals("echec")) {
				System.out.println("Echec de la création du message");
				return false;
			} else {
				System.out.println("Lecture réussie : " + messageObtenu.toString());
				//groupes = json.jsonToGroupes(messageObtenu);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Utilisateur getUtilisateur() {
		return utilisateurCourant;
	}

	private String read() throws IOException {
		// System.out.println("Read :");
		String response = "";

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String userInput = "";
		while ((userInput = in.readLine()) != null && !userInput.equals("")) {
			response = response + userInput;
			// System.out.println("reponse : " + response);

		}
		return response;
	}

	private String readPetiteQuantite() throws IOException {
		String response = "";
		// System.out.println("Debut read");
		int stream;
		byte[] b = new byte[255000];

		// reader.readAllBytes()
		stream = reader.read(b);
		// System.out.println("stream : " + stream);
		response = new String(b, 0, stream);
		return response;
	}

	public Set<Groupe> getGroupes() {
		return groupes;
	}

	@SuppressWarnings("static-access")
	public void run() {
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			//output = new PrintWriter(socket.getOutputStream(), true);
//			//reader = new BufferedInputStream(socket.getInputStream());
////			String recu = read(); 
////			System.out.println(recu);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// output.write("{ \"action\" : \"Demande fermeture connexion\", \"donnees\" : "
		// + "{\"liberationConnexion\" : true }");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public static void main(String[] args) {
//		Client c = new Client();
//
//		c.demandeConnexion("U1000001", "1234");
//		System.out.println(">>> Utilisateur récupéré 1 : \n" + c.getUtilisateur().toString());
//
//		c.creationTicket(c.getUtilisateur().getIdentifiant(), "Problème emploi du temps TDA2", "ETUDIANT",
//				"ADMINISTRATIF");
//
//		Client c2 = new Client();
//		c2.demandeConnexion("U1000003", "12345");
//		System.out.println(">>> Utilisateur récupéré 2 : \n" + c2.getUtilisateur().toString());
//
//		System.out.println("\nAprès création d'un nouveau ticket : ");
//		System.out.println(">>> Utilisateur récupéré 1 : \n" + c.getUtilisateur().toString());
//		System.out.println(">>> Utilisateur récupéré 2 : \n" + c2.getUtilisateur().toString());
//
//	}
}
