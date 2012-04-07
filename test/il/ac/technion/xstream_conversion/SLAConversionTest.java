/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 28/03/2012
 */
package il.ac.technion.xstream_conversion;

import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;

import com.google.inject.Guice;
import com.google.inject.Injector;



public class SLAConversionTest extends ConversionTest {

	private Injector inj = Guice.createInjector(new AppEngineSLAModule());
	
	@Override
	protected Object getSubject() {
		return inj.getInstance(SLA.class);
	}
}
