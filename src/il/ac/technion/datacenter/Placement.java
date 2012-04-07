/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.datacenter;

import il.ac.technion.misc.HashCodeUtil;

import java.util.LinkedList;
import java.util.List;

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
		super();
		this.hosts = hosts;
		this.vms = vms;
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
}
