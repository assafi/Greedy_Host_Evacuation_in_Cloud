/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.rigid;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.gap.guice.ProductionGAPModule;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RigidRecoveryTest {

	private Injector inj = Guice.createInjector(new ProductionGAPModule(), new AppEngineSLAModule());
	private RigidRecovery rr = inj.getInstance(RigidRecovery.class);
	
	private SLA sla1 = null;
	private SLA sla2 = null;
	
	@Before
	public void initSLA() {
		sla1 = inj.getInstance(SLA.class);
		sla2 = inj.getInstance(SLA.class);
	}
	
	@Test
	public void testRigidSimple() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(1, 512, 1000.0, Period.minutes(3));
		Host h3 = new Host(2, 512, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, sla1);
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		vm1.addBootTime(h3, Period.days(5));
		h1.assign(vm1);
		
		VM vm2 = new VM(1, 512, 100.0, sla1);
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		vm2.addBootTime(h3, Period.days(5));
		h2.assign(vm2);
		
		hosts.add(h1);
		hosts.add(h2);
		hosts.add(h3);
		
		RecoveryPlan rp = rr.hostsRecovery(hosts);
		assertTrue(rp.getMap().get(h3).contains(vm1));
		assertTrue(rp.getMap().get(h3).contains(vm2));
	}
	
	@Test
	public void testIncompleteRecovery() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(1, 1024, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, sla1);
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		h1.assign(vm1);
		
		VM vm2 = new VM(1, 512, 100.0, sla2);
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		h2.assign(vm2);
		
		hosts.add(h1);
		hosts.add(h2);
		
		RecoveryPlan rp = rr.hostsRecovery(hosts);
		assertTrue(rp.getMap().get(h2).contains(vm1));
		assertFalse(rp.isComplete());
	}
	
	@Test
	public void testSimpleAffinityRecovery() {
		List<PhysicalAffinity> al = new ArrayList<PhysicalAffinity>(3);

		for (int i = 0; i < 3; i++) {
			al.add(new PhysicalAffinity("test-affinity",i));
		}
				
		Host h0 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(2, 512, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, sla1);
		vm1.addBootTime(h0, Period.hours(1));
		vm1.addBootTime(h1, Period.days(3));
		vm1.addBootTime(h2, Period.days(5));
		h0.assign(vm1);
		h0.join(al.get(0));
		
		VM vm2 = new VM(1, 512, 100.0, sla2);
		vm2.addBootTime(h0, Period.days(3));
		vm2.addBootTime(h1, Period.hours(1));
		vm2.addBootTime(h2, Period.days(5));
		h1.assign(vm2);
		h1.join(al.get(1));
		
		h2.join(al.get(2));
		
		RecoveryPlan rp = rr.affinityRecovery(al);
		assertTrue(rp.getMap().get(h2).contains(vm1));
		assertTrue(rp.getMap().get(h2).contains(vm2));
	}
	
	@Test
	public void testComplexAffinityRecovery() {
		List<PhysicalAffinity> al = new ArrayList<PhysicalAffinity>(3);

		for (int i = 0; i < 3; i++) {
			al.add(new PhysicalAffinity("test-affinity",i));
		}
				
		Host h0 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(2, 512, 1000.0, Period.minutes(3));
		Host h3 = new Host(3, 512, 1000.0, Period.minutes(3));
		
		VM vm0 = new VM(0, 512, 100.0, sla1);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		vm0.addBootTime(h2, Period.days(5));
		h0.assign(vm0);
		h0.join(al.get(0));
		h1.join(al.get(0));
		
		VM vm1 = new VM(1, 512, 100.0, sla2);
		vm1.addBootTime(h0, Period.days(3));
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(5));
		h2.assign(vm1);
		h2.join(al.get(1));
		h3.join(al.get(1));
		
		RecoveryPlan rp = rr.affinityRecovery(al);
		assertTrue(rp.getMap().get(h3).contains(vm0));
		assertTrue(rp.getMap().get(h1).contains(vm1));
	}
}
