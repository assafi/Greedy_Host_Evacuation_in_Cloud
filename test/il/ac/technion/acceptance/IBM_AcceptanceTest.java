/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.data.DataConverter;
import il.ac.technion.data.DataException;
import il.ac.technion.data.IbmDataCoverter;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.gap.guice.ProductionGAPModule;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class IBM_AcceptanceTest {
	private static final String fileName = "data1.csv";
	private InputStream csvIn = null;
	private DataConverter dc = null;
	
	private Injector inj = Guice.createInjector(new ProductionGAPModule(), new AppEngineSLAModule());
	private SemiRigidLSRecovery srr = inj.getInstance(SemiRigidLSRecovery.class);
	
	@Before
	public void setUp() throws Exception {
		csvIn = getClass().getResourceAsStream(fileName);
		dc = new IbmDataCoverter();
	}
	
	@Test
	public void ibmDataTestHostRecovery() throws IOException, DataException {
		Placement p = dc.convert(csvIn);
		System.out.println("===== Placement =====");
		System.out.println(p);
		RecoveryPlan rp = srr.hostsRecovery(p.hosts());
		System.out.println(rp);
	}
	
	@Test
	public void ibmDataTestGroup40Affinity() throws IOException, DataException {
		Placement p = dc.convert(csvIn);
		System.out.println("===== Placement =====");
		System.out.println(p);
		
		List<PhysicalAffinity> la = p.groupHostsToNAffinities("Rack",4);
		
		RecoveryPlan rp = srr.affinityRecovery(la);
		System.out.println(rp);
	}
}
