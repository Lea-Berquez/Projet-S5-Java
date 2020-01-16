package projet.utilisateur;

import java.util.Comparator;

public class ComprateurGroupe implements Comparator<Groupe> {

	@Override
	public int compare(Groupe o1, Groupe o2) {
		return o1.getNom().compareTo(o2.getNom());
	}

}
