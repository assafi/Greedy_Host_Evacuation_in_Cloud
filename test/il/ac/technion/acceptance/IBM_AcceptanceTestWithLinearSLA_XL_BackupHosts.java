/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.acceptance;

import il.ac.technion.datacenter.sla.guice.LinearSLAModule;
import il.ac.technion.gap.guice.ProductionGAPModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class IBM_AcceptanceTestWithLinearSLA_XL_BackupHosts extends AbstractAcceptanceTest {
	private static final String fileName = "data1_with_spare_extra_large.csv";
	
	private static Injector inj = Guice.createInjector(new ProductionGAPModule(), new LinearSLAModule());
	
	public IBM_AcceptanceTestWithLinearSLA_XL_BackupHosts() {
		super(inj,fileName);
	}
}
