/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.datacenter;


import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.guice.VmType;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.junit.Test;

public class PlacementTest {

	@Test
	public void print() {
		List<Host> hosts = new ArrayList<Host>(2);
		List<VM> vms = new ArrayList<VM>(2);
		Host h0 = new Host(0, 2, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 2, 0.0, Period.minutes(3));
		Host h2 = new Host(2, 2, 10.0, Period.minutes(100));
		Host h3 = new Host(3, 1, 2.0, Period.minutes(100));
		
		VM vm0 = VmType.SMALL.createVm();
		vms.add(vm0);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		vm0.addBootTime(h2, Period.minutes(200));
		vm0.addBootTime(h3, Period.minutes(200));
		h0.assign(vm0);
		h0.activate();
		
		VM vm1 = VmType.SMALL.createVm();
		vms.add(vm1);
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
		
		Placement p = new Placement(hosts, vms);
		System.out.println(p);
	}
}
