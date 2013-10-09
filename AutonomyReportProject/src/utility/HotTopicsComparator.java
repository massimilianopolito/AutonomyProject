package utility;

import java.util.Comparator;

import model.HTClusterObject;


public class HotTopicsComparator implements Comparator<HTClusterObject> {

	@Override
	public int compare(HTClusterObject o1, HTClusterObject o2) {
		Long numDoc1 = new Long(o1.getNumberOfDocs());
		Long numDoc2 = new Long(o2.getNumberOfDocs());
		return numDoc1.compareTo(numDoc2);
	}

}
