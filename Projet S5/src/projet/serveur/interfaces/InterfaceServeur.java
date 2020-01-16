package projet.serveur.interfaces;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import projet.serveur.ServeurApplication;

public class InterfaceServeur extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ServeurApplication serveurApplication; 

	public InterfaceServeur() {
		serveurApplication = new ServeurApplication(); 
	
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		setTitle("Serveur");

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JButton boutonAjouterUtilisateur = new JButton("Ajouter Utilisateur");
		boutonAjouterUtilisateur.addActionListener(new InterfaceAjouterUtilisateur(serveurApplication));
		boutonAjouterUtilisateur.addActionListener(this);
		add(boutonAjouterUtilisateur);

		JButton boutonAjouterGroupe = new JButton("Ajouter Groupe");
		boutonAjouterGroupe.addActionListener(new InterfaceAjouterGroupe(serveurApplication));
		boutonAjouterGroupe.addActionListener(this);
		add(boutonAjouterGroupe);

		JButton boutonAjouterUtilisateurGroupe = new JButton("Ajouter Utilisateur à Groupe");
		boutonAjouterUtilisateurGroupe.addActionListener(new InterfaceAjouterUtilisateurGroupe(serveurApplication));
		boutonAjouterUtilisateurGroupe.addActionListener(this);
		add(boutonAjouterUtilisateurGroupe);

		JButton boutonSupprimerUtilisateur = new JButton("Supprimer Utilisateur");
		boutonSupprimerUtilisateur.addActionListener(new InterfaceSupprimerUtilisateur(serveurApplication));
		boutonSupprimerUtilisateur.addActionListener(this);
		add(boutonSupprimerUtilisateur);

		JButton boutonSupprimerGroupe = new JButton("Supprimer Groupe");
		boutonSupprimerGroupe.addActionListener(new InterfaceSupprimerGroupe(serveurApplication));
		boutonSupprimerGroupe.addActionListener(this);
		add(boutonSupprimerGroupe);

		JButton boutonSupprimerUtilisateurGroupe = new JButton("Supprimer Utilisateur à Groupe");
		boutonSupprimerUtilisateurGroupe.addActionListener(new InterfaceSupprimerUtilisateurGroupe(serveurApplication));
		boutonSupprimerUtilisateurGroupe.addActionListener(this);
		add(boutonSupprimerUtilisateurGroupe);

		// serveur = new Serveur();
		
//		hGroup.addGroup(layout.createParallelGroup().addComponent(boutonAjouterUtilisateur)
//				.addComponent(boutonSupprimerUtilisateur));
//
//		hGroup.addGroup(
//				layout.createParallelGroup().addComponent(boutonAjouterGroupe).addComponent(boutonSupprimerGroupe));
//
//		hGroup.addGroup(layout.createParallelGroup().addComponent(boutonAjouterUtilisateurGroupe)
//				.addComponent(boutonSupprimerUtilisateurGroupe));
//
//		layout.setHorizontalGroup(hGroup);
//		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonAjouterUtilisateur)
//				.addComponent(boutonAjouterGroupe).addComponent(boutonAjouterUtilisateurGroupe));
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonSupprimerUtilisateur)
//				.addComponent(boutonSupprimerGroupe).addComponent(boutonSupprimerUtilisateurGroupe));

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public static void main(String[] args) {
		new InterfaceServeur();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();
	}

}
