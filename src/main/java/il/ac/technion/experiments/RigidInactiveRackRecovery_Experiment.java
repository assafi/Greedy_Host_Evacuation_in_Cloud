/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 20 бреб 2012
 */
package il.ac.technion.experiments;

import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.rigid.RigidRecovery;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;


/**
 * Rack recovery that will not activate additional hosts. 
 * All backup hosts are powered off, and recovery will save their activation cost  
 * but will pay more for the VM recovery.
 * The size of a rack is determine by the number of racks and the number of hosts.
 */
public class RigidInactiveRackRecovery_Experiment implements Experimentable {

	private static Logger logger = Logger.getLogger(RigidInactiveRackRecovery_Experiment.class);
	private static final String delim = System.getProperty("line.separator");
	
	private RigidRecovery rr = null;
	private Placement p = null;
	private int numRacks = 1;
	
	@Inject
	public RigidInactiveRackRecovery_Experiment(RigidRecovery rr, Placement p, int numRacks) {
		this.rr = rr;
		this.p = p;
		this.numRacks = numRacks;
	}
	
	@Override
	public String runExperiment() {
		p.reset();
		logger.info(delim + "===== Rack Recovery with Inactive Backup Hosts =====");
		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",numRacks);
		int maxNumberOfHostsInRack = la.get(0).size();
		logger.info("Max number of hosts in rack: " + maxNumberOfHostsInRack);

		RecoveryPlan rp = rr.affinityRecovery(la);
		logger.info(delim + rp.summary());
//		 logger.info(delim + rp);
		return rp.csvReport();
	}

}
