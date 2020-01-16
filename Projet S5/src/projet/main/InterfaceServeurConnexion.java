package projet.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import projet.serveur.Serveur;
import projet.serveur.interfaces.InterfaceServeur;
import projet.utilisateur.Client;

public class InterfaceServeurConnexion extends JFrame {

	private static final long serialVersionUID = 1L;

	Serveur serveur;
	Client client;
	Thread thread;

	String compteAdministrateur = "admin";
	String mdpAdministrateur = "mdp";

	public InterfaceServeurConnexion() {
		super();
		String host = "127.0.0.1";
		int port = 8952;

		client = new Client(host, port);

		thread = new Thread(client);
		thread.start();

		build();

	}

	private void build() {
		setTitle("Connexion Serveur");
		setSize(400, 100);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		JPanel root = new JPanel();
		root.setBackground(Color.white);
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JLabel label1 = new JLabel("Login : ");
		JLabel label2 = new JLabel("Password : ");
		JTextField login = new JTextField();
		JPasswordField password = new JPasswordField();
		JButton bouton1 = new JButton("Connexion");

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		bouton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String login_string = login.getText();
				String password_string = String.valueOf(password.getPassword());

				if ((login_string.equals(compteAdministrateur)) && (password_string.equals(mdpAdministrateur))) {
					// System.out.println(client.getUtilisateur().getGroupes().toString());
					InterfaceServeur interS = new InterfaceServeur();
					interS.setVisible(true);
					dispose();
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(root, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE,
							null);

				}

			}
		});

		hGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(label2));
		hGroup.addGroup(layout.createParallelGroup().addComponent(login).addComponent(password));
		hGroup.addGroup(layout.createParallelGroup().addComponent(bouton1));

		layout.setHorizontalGroup(hGroup);

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(label1).addComponent(login)
				.addComponent(bouton1));
		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(label2).addComponent(password));

		layout.setVerticalGroup(vGroup);

		return root;
	}
}
