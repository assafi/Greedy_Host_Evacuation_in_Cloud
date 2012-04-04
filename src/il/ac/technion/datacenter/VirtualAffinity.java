/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 04/04/2012
 */
package il.ac.technion.datacenter;

import java.util.List;

public class VirtualAffinity extends Affinity<VM> {

	protected VirtualAffinity(String type, int id) {
		super(type, id);
	}

	public List<VM> getVMs() {
		return getElements();
	}
}
