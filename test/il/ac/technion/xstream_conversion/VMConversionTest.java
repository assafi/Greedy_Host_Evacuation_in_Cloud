/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 28/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.Host;
import il.ac.technion.datacenter.VM;
import il.ac.technion.datacenter.sla.SLABuilder;

import org.joda.time.Period;



public class VMConversionTest extends ConversionTest {

	@Override
	protected Object getSubject() {
		Host h1 = new Host(0, 512, 0.0, Period.minutes(3));
		VM vm1 = new VM(0, 512, 100.0, new SLABuilder().appEngineSLA());
		vm1.addBootTime(h1, Period.hours(1));
		return vm1;
	}
}
