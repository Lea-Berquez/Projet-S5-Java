package projet.serveur.interfaces;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import projet.commun.Statut;
import projet.serveur.ServeurApplication;
import projet.utilisateur.Utilisateur;

public class InterfaceAjouterUtilisateur extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel identite = new JLabel("NomPrenom - identité : ");
	JLabel mdp = new JLabel("Mot de passe : ");
	JLabel mdp2 = new JLabel("Mot de passe à nouveau : ");
	JLabel statut = new JLabel("Statut : ");
	JTextField identite_choisie = new JTextField();
	JPasswordField mdp_choisie1 = new JPasswordField();
	JPasswordField mdp_choisie2 = new JPasswordField();
	JComboBox<Statut> statut_choisie = new JComboBox<Statut>(Statut.values());
	JButton boutonAjouterUtilisateur = new JButton("Créer utilisateur");
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceAjouterUtilisateur(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceAjouterUtilisateur(serveurApplication);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);
		setTitle("Serveur - Ajouter utilisateur");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		boutonAjouterUtilisateur.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String identite_string = identite_choisie.getText();
				String mdp_string1 = String.valueOf(mdp_choisie1.getPassword());
				String mdp_string2 = String.valueOf(mdp_choisie2.getPassword());
				Statut statut_string = (Statut) statut_choisie.getSelectedItem();

				if (identite_string != null && statut_string != null && mdp_string1.equals(mdp_string2)
						&& !identite_string.equals("") && mdp_string1.equals(mdp_string2) && !mdp_string1.equals("")
						&& !mdp_string2.equals("")) {

					Utilisateur u = serveurApplication.ajouterUtilisateur(identite_string, mdp_string1, statut_string);

					setDefaultCloseOperation(DISPOSE_ON_CLOSE);

					setLayout(new FlowLayout(FlowLayout.CENTER));

					if (u != null) {

						JOptionPane.showMessageDialog(root,
								"Identite : " + u.getIdentite() + ", identifiant : " + u.getIdentifiant()
										+ ", statut : " + u.getStatut(),
								"Succés", JOptionPane.INFORMATION_MESSAGE, null);
						boutonAjouterUtilisateur.setEnabled(false);


					} else {
						JOptionPane.showMessageDialog(root, "Erreur de création", "Erreur", JOptionPane.ERROR_MESSAGE,
								null);
					}
				} else {
					JOptionPane.showMessageDialog(root, "Erreur de création", "Erreur", JOptionPane.ERROR_MESSAGE,
							null);
				}

				pack();
				setLocationRelativeTo(null);
				setVisible(true);

			}

		});

		menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new InterfaceServeur();

			}
		});

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(identite).addComponent(mdp).addComponent(mdp2)
				.addComponent(statut));

		hGroup.addGroup(layout.createParallelGroup().addComponent(identite_choisie).addComponent(mdp_choisie1)
				.addComponent(mdp_choisie2).addComponent(statut_choisie).addComponent(boutonAjouterUtilisateur));

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(identite).addComponent(identite_choisie));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(mdp).addComponent(mdp_choisie1));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(mdp2).addComponent(mdp_choisie2));
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(statut).addComponent(statut_choisie));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonAjouterUtilisateur)
				.addComponent(menu));

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
