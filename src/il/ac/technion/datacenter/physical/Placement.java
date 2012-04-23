/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.datacenter.physical;

import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.misc.HashCodeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.java.contract.Requires;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Placement")
public class Placement {

	private static final String delim = System.getProperty("line.separator");
	private List<Host> hosts = new LinkedList<Host>();
	private List<VM> vms = new LinkedList<VM>();
	
	@XStreamOmitField
	private int fHashCode = 0;
	
	public Placement(List<Host> hosts, List<VM> vms) {
		this.hosts = hosts;
		this.vms = vms;
	}
	
	@Inject
	public Placement(List<Host> hosts, List<VM> vms, @Named("Min GAP") GAP_Alg gap) {
		this(hosts,vms);
		gap.solve(hosts,vms);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Placement))
			return false;
		Placement p = (Placement)o;
		return hosts.equals(p.hosts) && vms.equals(p.vms);
	}
	
	@Override
	public int hashCode() {
		if (fHashCode  == 0) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, hosts);
			result = HashCodeUtil.hash(result, vms);
			fHashCode = result;
		}
		return fHashCode;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Host h : hosts) {
			sb.append(h + delim);
		}
		return sb.toString();
	}
	
	public List<Host> hosts() {
		return hosts;
	}
	
	public int numHosts() {
		return hosts.size();
	}
	
	public List<VM> vms() {
		return vms;
	}
	
	public int numVMs() {
		return vms.size();
	}

	@Requires("n > 0")
	public List<PhysicalAffinity> groupKHostsToNAffinities(String affinityName, int k, int n) {
		List<PhysicalAffinity> la = new ArrayList<PhysicalAffinity>(n);
		for (int i = 0; i < n; i++) {
			la.add(new PhysicalAffinity(affinityName, i));
		}
		
		PhysicalAffinity backup = new PhysicalAffinity("backup",n);
		la.add(backup);
		
		for (Host host : hosts) {
			if (host.id() <= k) {
				host.join(la.get(host.id() % n));
			} else {
				host.join(backup);
			}
		}
		
		return la;
	}
}
