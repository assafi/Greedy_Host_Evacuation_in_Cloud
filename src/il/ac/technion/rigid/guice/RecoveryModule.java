/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.rigid.guice;

import il.ac.technion.gap.GAP_Alg;
import il.ac.technion.gap.min.conversion.guice.MinGAP_LRMax_Module;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public class RecoveryModule extends AbstractModule {

	@Override
	protected void configure() {
		Injector inj = Guice.createInjector(new MinGAP_LRMax_Module());
		bind(GAP_Alg.class).annotatedWith(Names.named("Min GAP")).to(
				inj.getInstance(GAP_Alg.class).getClass());
	}
}
