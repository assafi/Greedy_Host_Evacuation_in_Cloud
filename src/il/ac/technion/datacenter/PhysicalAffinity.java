/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 03/04/2012
 */
package il.ac.technion.datacenter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A physical collection of hosts.
 * All share a common resource that may fail. 
 * For example, a group of hosts may join a common Rack,
 * Network switch or a Power region. 
 */
@XStreamAlias("PhysicalAffinity")
public class PhysicalAffinity extends Affinity<Host>{

	public PhysicalAffinity(String type, int id) {
		super(type,id);
	}
	
	public List<Host> getHosts() {
		return getElements();
	}

	public List<VM> getVMs() {
		List<VM> vms = new LinkedList<VM>();
		for (Host h : getHosts()) {
			vms.addAll(h.vms());
		}
		return vms;
	}
	
	public static List<Host> extractHosts(Collection<PhysicalAffinity> paList) {
		List<Host> hosts = new LinkedList<Host>();
		for (PhysicalAffinity pa : paList) {
			hosts.addAll(pa.getHosts());
		}
		return hosts;
	}
}
