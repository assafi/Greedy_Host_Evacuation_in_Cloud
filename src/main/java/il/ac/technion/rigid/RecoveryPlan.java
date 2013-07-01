/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.rigid;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.knapsack.Bin;
import il.ac.technion.knapsack.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.java.contract.Requires;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RecoveryPlan")
public class RecoveryPlan {

	private final Map<Host,List<VM>> rp = new HashMap<Host, List<VM>>();
	
	@XStreamAlias("Cost")
	private double totalCost = 0.0;
	private double recoveryCost = 0.0;
	private double activationCost = 0.0;
	private int recoveredVMsCount = 0;
	private int numActiveHosts = 0;
	private boolean completeRecovery = false;
	
	private static final String delim = System.getProperty("line.separator");
	
	public RecoveryPlan(Collection<Host> hosts) {
		for (Host host : hosts) {
			rp.put(host, new LinkedList<VM>());
		}
	}

	@Requires("binsPackings.length == targetHosts.size()")
	public void add(Bin[] packedBins, List<Host> targetHosts, List<VM> recoveredVMs) {
		for (int i = 0; i < targetHosts.size(); i++) {
			Host target = targetHosts.get(i);
			List<VM> recoveredVMsToTarget = rp.get(target);
			for (Item item : packedBins[i].assignedItems()) {
				VM recoveredVM = recoveredVMs.get(item.id);
				if (recoveredVMsToTarget.size() == 0) {
					updateHostStats(target); 
				}
				recoveredVMsToTarget.add(recoveredVM);
				updateStats(target,recoveredVM);
			}
		}
	}
	
	public void add(Host target, List<VM> recoveredVMs) {
		List<VM> recoveredVMsToTarget = rp.get(target);
		for (VM vm : recoveredVMs) {
			if (recoveredVMsToTarget.size() == 0) {
				updateHostStats(target); 
			}
			recoveredVMsToTarget.add(vm);
			updateStats(target,vm);
		}
	}

	private void updateHostStats(Host target) {
		if (target.active()) {
			numActiveHosts++;
		} 
		totalCost += target.cost();
		activationCost += target.cost();
	}

	private void updateStats(Host target, VM recoveredVM) {
		totalCost += recoveredVM.cost(target);
		recoveryCost += recoveredVM.cost(target);
		recoveredVMsCount++;
	}
	
	public Map<Host,List<VM>> getMap() {
		return new HashMap<Host, List<VM>>(rp);
	}
	
	public int activeHostCount() {
		return numActiveHosts;
	}
	
	public int inactiveHostCount() {
		return rp.keySet().size() - numActiveHosts;
	}
	
	public double cost() {
		return totalCost;
	}
	
	public double recoveryCost() {
		return recoveryCost;
	}
	
	public double activationCost() {
		return activationCost;
	}
	
	public void reset() {
		rp.clear();
		totalCost = 0.0;
		recoveredVMsCount = 0;
	}
	
	public String summary()	 {
		StringBuilder sb = new StringBuilder();
		sb.append("Recovery cost: " + recoveryCost + delim);
		sb.append("Activation cost: " + activationCost + delim);
		sb.append("Total recovery cost: " + totalCost + delim);
		sb.append("#VMs: " + recoveredVMsCount + delim);
		sb.append("#Hosts: " + rp.keySet().size() + delim);
		sb.append("#Active hosts: " + activeHostCount() + delim);
		sb.append("#Inactive hosts: " + inactiveHostCount() + delim);
		sb.append("Complete recovery: " + (isComplete() ? "Yes" : "No") + delim);
		return sb.toString();
	}
	
	/**
	 * @return
	 */
	public String csvReport() {
		String $ = recoveryCost + "," +
				activationCost + "," +
				totalCost + "," + 
				recoveredVMsCount + "," +
				rp.keySet().size() + "," +
				activeHostCount() + "," +
				inactiveHostCount() + "," +
				(isComplete() ? "Yes" : "No");
		return $;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(summary());
		sb.append("===== Recovery breakdown =====" + delim);
		
		List<Host> hosts = new ArrayList<Host>(rp.keySet());
		Collections.sort(hosts, new Comparator<Host>() {

			@Override
			public int compare(Host h1, Host h2) {
				return h1.id() - h2.id();
			}});
		
		for (Host h : hosts) {
			sb.append("Host #" + h.id() + " [" + (h.active() ? "active - " + h.cost() : "inactive") + "]: ");
			for (VM vm : rp.get(h)) {
				sb.append("VM-#" + vm.id + " [" + vm.cost(h) + "] ");
			}
			sb.append(delim);
		}
		return sb.toString();
	}

	public int recoveredVMsCount() {
		return recoveredVMsCount ;
	}
	
	public void full() {
		completeRecovery  = true;
	}
	
	public boolean isComplete() {
		return completeRecovery;
	}
}
