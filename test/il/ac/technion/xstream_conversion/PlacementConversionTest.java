/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 02/04/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PlacementConversionTest extends ConversionTest {

	private Injector inj = Guice.createInjector(new AppEngineSLAModule());
	
	@Override
	protected Object getSubject() {
		List<Host> hosts = new ArrayList<Host>(2);
		List<VM> vms = new ArrayList<VM>(2);
		Host h0 = new Host(0, 512, 0.0, Period.minutes(3));
		Host h1 = new Host(1, 512, 0.0, Period.minutes(3));
		Host h2 = new Host(2, 512, 10.0, Period.minutes(100));
		Host h3 = new Host(3, 256, 2.0, Period.minutes(100));
		
		VM vm0 = new VM(0, 256, 70.0, inj.getInstance(SLA.class));
		vms.add(vm0);
		vm0.addBootTime(h0, Period.hours(1));
		vm0.addBootTime(h1, Period.days(3));
		vm0.addBootTime(h2, Period.minutes(200));
		vm0.addBootTime(h3, Period.minutes(200));
		h0.assign(vm0);
		h0.activate();
		
		VM vm1 = new VM(1, 256, 70.0, inj.getInstance(SLA.class));
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
		
		return new Placement(hosts, vms);
	}
}
