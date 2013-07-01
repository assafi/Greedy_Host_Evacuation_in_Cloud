/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.semi_rigid;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.HostConfig;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.rigid.RigidRecovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

/**
 * VM fault recovery with host activations.
 * The LS algorithm iteratively activates rigid recovery algorithm,
 * and looks for the most beneficial host to activate at every step. 
 */
public class SemiRigidLSRecovery {

	private RigidRecovery rigidRecovery = null;
	
	@Inject
	public SemiRigidLSRecovery(RigidRecovery rr) {
		this.rigidRecovery = rr;
	}
	
	public RecoveryPlan affinityRecovery(List<PhysicalAffinity> pal) {
		List<Host> hosts = PhysicalAffinity.extractHosts(pal);
		List<Host> inactiveHosts = sieveActive(hosts);
		
		RecoveryPlan $ = rigidRecovery.affinityRecovery(pal);
		double $cost = cost($,hosts);
		boolean stop;
		
		do {
			stop = true;
			Host nextActive = null;
			Set<HostConfig> triedHostsConfigs = new HashSet<HostConfig>(); // Optimization 
			for (Host host : inactiveHosts) {
				HostConfig hc = host.getConfig();
				if (triedHostsConfigs.contains(hc)) continue;
				triedHostsConfigs.add(hc);
				host.activate();
				RecoveryPlan tempRP = rigidRecovery.affinityRecovery(pal);
				double tempCost = tempRP.cost();
				if (tempCost < $cost) {
					stop = false;
					nextActive = host;
					$cost = tempCost;
					$ = tempRP;
				}
				host.deactivate();
			}
			if (!stop) {
				nextActive.activate();
				inactiveHosts.remove(nextActive);
			}
		} while (!stop);
		return $;
	}
	
	public RecoveryPlan hostsRecovery(List<Host> hosts) {
		List<Host> inactiveHosts = sieveActive(hosts);
		
		RecoveryPlan $ = rigidRecovery.hostsRecovery(hosts);
		double $cost = cost($,hosts);
		boolean stop;
		
		do {
			stop = true;
			Host nextActive = null;
			for (Host host : inactiveHosts) {
				host.activate();
				RecoveryPlan tempRP = rigidRecovery.hostsRecovery(hosts);
				double tempCost = tempRP.cost();
				if (tempCost < $cost) {
					stop = false;
					nextActive = host;
					$cost = tempCost;
					$ = tempRP;
				}
				host.deactivate();
			}
			if (!stop) {
				nextActive.activate();
				inactiveHosts.remove(nextActive);
			}
		} while (!stop);
		return $;
	}

	/**
	 * Filter out all active hosts from the given list. Maintains order.
	 * @param hosts A list of hosts
	 * @return A list of inactive hosts. 
	 */
	private List<Host> sieveActive(List<Host> hosts) {
		List<Host> $ = new ArrayList<Host>(hosts.size());
		for (Host h : hosts) {
			if (!h.active())
				$.add(h);
		}
		return $;
	}

	private double cost(RecoveryPlan rp, List<Host> hosts) {
		double $ = rp.cost();
		for (Host host : hosts) {
			$ += host.cost();
		}
		return $;
	}
}
