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

public class InterfaceSupprimerUtilisateurGroupe extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel utilisateurChoisi = new JLabel("Utilisateur : ");

	JComboBox<Utilisateur> utilisateurs;
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceSupprimerUtilisateurGroupe(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceSupprimerUtilisateurGroupe();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);
		setTitle("Serveur - Supprimer un utilisateur dans un groupe");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JComboBox<Utilisateur> utilisateurs = new JComboBox<Utilisateur>();

		for (Utilisateur u : serveurApplication.getUtilisateurs()) {
			utilisateurs.addItem(u);
		}

		menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new InterfaceServeur();

			}
		});

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurChoisi));

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurs));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(menu)
				.addComponent(utilisateurChoisi).addComponent(utilisateurs));

		utilisateurs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Groupe> groupes = new JComboBox<Groupe>();
				JLabel label = new JLabel("Groupe : ");

				Utilisateur u = (Utilisateur) utilisateurs.getSelectedItem();

//				for (Utilisateur user : serveurApplication.getUtilisateurs()) {
//					System.out.println("Utilisateur " + user.getIdentite() + " : " + user.getGroupes());
//				}

				for (Groupe g : u.getGroupes()) {
					groupes.addItem(g);
				}

				JButton boutonSupprimer = new JButton("Supprimer");
				boutonSupprimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Groupe g = (Groupe) groupes.getSelectedItem();

						boolean result = serveurApplication.supprimerUtilisateurGroupe(u.getIdentifiant(), g.getNom());

						if (result) {
							setDefaultCloseOperation(DISPOSE_ON_CLOSE);

							setLayout(new FlowLayout(FlowLayout.CENTER));

							JOptionPane.showMessageDialog(root, "Action réussie", "Succès",
									JOptionPane.INFORMATION_MESSAGE, null);

							boutonSupprimer.setEnabled(false);

						} else {
							JOptionPane.showMessageDialog(root, "Erreur de suppression", "Erreur",
									JOptionPane.ERROR_MESSAGE, null);
						}

						pack();
						setLocationRelativeTo(null);
						setVisible(true);

					}
				});

				setDefaultCloseOperation(DISPOSE_ON_CLOSE);

				setLayout(new FlowLayout(FlowLayout.CENTER));

				hGroup.addGroup(layout.createParallelGroup().addComponent(label));

				hGroup.addGroup(layout.createParallelGroup().addComponent(groupes).addComponent(boutonSupprimer));

				layout.setHorizontalGroup(hGroup);
				GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
				vGroup.addGroup(
						layout.createParallelGroup(Alignment.BASELINE).addComponent(label).addComponent(groupes));
				vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonSupprimer));

				layout.setVerticalGroup(vGroup);

				pack();
				setLocationRelativeTo(null);
				setVisible(true);

			}
		});

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
