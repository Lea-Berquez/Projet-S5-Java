package projet.utilisateur.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import projet.utilisateur.Client;
import projet.utilisateur.Groupe;
import projet.utilisateur.Ticket;

public class InterfaceUtilisateurCreationTicket extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<Groupe> groupeEmetteur;
	private JComboBox<Groupe> groupeRecepteur;
	private Client client;

	public InterfaceUtilisateurCreationTicket(Client c) {
		super();
		client = c;
		build();
	}

	private void build() {
		setTitle("Création Ticket Utilisateur");
		setSize(600, 180);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		JPanel root = new JPanel();
		// root.setBackground(Color.white);
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JLabel label1 = new JLabel("Titre Ticket");
		JLabel label2 = new JLabel("Groupe Concerné/Emetteur");
		JLabel label3 = new JLabel("Groupe destinataire");
		JLabel label4 = new JLabel("Message");
		JTextField titreTicket = new JTextField();
		JTextArea message = new JTextArea(50, 20);

//		Object[] listeGroupeEmetteur = new Object[] { "Groupe 1", "Groupe 2", "Groupe 3" };
//		Object[] listeGroupeRecepteur = new Object[] { "Equipe Technique", "Administration" };

		Set<Groupe> listeGroupeEmetteur = client.getUtilisateur().getGroupes();

		groupeEmetteur = new JComboBox<Groupe>();
		for (Groupe g : listeGroupeEmetteur) {
			groupeEmetteur.addItem(g);
		}

		groupeRecepteur = new JComboBox<Groupe>();

		groupeEmetteur.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				groupeRecepteur.removeAllItems();
				Groupe groupeSelect = (Groupe) groupeEmetteur.getSelectedItem();
				client.demandeGroupes();

				Set<Groupe> listeGroupeRecepteur = client.getGroupes();

				for (Groupe g : listeGroupeRecepteur) {
					if (!g.getNom().equals(groupeSelect.getNom())) {
						groupeRecepteur.addItem(g);
					}
				}

			}
		});

		JButton bouton = new JButton("Créer");

		bouton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String titreTicket_string = titreTicket.getText();
				String message_string = message.getText();

				Groupe groupeESelect = (Groupe) groupeEmetteur.getSelectedItem();
				Groupe groupeRSelect = (Groupe) groupeRecepteur.getSelectedItem();

				if (groupeESelect != null && groupeRSelect != null && groupeESelect.getNom() != null
						&& groupeRSelect.getNom() != null) {
					Ticket t = client.creationTicket(client.getUtilisateur().getIdentifiant(), titreTicket_string,
							groupeESelect.getNom(), groupeRSelect.getNom());

					if (t != null && t.getIdentifiant() != null && !message_string.equals("")) {
						boolean reussi = client.creationMessage(client.getUtilisateur().getIdentifiant(),
								t.getIdentifiant(), client.getUtilisateur().getIdentite(), message_string);

						if (reussi) {
							bouton.setEnabled(false);
							dispose();
							System.out.println("Création ticket : ");
							for (Ticket ticket : client.getUtilisateur().getTickets()) {
								System.out.println("Ticket : " + ticket.getTitre());
							}
							client.demandeRafraichissement(client.getUtilisateur().getIdentifiant(),
									client.getUtilisateur().getMdp());
							new InterfaceUtilisateurMain(client, null);
						} else {
							JOptionPane.showMessageDialog(root, "Erreur création ticket", "Erreur",
									JOptionPane.ERROR_MESSAGE, null);

						}
					} else {
						JOptionPane.showMessageDialog(root, "Erreur création ticket", "Erreur",
								JOptionPane.ERROR_MESSAGE, null);
					}
				} else {
					JOptionPane.showMessageDialog(root, "Erreur création ticket", "Erreur", JOptionPane.ERROR_MESSAGE,
							null);
				}
			}
		});

		JButton boutonMenu = new JButton("Menu");

		boutonMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new InterfaceUtilisateurMain(client, null);
			}
		});

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(label2).addComponent(label3)
				.addComponent(label4));
		hGroup.addGroup(layout.createParallelGroup().addComponent(titreTicket).addComponent(groupeEmetteur)
				.addComponent(groupeRecepteur).addComponent(message).addComponent(bouton));

		hGroup.addGroup(layout.createParallelGroup().addComponent(boutonMenu));

		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label1).addComponent(titreTicket));
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(label2).addComponent(groupeEmetteur));
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(label3).addComponent(groupeRecepteur));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label4).addComponent(message));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(bouton).addComponent(boutonMenu));

		layout.setVerticalGroup(vGroup);

		return root;
	}
}
