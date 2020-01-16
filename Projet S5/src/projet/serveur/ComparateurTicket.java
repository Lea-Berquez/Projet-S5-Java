package projet.serveur;

import java.util.Comparator;

import projet.utilisateur.Ticket;

public class ComparateurTicket implements Comparator<Ticket> {

	@Override
	public int compare(Ticket o1, Ticket o2) {
		if (o1 != null && o2 != null) {
			if (o1.getIdentifiant() != null && o2.getIdentifiant() != null
					&& o1.getIdentifiant().equals(o2.getIdentifiant())) {
				return 0;
			}
			if (o1.getCreation() != null && o2.getCreation() != null) {
				if(o1.getCreation().compareTo(o2.getCreation()) == 0) {
					return -1; 
				}
				return -o1.getCreation().compareTo(o2.getCreation());
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

}
