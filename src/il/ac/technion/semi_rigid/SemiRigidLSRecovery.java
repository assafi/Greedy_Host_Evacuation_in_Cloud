/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.semi_rigid;

import il.ac.technion.datacenter.Host;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.rigid.RigidRecovery;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

public class SemiRigidLSRecovery {

	private RigidRecovery rr = null;
	
	@Inject
	public SemiRigidLSRecovery(RigidRecovery rr) {
		this.rr = rr;
	}
	
	public RecoveryPlan solve(List<Host> hosts) {
		List<Host> inactiveHosts = sieveInactive(hosts);
		
		RecoveryPlan $ = rr.solve(hosts);
		double $cost = cost($,hosts);
		boolean stop;
		
		do {
			stop = true;
			Host nextActive = null;
			for (Host host : inactiveHosts) {
				host.activate();
				RecoveryPlan tempRP = rr.solve(hosts);
				double tempCost = tempRP.cost();
				if (tempCost < $cost) {
					stop = false;
					nextActive = host;
					$cost = tempCost;
					$ = tempRP;
				}
				host.diactivate();
			}
			if (!stop) {
				nextActive.activate();
				inactiveHosts.remove(nextActive);
			}
		} while (!stop);
		return $;
	}

	private List<Host> sieveInactive(List<Host> hosts) {
		List<Host> $ = new ArrayList<Host>(hosts.size());
		for (Host h : hosts) {
			if (!h.isActive())
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
