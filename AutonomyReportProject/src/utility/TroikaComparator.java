package utility;

import java.util.Comparator;

import model.Troika;

public class TroikaComparator implements Comparator<Troika> {

	@Override
	public int compare(Troika first, Troika second) {
		if(first.getCompleteName().compareTo(second.getCompleteName())<0) return -1;
		return 0;
	}

}
