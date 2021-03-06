/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 27/03/2012
 */
package il.ac.technion.semi_rigid;


import static org.junit.Assert.assertFalse;
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

public class SemiRigidRecoveryTest {

	private Injector inj = Guice.createInjector(new ProductionModule(), new AppEngineSLAModule());
	private SemiRigidLSRecovery srr = inj.getInstance(SemiRigidLSRecovery.class);
	
	private static int testCount = 1;
	
	private SLA sla1 = null;
	private SLA sla2 = null;
	
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
	public void testSRRSimpleNoActivation() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h2 = new Host(1, 512, 0.0, Period.minutes(3));
		Host h3 = new Host(2, 512, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, sla1);
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		vm1.addBootTime(h3, Period.days(5));
		h1.assign(vm1);
		h1.activate();
		
		VM vm2 = new VM(1, 512, 100.0, sla2);
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		vm2.addBootTime(h3, Period.days(5));
		h2.assign(vm2);
		h2.activate();
		
		hosts.add(h1);
		hosts.add(h2);
		hosts.add(h3);
		h3.deactivate();
		
		RecoveryPlan rp = srr.hostsRecovery(hosts);
		System.out.println(rp);
		assertTrue(rp.getMap().get(h3).contains(vm1));
		assertTrue(rp.getMap().get(h3).contains(vm2));
		assertFalse(h3.active());
	}
	
	@Test
	public void testSRRSimpleWithActivation() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h2 = new Host(1, 512, 0.0, Period.minutes(3));
		Host h3 = new Host(2, 512, 10.0, Period.minutes(100));
		
		VM vm1 = new VM(0, 512, 100.0, sla1);
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		vm1.addBootTime(h3, Period.minutes(200));
		h1.assign(vm1);
		h1.activate();
		
		VM vm2 = new VM(1, 512, 100.0, sla2);
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		vm2.addBootTime(h3, Period.minutes(200));
		h2.assign(vm2);
		h2.activate();
		
		hosts.add(h1);
		hosts.add(h2);
		hosts.add(h3);
		h3.deactivate();
		
		RecoveryPlan rp = srr.hostsRecovery(hosts);
		System.out.println(rp);
		assertTrue(rp.getMap().get(h3).contains(vm1));
		assertTrue(rp.getMap().get(h3).contains(vm2));
		assertTrue(h3.active());
	}
	
	@Test
	public void testSRRSimpleWithPartialActivation() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h0 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 0.0, Period.minutes(3));
		Host h2 = new Host(2, 512, 10.0, Period.minutes(100));
		Host h3 = new Host(3, 256, 2.0, Period.minutes(100));
		
		VM vm0 = new VM(0, 256, 70.0, sla1);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		vm0.addBootTime(h2, Period.minutes(200));
		vm0.addBootTime(h3, Period.minutes(200));
		h0.assign(vm0);
		h0.activate();
		
		VM vm1 = new VM(1, 256, 70.0, sla2);
		vm1.addBootTime(h0, Period.days(3));
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.minutes(200));
		vm1.addBootTime(h3, Period.minutes(200));
		h0.assign(vm1);
		
		h1.deactivate();
		
		hosts.add(h0);
		hosts.add(h1);
		hosts.add(h2);
		hosts.add(h3);
		h2.deactivate();
		h3.deactivate();
		
		RecoveryPlan rp = srr.hostsRecovery(hosts);
		System.out.println(rp);
		assertTrue(2.0 == rp.cost());
		assertTrue(h3.active());
	}
	
	@Test
	public void testComplexAffinityRecovery() {
		List<PhysicalAffinity> al = new ArrayList<PhysicalAffinity>(3);

		for (int i = 0; i < 3; i++) {
			al.add(new PhysicalAffinity("test-affinity",i));
		}
				
		Host h0 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 0.0, Period.minutes(3));
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
		
		RecoveryPlan rp = srr.affinityRecovery(al);
		System.out.println(rp);
		assertTrue(rp.getMap().get(h3).contains(vm0));
		assertTrue(rp.getMap().get(h2).contains(vm1));
	}
}
