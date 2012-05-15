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
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.rigid.RigidRecovery;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

public class AbstractAcceptanceTest {
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
	
	@Ignore
	@Test
	public void testSemiRigidHostRecovery() throws IOException, DataException {
		Placement p = dc.convert(tConfig);
		System.out.println("===== Placement =====");
		System.out.println(p);
		RecoveryPlan rp = srr.hostsRecovery(p.hosts());
		System.out.println(rp);
	}
	
//	@Ignore
	@Test
	public void testSemiRigidAffinityOfK() throws IOException, DataException {
		Placement p = dc.convert(tConfig);
		System.out.println("===== Placement =====");
		System.out.println(p);
		
		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",tConfig.getNumAffinities());
//		List<PhysicalAffinity> la = p.groupKHostsToNAffinities("Rack",tConfig.getNumAffinities(),tConfig.getNumAffinities());
		
		RecoveryPlan rp = srr.affinityRecovery(la);
		System.out.println(rp);
	}
	
//	@Ignore
	@Test
	public void testInactiveBackupsRigidAffinityOfK() throws IOException, DataException {
		Placement p = dc.convert(tConfig);
		System.out.println("===== Placement =====");
		System.out.println(p);
		
		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",tConfig.getNumAffinities());
//		List<PhysicalAffinity> la = p.groupKHostsToNAffinities("Rack",tConfig.getNumAffinities(),tConfig.getNumAffinities());
		
		RecoveryPlan rp = rr.affinityRecovery(la);
		System.out.println(rp);
	}
	
//	@Ignore
	@Test
	public void testActiveBackupsRigidAffinityOfK() throws IOException, DataException {
		Placement p = dc.convert(tConfig);
		p.activateAll();
		System.out.println("===== Placement =====");
		System.out.println(p);
		
		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",tConfig.getNumAffinities());
//		List<PhysicalAffinity> la = p.groupKHostsToNAffinities("Rack",tConfig.getNumAffinities(),tConfig.getNumAffinities());
		
		RecoveryPlan rp = rr.affinityRecovery(la);
		System.out.println(rp);
	}
}
