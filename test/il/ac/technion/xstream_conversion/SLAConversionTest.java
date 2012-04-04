/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 28/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.sla.SLABuilder;



public class SLAConversionTest extends ConversionTest {

	@Override
	protected Object getSubject() {
		return new SLABuilder().appEngineSLA();
	}
}
