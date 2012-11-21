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
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

/**
 * Rack recovery that can activate additional hosts. 
 * The size of a rack is determine by the number of racks and the number of hosts.
 */
public class SemiRigidRackRecovery_Experiment implements Experimentable {
	
	private static Logger logger = Logger.getLogger(SemiRigidRackRecovery_Experiment.class);
	private static final String delim = System.getProperty("line.separator");
	
	private SemiRigidLSRecovery srr = null;
	private Placement p = null;
	private int numRacks = 1;
	
	@Inject
	public SemiRigidRackRecovery_Experiment(SemiRigidLSRecovery srr, Placement p, int numRacks) {
		this.srr = srr;
		this.p = p;
		this.numRacks = numRacks;
	}
	
	@Override
	public String runExperiment() {
		p.reset();
		logger.info(delim + "===== Rack Recovery with Backup Hosts Activation =====");

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",numRacks);
		int maxNumberOfHostsInRack = la.get(0).size();
		logger.info("Max number of hosts in rack: " + maxNumberOfHostsInRack);

		RecoveryPlan rp = srr.affinityRecovery(la);
		logger.info(delim + rp.summary());
//		 logger.info(delim + rp);
		return rp.csvReport();
	}

}
