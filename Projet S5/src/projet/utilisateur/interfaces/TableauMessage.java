package projet.utilisateur.interfaces;

import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import projet.utilisateur.Message;

public class TableauMessage extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TreeSet<Message> messages;

	public TableauMessage(TreeSet<Message> messages_set) {
		this.messages = messages_set;
		// System.out.println(messages.toString());
	}

	@Override
	public int getRowCount() {
		return messages.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	public Message getMessageAt(int row) {
		Message m = null;
		Iterator<Message> iter = messages.iterator();
		int i = 0;
		while (i <= row && iter.hasNext()) {
			i++;
			m = iter.next();
		}
		return m;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Message m = null;
		Iterator<Message> iter = messages.iterator();
		int i = 0;
		while (i <= rowIndex && iter.hasNext()) {
			i++;
			m = iter.next();
		}

		switch (columnIndex) {
		case 0:
			return m.getIdentite();
		case 1:
			return m.getDate();
		case 2:
			return m.getMessage();
		default:
			return null;
		}
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Identité";
		case 1:
			return "Date";
		case 2:
			return "Message";
		default:
			return null;
		}
	}

	public Object getValueAt(int row) {
		Message m = null;
		Iterator<Message> iter = messages.iterator();
		int i = 0;
		while (i < row && iter.hasNext()) {
			i++;
			m = iter.next();
		}

		return m;
	}

}
