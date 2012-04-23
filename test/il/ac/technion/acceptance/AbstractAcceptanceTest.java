/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 19/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.data.DataConverter;
import il.ac.technion.data.DataException;
import il.ac.technion.data.IbmDataCoverter;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.rigid.RecoveryPlan;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

public class AbstractAcceptanceTest {
	private final String fileName;
	private InputStream csvIn = null;
	private DataConverter dc = null;
	
	private Injector inj = null;
	private SemiRigidLSRecovery srr = null;
	
	public AbstractAcceptanceTest(Injector inj, String fileName) {
		this.inj = inj;
		this.srr = inj.getInstance(SemiRigidLSRecovery.class);
		this.fileName = fileName;
	}
	
	@Before
	public void setUp() throws Exception {
		csvIn = getClass().getResourceAsStream(fileName);
		dc = new IbmDataCoverter(inj);
	}
	
	@Ignore
	@Test
	public void testHostRecovery() throws IOException, DataException {
		Placement p = dc.convert(csvIn);
		System.out.println("===== Placement =====");
		System.out.println(p);
		RecoveryPlan rp = srr.hostsRecovery(p.hosts());
		System.out.println(rp);
	}
	
	@Test
	public void testAffinityOf4() throws IOException, DataException {
		Placement p = dc.convert(csvIn);
		System.out.println("===== Placement =====");
		System.out.println(p);
		
		List<PhysicalAffinity> la = p.groupKHostsToNAffinities("Rack",136,4);
		
		RecoveryPlan rp = srr.affinityRecovery(la);
		System.out.println(rp);
	}
}
