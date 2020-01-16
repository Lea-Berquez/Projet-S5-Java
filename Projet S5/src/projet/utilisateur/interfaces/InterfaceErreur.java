package projet.utilisateur.interfaces;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InterfaceErreur extends JFrame {

	private static final long serialVersionUID = 1L;
	JFrame frame; 

	public InterfaceErreur() {
		super();
		build(); 
	}

	private void build() {
		buildContentPane(); 
	}

	private JPanel buildContentPane() {
		setTitle("Erreur");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.CENTER)); 
		JPanel root = new JPanel();
		root.setBackground(Color.white);
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JLabel erreur = new JLabel("Erreur, veuillez recommencer ou quitter.", JLabel.CENTER);

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(erreur));

		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(erreur));

		layout.setVerticalGroup(vGroup);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		return root;
	}

	public static void main(String[] args) {
//		TestInterface test = new TestInterface();
//		test.setVisible(true);
		InterfaceErreur test = new InterfaceErreur();
		test.setVisible(true);
	}
}
