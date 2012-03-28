/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.rigid;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import il.ac.technion.gap.guice.ProductionModule;
import il.ac.technion.misc.Host;
import il.ac.technion.misc.VM;
import il.ac.technion.sla.SLABuilder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RigidRecoveryTest {

	private Injector inj = Guice.createInjector(new ProductionModule());
	private RigidRecovery rr = inj.getInstance(RigidRecovery.class);
	
	@Test
	public void testRigidSimple() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(1, 512, 1000.0, Period.minutes(3));
		Host h3 = new Host(2, 512, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, new SLABuilder().appEngineSLA());
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		vm1.addBootTime(h3, Period.days(5));
		h1.assign(vm1);
		
		VM vm2 = new VM(1, 512, 100.0, new SLABuilder().appEngineSLA());
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		vm2.addBootTime(h3, Period.days(5));
		h2.assign(vm2);
		
		hosts.add(h1);
		hosts.add(h2);
		hosts.add(h3);
		
		RecoveryPlan rp = rr.solve(hosts);
		assertTrue(rp.getMap().get(h3).contains(vm1));
		assertTrue(rp.getMap().get(h3).contains(vm2));
	}
	
	@Test
	public void testIncompleteRecovery() {
		List<Host> hosts = new ArrayList<Host>(2);
		Host h1 = new Host(0, 512, 1000.0, Period.minutes(3));
		Host h2 = new Host(1, 1024, 1000.0, Period.minutes(3));
		
		VM vm1 = new VM(0, 512, 100.0, new SLABuilder().appEngineSLA());
		vm1.addBootTime(h1, Period.hours(1));
		vm1.addBootTime(h2, Period.days(3));
		h1.assign(vm1);
		
		VM vm2 = new VM(1, 512, 100.0, new SLABuilder().appEngineSLA());
		vm2.addBootTime(h1, Period.days(3));
		vm2.addBootTime(h2, Period.hours(1));
		h2.assign(vm2);
		
		hosts.add(h1);
		hosts.add(h2);
		
		RecoveryPlan rp = rr.solve(hosts);
		assertTrue(rp.getMap().get(h2).contains(vm1));
		assertFalse(rp.isComplete());
	}
}
