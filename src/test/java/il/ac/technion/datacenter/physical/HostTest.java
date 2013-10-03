/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22 бреб 2012
 */
package il.ac.technion.datacenter.physical;

import junit.framework.Assert;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.guice.VmType;

import org.joda.time.Period;
import org.junit.Test;

public class HostTest {

	/**
	 * Test method for {@link il.ac.technion.datacenter.physical.Host#unassign(il.ac.technion.datacenter.vm.VM)}.
	 */
	@Test
	public void testAssignment() {
		Host host = new Host(0, 1, 1.0, Period.ZERO);
		host.deactivate();
		VM vm = VmType.SMALL.createVm();
		Assert.assertTrue(host.assign(vm));
		Assert.assertEquals(vm.size(), host.usedCapacity());
		Assert.assertTrue(host.unassign(vm));
		Assert.assertEquals(0, host.usedCapacity());
		
		Assert.assertTrue(host.assign(vm));
		host.activate();
		Assert.assertTrue(host.unassign(vm));
		host.deactivate();
		Assert.assertEquals(0, host.usedCapacity());
		
		Assert.assertTrue(host.assign(vm));
		host.activate();
		Assert.assertTrue(host.clear());
		Assert.assertTrue(host.active());
		Assert.assertTrue(host.assign(vm));
		Assert.assertTrue(host.unassign(vm));
		host.deactivate();
		
	}

}
