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

import projet.serveur.ServeurApplication;
import projet.utilisateur.Groupe;
import projet.utilisateur.Utilisateur;

public class InterfaceAjouterUtilisateurGroupe extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel groupeChoisi = new JLabel("Groupe : ");
	JLabel utilisateurChoisi = new JLabel("Utilisateur : ");

	JComboBox<Utilisateur> utilisateurs;
	JComboBox<Groupe> groupes;

	JButton boutonUtilisateurGroupe = new JButton("Relier");
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceAjouterUtilisateurGroupe(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceAjouterUtilisateurGroupe();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);
		setTitle("Serveur - Ajouter un utilisateur à un groupe");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JComboBox<Utilisateur> utilisateurs = new JComboBox<Utilisateur>();
		JComboBox<Groupe> groupes = new JComboBox<Groupe>();

		for (Utilisateur u : serveurApplication.getUtilisateurs()) {
			utilisateurs.addItem(u);
		}
		for (Groupe g : serveurApplication.getGroupes()) {
			groupes.addItem(g);
		}

		boutonUtilisateurGroupe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Groupe g = (Groupe) groupes.getSelectedItem();
				Utilisateur u = (Utilisateur) utilisateurs.getSelectedItem();

				boolean result = serveurApplication.ajouterUtilisateurGroupe(u, g);

				if (result) {
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);

					setLayout(new FlowLayout(FlowLayout.CENTER));

					if (u != null) {

						JOptionPane.showMessageDialog(root, "Action réussie", "Suucés", JOptionPane.ERROR_MESSAGE,
								null);
						boutonUtilisateurGroupe.setEnabled(false);


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

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurChoisi).addComponent(groupeChoisi));

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurs).addComponent(groupes)
				.addComponent(boutonUtilisateurGroupe));

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(utilisateurChoisi)
				.addComponent(utilisateurs));
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(groupeChoisi).addComponent(groupes));

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonUtilisateurGroupe)
				.addComponent(menu));

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
