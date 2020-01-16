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

public class InterfaceSupprimerGroupe extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel groupeChoisi = new JLabel("Groupe : ");

	JComboBox<Groupe> groupes;

	JButton boutonSupprimer = new JButton("Supprimer");
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceSupprimerGroupe(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceSupprimerGroupe();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);
		setTitle("Serveur - Supprimer groupe");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JComboBox<Groupe> groupes = new JComboBox<Groupe>();

		for (Groupe g : serveurApplication.getGroupes()) {
			groupes.addItem(g);
		}

		boutonSupprimer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Groupe g = (Groupe) groupes.getSelectedItem();

				boolean result = serveurApplication.supprimerGroupe(g.getNom());

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

		hGroup.addGroup(layout.createParallelGroup().addComponent(groupeChoisi));

		hGroup.addGroup(layout.createParallelGroup().addComponent(groupes).addComponent(boutonSupprimer));

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(groupeChoisi).addComponent(groupes));

		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonSupprimer).addComponent(menu));

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
