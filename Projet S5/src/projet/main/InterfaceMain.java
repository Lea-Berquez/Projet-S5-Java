package projet.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import projet.serveur.Serveur;
import projet.utilisateur.Client;
import projet.utilisateur.interfaces.InterfaceUtilisateurConnexion;

public class InterfaceMain extends JFrame {

	private static final long serialVersionUID = 1L;

	Serveur serveur;
	Client client;
	Thread thread;

	public InterfaceMain() {
		super();
		String host = "127.0.0.1";
		int port = 8952;

		client = new Client(host, port);

		thread = new Thread(client);
		thread.start();

		build();

	}

	private void build() {
		setTitle("NéoC@mpus application");
		setSize(400, 100);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		JPanel root = new JPanel();
		root.setBackground(Color.white);
		this.add(root);

		BorderLayout layout = new BorderLayout();
		root.setLayout(layout);

		JLabel titre = new JLabel("Veuillez choisir votre profil", JLabel.CENTER);
		JButton admin = new JButton("Administrateur");
		JButton utilisateur = new JButton("Utilisateur");

		admin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InterfaceServeurConnexion iterS = new InterfaceServeurConnexion();
				iterS.setVisible(true);
				dispose();

			}
		});

		utilisateur.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InterfaceUtilisateurConnexion iterU = new InterfaceUtilisateurConnexion();
				iterU.setVisible(true);
				dispose();
			}
		});

		root.add("North", titre);
		root.add("Center", admin);
		root.add("South", utilisateur);

		return root;
	}

	public static void main(String[] args) {
		InterfaceMain interfaceMain = new InterfaceMain();
		interfaceMain.setVisible(true);
	}
}
