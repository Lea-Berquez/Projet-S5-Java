package projet.serveur.interfaces;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import projet.serveur.ServeurApplication;
import projet.utilisateur.Groupe;

public class InterfaceAjouterGroupe extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServeurApplication serveurApplication;

	JLabel nomGroupe = new JLabel("Nom du groupe");
	JTextField groupe = new JTextField();
	JButton boutonAjouterGroupe = new JButton("Créer");
	JButton menu = new JButton("Menu");

	private GestionnaireFenetre gf;

	public InterfaceAjouterGroupe(ServeurApplication serveurApplication) {
		this.serveurApplication = serveurApplication;
		gf = new GestionnaireFenetre(serveurApplication);
	}

	public static void main(String[] args) {
		// new InterfaceAjouterGroupe();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addWindowListener(gf);

		setTitle("Serveur - Ajouter groupe");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel root = new JPanel();
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		boutonAjouterGroupe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String groupe_string = groupe.getText();

				if (groupe_string != null && !groupe_string.equals("")) {

					Groupe g = serveurApplication.ajouterGroupe(groupe_string);

					setDefaultCloseOperation(DISPOSE_ON_CLOSE);

					setLayout(new FlowLayout(FlowLayout.CENTER));

					if (g != null) {
						JOptionPane.showMessageDialog(root, "Groupe : " + g.getNom() + " créé", "Succés",
								JOptionPane.INFORMATION_MESSAGE, null);

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
				boutonAjouterGroupe.setEnabled(false);

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

		hGroup.addGroup(layout.createParallelGroup().addComponent(nomGroupe));

		hGroup.addGroup(layout.createParallelGroup().addComponent(groupe).addComponent(boutonAjouterGroupe));

		hGroup.addGroup(layout.createParallelGroup().addComponent(menu));

		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(nomGroupe).addComponent(groupe));
		vGroup.addGroup(
				layout.createParallelGroup(Alignment.BASELINE).addComponent(boutonAjouterGroupe).addComponent(menu));

		layout.setVerticalGroup(vGroup);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
