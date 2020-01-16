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
import projet.utilisateur.Utilisateur;

public class InterfaceSupprimerUtilisateur extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel utilisateurSupprime = new JLabel("Utilisateur à supprimer : ");

	JComboBox<Utilisateur> utilisateurs;

	JButton boutonSupprimer = new JButton("Supprimer");
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceSupprimerUtilisateur(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceSupprimerUtilisateur();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);
		setTitle("Serveur - Supprimer utilisateur");
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

		boutonSupprimer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Utilisateur u = (Utilisateur) utilisateurs.getSelectedItem();

				boolean result = serveurApplication.supprimerUtilisateur(u.getIdentifiant());

				if (result) {
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);

					setLayout(new FlowLayout(FlowLayout.CENTER));

					JOptionPane.showMessageDialog(root, "Action réussie", "Succès", JOptionPane.INFORMATION_MESSAGE,
							null);

					boutonSupprimer.setEnabled(false);

				} else {
					JOptionPane.showMessageDialog(root, "Erreur de suppression", "Erreur", JOptionPane.ERROR_MESSAGE,
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

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurSupprime));

		hGroup.addGroup(layout.createParallelGroup().addComponent(utilisateurs).addComponent(boutonSupprimer));

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(utilisateurSupprime)
				.addComponent(utilisateurs));

		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonSupprimer).addComponent(menu));

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
