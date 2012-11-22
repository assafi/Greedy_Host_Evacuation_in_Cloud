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

import junit.framework.Assert;
import junitparams.JUnitParamsRunner;
import static junitparams.JUnitParamsRunner.$;
import junitparams.Parameters;

import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PlacementTest {

	private List<Host> hosts = new ArrayList<>();
	private List<VM> vms = new ArrayList<>();
	
	public PlacementTest(List<Host> hosts, List<VM> vms) {
		this.hosts = hosts;
		this.vms = vms;
	}
	
	public Object[] generateTopology() {
		
		List<Host> hosts = new ArrayList<Host>(4);
		List<VM> vms = new ArrayList<VM>(2);
		Host h0 = new Host(0, 10, 100.0, Period.minutes(3));
		Host h1 = new Host(1, 10, 100.0, Period.minutes(3));
		
		VM vm0 = VmType.SMALL.createVm();
		vms.add(vm0);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		h0.assign(vm0);
		h0.activate();
		
		VM vm1 = VmType.SMALL.createVm();
		vms.add(vm1);
		vm1.addBootTime(h0, Period.days(3));
		vm1.addBootTime(h1, Period.hours(1));
		h0.assign(vm1);
		
		h1.deactivate();
		
		hosts.add(h0);
		hosts.add(h1);

		return $(
					$(hosts, vms)
				);
	}
	
	@Test
	@Parameters(method = "generateTopology")
	public void testReset(List<Host> hosts, List<VM> vms) {
		
	}
}
