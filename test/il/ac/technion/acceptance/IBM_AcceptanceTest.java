/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.gap.guice.ProductionGAPModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class IBM_AcceptanceTest extends AbstractAcceptanceTest {
	private static final String fileName = "data1.csv";
	
	private static Injector inj = Guice.createInjector(new ProductionGAPModule(), new AppEngineSLAModule());
	
	public IBM_AcceptanceTest() {
		super(inj,fileName);
	}
}
