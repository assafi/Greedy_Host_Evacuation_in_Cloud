/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 04/04/2012
 */
package il.ac.technion.datacenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public abstract class Affinity<T> {
	public final String type;
	public final int id;
	
	private List<T> hosts = new LinkedList<T>();
	
	protected Affinity(String type, int id) {
		this.type = type;
		this.id = id;
	}
	
	protected List<T> getElements() {
		return new ArrayList<T>(hosts);
	}
	
	public boolean join(T h) {
		return hosts.add(h);
	}
	
	public boolean leave(T h) {
		return hosts.remove(h);
	}
}
