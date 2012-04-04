/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.Host;
import il.ac.technion.datacenter.VM;
import il.ac.technion.datacenter.sla.SLABuilder;

import org.joda.time.Period;


public class HostConversionTest extends ConversionTest {

	@Override
	protected Object getSubject() {
		Host h = new Host(1,10,100,Period.millis(1000));
		h.assign(new VM(1,1,1.0,new SLABuilder(Period.millis(1000)).appEngineSLA()));
		return h;
	}
}
