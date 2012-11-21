/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 20 бреб 2012
 */
package il.ac.technion.experiments;

import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

/**
 * Host recovery that can activate additional hosts.
 */
public class SemiRegidHostRecovery_Experiment implements Experimentable {

	private static Logger logger = Logger.getLogger(SemiRegidHostRecovery_Experiment.class);
	private static final String delim = System.getProperty("line.separator");
	
	private SemiRigidLSRecovery srr = null;
	private Placement p = null;
	
	@Inject
	public SemiRegidHostRecovery_Experiment(SemiRigidLSRecovery srr, Placement p) {
		this.srr = srr;
		this.p = p;
	}
	
	@Override
	public String runExperiment() {
		p.reset();
		logger.info(delim + "===== Hosts Recovery with Backup Hosts Activation =====");
		RecoveryPlan rp = srr.hostsRecovery(p.hosts());
		logger.info(delim + rp.summary());
		// logger.info(delim + rp);
		return rp.csvReport();
	}
}
