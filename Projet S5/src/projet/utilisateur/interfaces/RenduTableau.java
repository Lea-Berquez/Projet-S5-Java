package projet.utilisateur.interfaces;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import projet.utilisateur.Message;

public class RenduTableau extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Color ROUGE = new Color(128, 0, 0);
	private static final Color VERT = new Color(0, 128, 0);
	private static final Color ORANGE = new Color(255, 165, 0);

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		//Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setText(String.valueOf(value));

		Message m = ((TableauMessage) table.getModel()).getMessageAt(row);

		if (m != null) {
			if (m.isRecueParToutLeMonde()) {
				if (m.isVueParToutLeMonde()) {
					setForeground(VERT);
				} else {
					setForeground(ORANGE);
				}
			} else {
				setForeground(ROUGE);
			}

		}
		return this; 
	}

}
