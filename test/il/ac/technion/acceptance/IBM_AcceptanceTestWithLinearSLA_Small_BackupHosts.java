/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 19/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.datacenter.sla.guice.HA_TableSLAModule;
import il.ac.technion.gap.guice.ProductionGAPModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class IBM_AcceptanceTestWithLinearSLA_Small_BackupHosts extends
		AbstractAcceptanceTest {

	private static final String fileName = "data1_with_spare_small.csv";
	
	private static Injector inj = Guice.createInjector(new ProductionGAPModule(), new HA_TableSLAModule());
	
	public IBM_AcceptanceTestWithLinearSLA_Small_BackupHosts() {
		super(inj, fileName);
	}
}
