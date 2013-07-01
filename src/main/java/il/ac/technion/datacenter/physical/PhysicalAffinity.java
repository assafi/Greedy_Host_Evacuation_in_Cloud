/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 03/04/2012
 */
package il.ac.technion.datacenter.physical;

import il.ac.technion.datacenter.vm.VM;

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
	
	public List<Host> hosts() {
		return getElements();
	}

	public List<VM> vms() {
		List<VM> vms = new LinkedList<>();
		for (Host h : hosts()) {
			vms.addAll(h.vms());
		}
		return vms;
	}
	
	/**
	 * @param paList A list of physical affinities.  
	 * @return A list of all hosts residing in the physical affinities list.
	 */
	public static List<Host> extractHosts(Collection<PhysicalAffinity> paList) {
		List<Host> hosts = new LinkedList<>();
		for (PhysicalAffinity pa : paList) {
			hosts.addAll(pa.hosts());
		}
		return hosts;
	}
	
	/**
	 * @param paList A lsit of physical affinities
	 * @return A list of all vms residing in the physical affinities hosts.
	 */
	public static List<VM> extractVMs(Collection<PhysicalAffinity> paList) {
		List<VM> vms = new LinkedList<>();
		for (PhysicalAffinity pa: paList) {
			vms.addAll(pa.vms());
		}
		return vms;
	}
}
