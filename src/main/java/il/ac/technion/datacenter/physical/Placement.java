/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.datacenter.physical;

import il.ac.technion.datacenter.physical.guice.PlacementModule;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.guice.ProductionModule;
import il.ac.technion.misc.HashCodeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.java.contract.Requires;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Placement")
public class Placement {

	private static final String delim = System.getProperty("line.separator");
	private List<Host> hosts = null;
	private List<VM> vms = null;
	private Map<Host,List<VM>> originalPlacement = new HashMap<>();
	private GAP_Alg gap = null;
	
	@XStreamOmitField
	private int fHashCode = 0;
	
	public Placement(List<Host> hosts, List<VM> vms) {
		this.hosts = hosts;
		this.vms = vms;
		/*
		 * A default value for GAP is initialized, but no packing is done.
		 * This will be used if repack() is called.
		 */
		Injector injector = Guice.createInjector(new ProductionModule());
		this.gap = injector.getInstance(Key.get(GAP_Alg.class, Names.named("Min GAP")));
		setAsOriginalPlacement(hosts);
	}
	
	@Inject
	public Placement(List<Host> hosts, List<VM> vms, @Named("Max GAP") GAP_Alg gap) {
		this(hosts,vms);
		pack(hosts, vms, gap);
		setAsOriginalPlacement(hosts);
	}

	private void pack(List<Host> hosts, List<VM> vms, GAP_Alg gap) {
		gap.solve(hosts,vms,true);
		for (Host host : hosts) {
			if (host.usedCapacity() == 0) {
				host.deactivate();
			} else {
				host.setActivationCost(0.0);
				host.activate();
			}
		}
	}

	/**
	 *Remembers the current VM assignments. These assignment is restored upon <code>reset()</code> call.
	 * @param hosts The hosts with the VM assignments.
	 */
	private void setAsOriginalPlacement(List<Host> hosts) {
		originalPlacement.clear();
		for (Host host : hosts) {
			originalPlacement.put(host, host.vms());
		}
	}
	
	public void repack(boolean reshuffleVMs) {
		for (Host host : hosts) {
			host.clear();
		}
		if (reshuffleVMs) 
			Collections.shuffle(vms);
		pack(hosts, vms, gap);
	}
	
	public void twoPhaseRepacking(List<VM> firstPhaseVMs, boolean reshuffleVMs) {
		List<VM> secondPhaseVMs = new ArrayList<>(vms);
		secondPhaseVMs.removeAll(firstPhaseVMs);
		vms = firstPhaseVMs;
		repack(reshuffleVMs);
		Injector injector = Guice.createInjector(new PlacementModule(hosts, vms));
		if (reshuffleVMs)
			Collections.shuffle(secondPhaseVMs);
		injector.getInstance(Placement.class);
		secondPhaseVMs.addAll(firstPhaseVMs);
		vms = secondPhaseVMs;
		setAsOriginalPlacement(hosts);
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
	public List<PhysicalAffinity> groupHostsToNAffinities(String affinityName, int n) {
		List<PhysicalAffinity> la = new ArrayList<PhysicalAffinity>(n);
		for (int i = 0; i < n; i++) {
			la.add(new PhysicalAffinity(affinityName, i));
		}
		
		PhysicalAffinity backup = new PhysicalAffinity("backup",n);
		la.add(backup);
		
		for (Host host : hosts) {
			if (host.usedCapacity() != 0) {
				host.join(la.get(host.id() % n));
			} else {
				host.join(backup);
			}
		}
		
		return la;
	}

	public void activateAll() {
		for (Host host : hosts) {
			host.activate();
		}
	}
	
	public boolean reset() {
		boolean $ = true;
		for (Host host : hosts) {
			$ &= host.clear();
			for (VM vm : originalPlacement.get(host)) {
				$ &= host.assign(vm);
			}
			if (host.usedCapacity() == 0) {
				host.deactivate();
			} else {
				host.activate();
			}
		}
		return $;
	}
}
