/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 19/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.config.TestConfiguration;
import il.ac.technion.data.DataConverter;
import il.ac.technion.data.DataCoverterImpl;
import il.ac.technion.data.DataException;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.rigid.RigidRecovery;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.inject.Injector;

public class AbstractAcceptanceTest {

	private static final int EXPENSIVE_COST_FACTOR = 5;

	private static final String delim = System.getProperty("line.separator");

	private static Logger logger = Logger.getLogger(AbstractAcceptanceTest.class);

	private DataConverter dc = new DataCoverterImpl();
	private final TestConfiguration tConfig;

	private Injector inj = null;
	private SemiRigidLSRecovery srr = null;
	private RigidRecovery rr = null;

	public AbstractAcceptanceTest(TestConfiguration tConfig) {
		this.tConfig = tConfig;
		this.inj = tConfig.getSlaInjector();
		this.srr = inj.getInstance(SemiRigidLSRecovery.class);
		this.rr = inj.getInstance(RigidRecovery.class);
	}

//	@Ignore
	@Test
	public void testSemiRigidHostRecovery() throws IOException, DataException {
		logger.info(delim + "===== Semi-Rigid Host Recovery =====");
		Placement p = dc.convert(tConfig);
		// logger.info("===== Placement =====");
		// logger.info(p);
		RecoveryPlan rp = srr.hostsRecovery(p.hosts());
		logger.info(delim + rp.summary());
		// logger.info(delim + rp);
	}

//	@Ignore
	@Test
	public void testSemiRigidAffinityOfK() throws IOException, DataException {
		logger.info(delim + "===== Semi-Rigid Rack Recovery =====");
		Placement p = dc.convert(tConfig);
		 logger.info("===== Placement =====");
		 logger.info(p);

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		updateExpensiveAffinities(la,tConfig);

		RecoveryPlan rp = srr.affinityRecovery(la);
//		logger.info(delim + rp.summary());
		 logger.info(delim + rp);
	}

//	@Ignore
	@Test
	public void testInactiveBackupsRigidAffinityOfK() throws IOException,
			DataException {
		logger.info(delim + "===== Rigid Inactive Rack Recovery =====");
		Placement p = dc.convert(tConfig);
		// logger.info("===== Placement =====");
		// logger.info(p);

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		updateExpensiveAffinities(la,tConfig);

		RecoveryPlan rp = rr.affinityRecovery(la);
//		logger.info(delim + rp.summary());
		 logger.info(delim + rp);
	}

//	@Ignore
	@Test
	public void testActiveBackupsRigidAffinityOfK() throws IOException,
			DataException {
		logger.info(delim + "===== Rigid Active Rack Recovery =====");
		Placement p = dc.convert(tConfig);
		p.activateAll();
//		logger.info("===== Placement =====");
//		logger.info(p);

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		updateExpensiveAffinities(la,tConfig);

		RecoveryPlan rp = rr.affinityRecovery(la);
//		logger.info(delim + rp.summary());
		 logger.info(delim + rp);
	}
	
//	@Ignore
	@Test
	public void test2PriceModelsPacking_ActiveRecovery() throws IOException,
			DataException {
		logger.info(delim + "===== Rigid Active Rack Recovery =====");
		logger.info("***** Compact Expensive Packing *****");
		Placement p = dc.convert(tConfig);
		p.activateAll();
		logger.info("===== Placement =====");
		logger.info(p);

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		updateExpensiveAffinities(la,tConfig);

		RecoveryPlan rp = rr.affinityRecovery(la);
//		logger.info(delim + rp.summary());
		logger.info(delim + rp);
		
		logger.info("***** Distributed Expensive Packing *****");
		p.repack(true);
		p.activateAll();
		logger.info("===== Placement =====");
		logger.info(p);
		la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		rp = rr.affinityRecovery(la);
//	logger.info(delim + rp.summary());
	  logger.info(delim + rp);
	}
	
	@Test
	public void test2PriceModelsPacking_SemiRigidRecovery() throws IOException,
			DataException {
		logger.info(delim + "===== Semi Rigid Rack Recovery =====");
		logger.info("***** Compact Expensive Packing *****");
		Placement p = dc.convert(tConfig);
		logger.info("===== Placement =====");
		logger.info(p);

		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		updateExpensiveAffinities(la,tConfig);

		RecoveryPlan rp = srr.affinityRecovery(la);
//		logger.info(delim + rp.summary());
		logger.info(delim + rp);
		
		logger.info("***** Distributed Expensive Packing *****");
		p.repack(true);
		logger.info("===== Placement =====");
		logger.info(p);
		la = p.groupHostsToNAffinities("Rack",
				tConfig.getNumAffinities());
		rp = srr.affinityRecovery(la);
//	logger.info(delim + rp.summary());
	  logger.info(delim + rp);
	}
	
	private void updateExpensiveAffinities(List<PhysicalAffinity> la,
			int numExpensiveRacks, int contractFactor) {
		if (numExpensiveRacks == 0) return;
		for (PhysicalAffinity physicalAffinity : la) {
			if (physicalAffinity.id < numExpensiveRacks) {
				for (VM vm : physicalAffinity.getVMs()) {
					vm.type.setContractCost(vm.type.getContractCost() * contractFactor);
				}
			}
		}
	}
	
	private void updateExpensiveVMs(List<PhysicalAffinity> la, int numExpensiveVMs, int contractFactor) {
		for (int i = 0; i < numExpensiveVMs; i++) {
			PhysicalAffinity pa = la.get(i % la.size());
			if (pa.type == "backup") {
				pa = la.get(0);
			}
			List<Host> hostsInPA = pa.getHosts();
			Host host = hostsInPA.get(i % hostsInPA.size());
			int j = 1;
			while (host.freeCapacity() == host.capacity()) {
				host = hostsInPA.get((i + j++) % hostsInPA.size());
				if (j == hostsInPA.size() + 1) {
					break;
				}
			}
			if (host.freeCapacity() == host.capacity()) continue;
			List<VM> vmsInHost = host.vms();
			VM vm = vmsInHost.get(i % vmsInHost.size());
			vm.type.setContractCost(vm.type.getContractCost() * contractFactor);
		}
	}
	
	private void updateExpensiveAffinities(List<PhysicalAffinity> la,
			TestConfiguration configuration) {
		if (!configuration.expensiveProcessing()) return;
		if (configuration.distributedExpensive()) {
			updateExpensiveVMs(la, configuration.getNumExpensiveVMs(),EXPENSIVE_COST_FACTOR);
		} else {
			updateExpensiveAffinities(la,configuration.getNumExpensiveRacks(),EXPENSIVE_COST_FACTOR);
		}
	}
}
