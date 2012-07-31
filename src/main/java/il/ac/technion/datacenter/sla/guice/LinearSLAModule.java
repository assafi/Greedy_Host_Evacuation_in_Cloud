/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 18/04/2012
 */
package il.ac.technion.datacenter.sla.guice;

import il.ac.technion.datacenter.sla.LinearSLA;
import il.ac.technion.datacenter.sla.SLA;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LinearSLAModule extends AbstractModule {

	private Period billingPeriod = Period.years(1);
	
	public LinearSLAModule() {
		super();
	}

	public LinearSLAModule(Period billingPeriod) {
		super();
		this.billingPeriod = billingPeriod;
	}
	
	@Override
	protected void configure() {
	}
	
	@Provides
	public SLA getSLA() {
		return new LinearSLA(billingPeriod);
	}
}
