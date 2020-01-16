package projet.utilisateur.interfaces;

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
import projet.utilisateur.Client;

public class InterfaceUtilisateurConnexion extends JFrame {

	private static final long serialVersionUID = 1L;

	Serveur serveur;
	Client client;
	Thread thread;

	public InterfaceUtilisateurConnexion() {
		super();
		String host = "127.0.0.1";
		int port = 8952;

		client = new Client(host, port);

		thread = new Thread(client);
		thread.start();

		build();

	}

	private void build() {
		setTitle("Connexion Utilisateur");
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

				boolean reussi = false;
				if (login_string != null && !login_string.equals("") && password_string != null
						&& !password_string.equals("")) {
//					System.out.println("log : " + login_string + ", mdp " + password_string);
//					System.out.println(client);
					reussi = client.demandeConnexion(login_string, password_string);
				}

				if (reussi) {
					//System.out.println(client.getUtilisateur().getGroupes().toString());
					InterfaceUtilisateurMain inter = new InterfaceUtilisateurMain(client, null);
					inter.setVisible(true);
					dispose();
					setVisible(false); 
				} else {
					 JOptionPane.showMessageDialog(root, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE, null);

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
