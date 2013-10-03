/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 29, 2013
 */
package il.ac.technion.glpk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.guice.ProductionModule;
import il.ac.technion.rigid.RecoveryPlan;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AbstractVMRPTests {

	private Injector inj = Guice.createInjector(new ProductionModule(), new AppEngineSLAModule());
	
	private static int testCount = 1;
	
	private SLA sla1 = null;
	private SLA sla2 = null;

	private final VMRP vmrp;
	
	protected AbstractVMRPTests(VMRP vmrp) {
		this.vmrp = vmrp;
	}
	
	@Before
	public void initSLA() {
		sla1 = inj.getInstance(SLA.class);
		sla2 = inj.getInstance(SLA.class);
	}
	
	@Before
	public void printTitle() {
		System.out.println("===== Test #" + testCount++ + " =====");
		System.out.println();
	}
	
	@Test
	public void recoverTwoVMsToSecondHost() throws Exception {
		List<PhysicalAffinity> al = new ArrayList<PhysicalAffinity>(2);

		for (int i = 0; i < 2; i++) {
			al.add(new PhysicalAffinity("test-affinity",i));
		}
				
		Host h0 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 2.0, Period.minutes(100));
		
		VM vm0 = new VM(0, 256, 70.0, sla1);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.minutes(200));
		h0.assign(vm0);
				
		VM vm1 = new VM(1, 256, 70.0, sla2);
		vm1.addBootTime(h0, Period.days(3));
		vm1.addBootTime(h1, Period.minutes(200));
		h0.assign(vm1);
		
		h0.activate();
		h0.join(al.get(0));
		
		h1.deactivate();
		h1.join(al.get(1));
		
		RecoveryPlan rp = vmrp.solve(al);
		System.out.println(rp);
		assertTrue(rp.getMap().get(h1).contains(vm0));
		assertTrue(rp.getMap().get(h1).contains(vm1));
		assertEquals(2,rp.cost(),0.000001);
	}

	@Test
	public void solveComplexAffinityProblem() throws Exception {
		List<PhysicalAffinity> al = new ArrayList<PhysicalAffinity>(3);

		for (int i = 0; i < 2; i++) {
			al.add(new PhysicalAffinity("test-affinity",i));
		}
				
		Host h0 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 1.0, Period.minutes(3));
		Host h2 = new Host(2, 512, 10.0, Period.minutes(100));
		Host h3 = new Host(3, 256, 2.0, Period.minutes(100));
		
		VM vm0 = new VM(0, 256, 70.0, sla1);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		vm0.addBootTime(h2, Period.minutes(200));
		vm0.addBootTime(h3, Period.minutes(200));
		h0.assign(vm0);
				
		VM vm1 = new VM(1, 256, 70.0, sla2);
		vm1.addBootTime(h0, Period.days(3));
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.minutes(200));
		vm1.addBootTime(h3, Period.minutes(200));
		h0.assign(vm1);
		
		h0.activate();
		h0.join(al.get(0));
		
		h1.deactivate();
		h1.join(al.get(0));
		
		h2.deactivate();
		h2.join(al.get(1));
		
		h3.deactivate();
		h3.join(al.get(1));
		
		RecoveryPlan rp = vmrp.solve(al);
		System.out.println(rp);
		assertEquals(1,rp.numVMs(h3));
		assertEquals(1,rp.numVMs(h2));
	}
}
