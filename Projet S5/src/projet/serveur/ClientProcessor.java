package projet.serveur;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import projet.utilisateur.Groupe;
import projet.utilisateur.Message;
import projet.utilisateur.Ticket;
import projet.utilisateur.Utilisateur;

public class ClientProcessor implements Runnable {

	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	protected ServeurApplication serveurApplication;

	protected ConvertJson json;

	public ClientProcessor(Socket pSock) throws IOException {
		sock = pSock;
		serveurApplication = new ServeurApplication();
		json = new ConvertJson();
	}

	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

	public void run() {
		System.out.println("Lancement du traitement de la connexion cliente");
		boolean closeConnexion = false;
		while (!sock.isClosed()) {
			try {

				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());

				// BufferedReader plec = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));

				String input = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();

				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() + ".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				debug += "\t -> Commande reçue : " + input + "\n";
				System.out.println("\n" + debug);

				if (input != null) {
					System.out.println("Message reçu : " + input);

					String action = json.jsonToAction(input);
					String donnees = json.jsonToDonnees(input);

					if (donnees != null) {
						switch (action) {
						case "Demande connexion":
							System.out.println("Demande de connexion aboutie.");
							// System.out.println("Données reçues : " + donnees);
							System.out.println("Données utilisateur reçues: " + donnees);
							Utilisateur usr1 = json.jsonToUtilisateurBase(donnees);

							System.out.println("Recherche données utilisateur : ");
							Utilisateur usr = serveurApplication.connexionUtilisateur(usr1.getIdentifiant(),
									usr1.getMdp());

							if (usr != null) {
								System.out.println("Utilisateur trouvé");
								String utilisateur = json.utilisateurToJson(usr);
								System.out.println(utilisateur);

								writer.write(utilisateur + "\n");
								writer.flush();
							} else {
								System.out.println("!!!! Utilisateur inconnu !!!!");
								System.out.println();

								writer.write("echec" + "\n");
								writer.flush();
							}
							break;
						case "Demande rafraichissement":
							System.out.println("Demande de rafraichissement aboutie.");
							// System.out.println("Données reçues : " + donnees);
							System.out.println("Données utilisateur reçues: " + donnees);
							usr1 = json.jsonToUtilisateurBase(donnees);

							System.out.println("Recherche données utilisateur : ");
							usr = serveurApplication.rechargerUtilisateur((usr1.getIdentifiant()));

							if (usr != null) {
								System.out.println("Utilisateur trouvé");
								String utilisateur = json.utilisateurToJson(usr);

								writer.write(utilisateur + "\n");
								writer.flush();
							} else {
								System.out.println("!!!! Utilisateur inconnu !!!!");
								System.out.println();

								writer.write("echec" + "\n");
								writer.flush();
							}
							break;
						case "Notification lecture":
							System.out.println("Demande de rafraichissement aboutie.");
							// System.out.println("Données reçues : " + donnees);
							System.out.println("Données lecture reçues: " + donnees);
							List<String> elements = json.jsonToDonneesLecture(donnees);

							boolean test = serveurApplication.lectureMessage(elements); 
							if (test) {
								System.out.println("Lecture effectuée");
								
								writer.write(test + "\n");
								writer.flush();
							} else {
								System.out.println("!!!! Pas d'actualisation de lecture !!!!");
								System.out.println();

								writer.write("echec" + "\n");
								writer.flush();
							}
							break;
						case "Demande ticket":
							System.out.println("Demande de création de ticket");
							System.out.println("Données reçues : " + donnees);
							Ticket ticket = json.jsonToTicket(donnees);

							if (ticket != null) {
								System.out.println("Ticket créé");

								String ticket_string = json.ticketToJson(ticket);
								System.out.println("ticket cree : " + ticket_string);

								writer.write(ticket_string);
								writer.flush();
								System.out.println("Ticket envoyé");

							} else {
								System.out.println("!!!! Ticket non créer !!!!");
								System.out.println();

								writer.write("echec" + "\n");
								writer.flush();
							}

							break;
						case "Demande message":
							System.out.println("Demande de création de message");
							System.out.println("Données reçues : " + donnees);
							Message m = json.jsonToMessageBase(donnees);

							if (m != null) {
								System.out.println("Message créé");
								String message = json.messageToJson(m);
								System.out.println("message cree : " + message);
								
								writer.write(message + "\n");
								writer.flush();
								System.out.println("Message envoyé");
							} else {
								System.out.println("!!!! Message non créer !!!!");
								System.out.println();

								writer.write("echec" + "\n");
								writer.flush();
							}

							break;
						case "Demande groupes":
							System.out.println("Demande de récupération de message");
							List<Groupe> groupes = serveurApplication.getGroupes();
							String message = json.groupesListeToJson(groupes);
							System.out.println("Message créé : " + message);
							writer.write(message + "\n");
							writer.flush();
							System.out.println("Message envoyé");
							break;
						case "Demande fermeture connexion":
							closeConnexion = true;
							writer.write("Communication terminée" + "\n");
							break;

						}

						if (closeConnexion) {
							System.out.println("Fermeture connexion");
							writer = null;
							reader = null;
							sock.close();
							// return;

							break;
						}
					} else {
						System.out.println("!!!! Donnees non transmises !!!!");
						System.out.println();

						writer.write("echec" + "\n");
						writer.flush();
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
//		Serveur c = new Serveur();
//		Thread t = new Thread(c);
//		t.start();
	}
}
