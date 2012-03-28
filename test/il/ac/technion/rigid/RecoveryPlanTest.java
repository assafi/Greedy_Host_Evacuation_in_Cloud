/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.rigid;

import static org.junit.Assert.assertTrue;
import il.ac.technion.misc.Bin;
import il.ac.technion.misc.Host;
import il.ac.technion.misc.Item;
import il.ac.technion.misc.VM;
import il.ac.technion.sla.SLABuilder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.junit.Test;

public class RecoveryPlanTest {

	@Test
	public void testAdd() {
		Bin[] bins = new Bin[2];
		bins[0] = new Bin(0,2);
		bins[1] = new Bin(1,3);
		List<Host> hosts = new ArrayList<Host>(2);
		hosts.add(new Host(bins[0],1.0,Period.millis(1000)));
		hosts.add(new Host(bins[1],1.0,Period.millis(1000)));
		RecoveryPlan rp = new RecoveryPlan(hosts);
		VM vm = new VM(0, 2, 100.0, new SLABuilder().appEngineSLA());
		vm.addBootTime(hosts.get(0), Period.days(3));
		vm.addBootTime(hosts.get(1), Period.days(10));
		List<VM> vms = new ArrayList<VM>(1);
		vms.add(vm);
		bins[0].assign(new Item(vm.id,vm.ram,vm.cost(hosts.get(0))));
		rp.add(bins, hosts, vms);
		
		assertTrue(rp.getMap().containsKey(hosts.get(0)));
		assertTrue(rp.getMap().get(hosts.get(0)).contains(vm));
		assertTrue(10.0 == rp.cost());
	}

	@Test
	public void testReset() {
		Bin[] bins = new Bin[2];
		bins[0] = new Bin(0,2);
		bins[1] = new Bin(1,3);
		List<Host> hosts = new ArrayList<Host>(2);
		hosts.add(new Host(bins[0],1.0,Period.millis(1000)));
		hosts.add(new Host(bins[1],1.0,Period.millis(1000)));
		VM vm = new VM(0, 2, 100.0, new SLABuilder().appEngineSLA());
		vm.addBootTime(hosts.get(0), Period.days(3));
		vm.addBootTime(hosts.get(1), Period.days(10));
		List<VM> vms = new ArrayList<VM>(1);
		vms.add(vm);
		bins[0].assign(new Item(vm.id,vm.ram,vm.cost(hosts.get(0))));
		RecoveryPlan rp = new RecoveryPlan(hosts);
		rp.add(bins, hosts, vms);
		rp.reset();
		assertTrue(0.0 == rp.cost());
	}
}
