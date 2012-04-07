/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;

import org.joda.time.Period;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class HostConversionTest extends ConversionTest {

	private Injector inj = Guice.createInjector(new AppEngineSLAModule());
	
	@Override
	protected Object getSubject() {
		Host h = new Host(1,10,100,Period.seconds(1000));
		h.assign(new VM(1,1,1.0,inj.getInstance(SLA.class)));
		return h;
	}
}
