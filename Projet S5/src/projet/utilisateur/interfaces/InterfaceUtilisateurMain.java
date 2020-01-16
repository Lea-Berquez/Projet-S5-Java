package projet.utilisateur.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;

import projet.utilisateur.Client;
import projet.utilisateur.Groupe;
import projet.utilisateur.Ticket;

public class InterfaceUtilisateurMain extends JFrame {

	private static final long serialVersionUID = 1L;

	Client client;
	JTable tableConversation = new JTable();
	JScrollPane conversation = new JScrollPane(tableConversation);
	DefaultMutableTreeNode conv = new DefaultMutableTreeNode("Groupes");
	JTree conversations = new JTree(conv);
	JScrollPane treePane = new JScrollPane(conversations);
	JTextField message;
	Boolean close = false;

	Timer timer = new Timer(1800, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			client.demandeRafraichissement(client.getUtilisateur().getIdentifiant(), client.getUtilisateur().getMdp());
			// timer.stop();
		}
	});

	Ticket ticketSelectionne;

	private GestionnaireFenetreUtilisateur gf;

	@SuppressWarnings("unused")
	public InterfaceUtilisateurMain(Client c, Ticket t) {
		super();
		client = c;

		gf = new GestionnaireFenetreUtilisateur(client, timer);
		if (t != null) {
			ticketSelectionne = t;
			if (ticketSelectionne.getMessages() != null && ticketSelectionne.getMessages().size() > 0) {
				TableauMessage tMessage = new TableauMessage(t.getMessages());
				tableConversation = new JTable(tMessage);

				TableColumnModel modeleColonne = tableConversation.getColumnModel();

				modeleColonne.getColumn(0).setCellRenderer(new RenduTableau());
				modeleColonne.getColumn(1).setCellRenderer(new RenduTableau());
				modeleColonne.getColumn(2).setCellRenderer(new RenduTableau());

				tableConversation.setColumnModel(modeleColonne);

				int col = 0, larg = 0, largTotal = 0, row = 0, tableX = 0, width = 0;
				JTableHeader header = tableConversation.getTableHeader();
				Enumeration<TableColumn> columns = tableConversation.getColumnModel().getColumns();

				tableConversation.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

				while (columns.hasMoreElements()) {
					TableColumn column = (TableColumn) columns.nextElement();
					col = header.getColumnModel().getColumnIndex(column.getIdentifier());
					width = (int) tableConversation.getTableHeader().getDefaultRenderer()
							.getTableCellRendererComponent(tableConversation, column.getIdentifier(), false, false, -1,
									col)
							.getPreferredSize().getWidth();
					for (row = 0; row < tableConversation.getRowCount(); row++) {
						int preferedWidth = (int) tableConversation.getCellRenderer(row, col)
								.getTableCellRendererComponent(tableConversation,
										tableConversation.getValueAt(row, col), false, false, row, col)
								.getPreferredSize().getWidth();
						width = Math.max(width, preferedWidth);
					}
					header.setResizingColumn(column);
					larg = width + tableConversation.getIntercellSpacing().width;
					larg = larg + 20;
					largTotal += larg;
					column.setWidth(larg);
				}

				tableConversation.setVisible(true);
				tableConversation.setAutoscrolls(true);

				conversation.getViewport().add(tableConversation);

				client.lectureTicket(ticketSelectionne.getIdentifiant(), client.getUtilisateur().getIdentifiant());
				client.demandeRafraichissement(client.getUtilisateur().getIdentifiant(),
						client.getUtilisateur().getMdp());

				build();
			}
		} else {
			ticketSelectionne = new Ticket("Selectionner ticket", null, null, null, null);
			build();
		}
	}

	private void build() {
//		System.out.println("Interface utilisateur ticket : ");
//		for (Ticket ticket : client.getUtilisateur().getTickets()) {
//			System.out.println("Ticket : " + ticket.getTitre());
//		}

		addWindowListener(gf);
		setTitle("Interface Utilisateur - " + client.getUtilisateur().getIdentite());
		setVisible(true);
		setSize(900, 900);
		setLocationRelativeTo(null);
		setResizable(true);
		setContentPane(buildContentPane());
		pack();

	}

	private JPanel buildContentPane() {
		JPanel root = new JPanel();
		root.setBackground(Color.white);
		this.add(root);

		GroupLayout layout = new GroupLayout(root);
		root.setLayout(layout);

		JButton ajouterTicket = new JButton("+Ticket");

		ajouterTicket.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InterfaceUtilisateurCreationTicket iterT = new InterfaceUtilisateurCreationTicket(client);
				iterT.setVisible(true);
				// timer.stop();
				dispose();
			}
		});

		message = new JTextField();

		JButton envoyerMessage = new JButton("Envoyer");

		envoyerMessage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message_string = message.getText();
				boolean reussi = false;
				if (message_string != null && !message_string.equals("") && ticketSelectionne != null) {
					reussi = client.creationMessage(client.getUtilisateur().getIdentifiant(),
							ticketSelectionne.getIdentifiant(), client.getUtilisateur().getIdentite(), message_string);
				}
				if (reussi) {
					message.setText("");
					new InterfaceUtilisateurMain(client, ticketSelectionne);
					// timer.stop();
					dispose();
				} else {
					JOptionPane.showMessageDialog(root, "Erreur création message", "Erreur", JOptionPane.ERROR_MESSAGE,
							null);

				}
			}
		});

		// System.out.println(client.toString());
		Set<Groupe> setGroupes = client.getUtilisateur().getGroupes();
		Set<Ticket> setTickets = client.getUtilisateur().getTickets();

		for (Groupe g : setGroupes) {
			DefaultMutableTreeNode groupe = new DefaultMutableTreeNode(g.getNom());

			for (Ticket t : setTickets) {
				// System.out.println(t.getTitre());
				DefaultMutableTreeNode ticket = new DefaultMutableTreeNode(t);

				if (t.getGroupeEmetteur().getNom().equals(g.getNom())
						|| t.getGroupeRecepteur().getNom().equals(g.getNom())) {
					groupe.add(ticket);
				}
			}

			conv.add(groupe);
		}

		conversations = new JTree(conv);

		conversations.setShowsRootHandles(true);

		treePane = new JScrollPane(conversations);

		JLabel titreTicket = new JLabel(ticketSelectionne.getTitre(), JLabel.CENTER);

		conversations.addTreeSelectionListener(new TreeSelectionListener() {

			@SuppressWarnings("unused")
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) conversations.getLastSelectedPathComponent();

				if (noeud != null) {
					if (noeud.getUserObject() instanceof Ticket) {
						// System.out.println("Selection ticket");
						ticketSelectionne = (Ticket) noeud.getUserObject();
						titreTicket.setText(ticketSelectionne.getTitre());

						client.lectureTicket(ticketSelectionne.getIdentifiant(),
								client.getUtilisateur().getIdentifiant());
						new InterfaceUtilisateurMain(client, ticketSelectionne);
						dispose();
					}
				}
			}
		});

		JButton rafraichir = new JButton("Rafraichir");

		rafraichir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("mdp : " + client.getUtilisateur().getMdp());
				client.demandeRafraichissement(client.getUtilisateur().getIdentifiant(),
						client.getUtilisateur().getMdp());

				for (Ticket t : client.getUtilisateur().getTickets()) {
					if (t.getIdentifiant().equals(ticketSelectionne.getIdentifiant())) {
						ticketSelectionne = t;
					}
				}
				new InterfaceUtilisateurMain(client, ticketSelectionne);
				// timer.stop();
				dispose();
			}
		});

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();

		BorderLayout layout1 = new BorderLayout();
		BorderLayout layout2 = new BorderLayout();
		BorderLayout layout3 = new BorderLayout();
		BorderLayout layout4 = new BorderLayout();

		panel1.setLayout(layout1);
		panel2.setLayout(layout2);
		panel3.setLayout(layout3);
		panel4.setLayout(layout4);

		panel4.add("North", rafraichir);
		panel4.add("Center", ajouterTicket);

		panel1.add("North", panel4);
		panel1.add("Center", treePane);

		// panel2.add("North", conversation);
		panel2.add("Center", message);
		panel2.add("South", envoyerMessage);

		panel3.add("North", titreTicket);
		panel3.add("Center", conversation);
		panel3.add("South", panel2);

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(panel1));
		hGroup.addGroup(layout.createParallelGroup().addComponent(panel3));

		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(panel1).addComponent(panel3));

		layout.setVerticalGroup(vGroup);

//		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
//
//		hGroup.addGroup(layout.createParallelGroup().addComponent(ajouterTicket).addComponent(conversations));
//		hGroup.addGroup(layout.createParallelGroup().addComponent(titreTicket).addComponent(conversation)
//				.addComponent(message).addComponent(envoyerMessage));
//
//		layout.setHorizontalGroup(hGroup);
//
//		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//
//		vGroup.addGroup(
//				layout.createParallelGroup(Alignment.BASELINE).addComponent(ajouterTicket).addComponent(titreTicket));
//		vGroup.addGroup(
//				layout.createParallelGroup(Alignment.BASELINE).addComponent(conversations).addComponent(conversation));
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(message));
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(envoyerMessage));
//
//		layout.setVerticalGroup(vGroup);

		return root;
	}

	public static void main(String[] args) {
//		TestInterface test = new TestInterface();
//		test.setVisible(true);
//		InterfaceUtilisateurMain test = new InterfaceUtilisateurMain();
//		test.setVisible(true);
	}
}
