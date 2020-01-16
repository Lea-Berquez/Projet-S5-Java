package projet.serveur;

import java.util.Comparator;

import projet.utilisateur.Message;

public class ComparateurMessage implements Comparator<Message> {

	@Override
	public int compare(Message o1, Message o2) {
		if (o1 != null && o2 != null && o1.getDate() != null && o2.getDate() != null) {
			if (o1.getIdentifiant() != null && o2.getIdentifiant() != null
					&& o1.getIdentifiant().equals(o2.getIdentifiant())) {
				return 0;
			}
			if (o1.getDate().compareTo(o2.getDate()) == 0) {
				return 1;
			}
			return o1.getDate().compareTo(o2.getDate());
		} else {
			return 1;
		}
	}

}
