/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 18/04/2012
 */
package il.ac.technion.datacenter.sla;

import org.joda.time.Period;

public class LinearSLA extends SLA {

	public LinearSLA(Period billingPeriod) {
		super(billingPeriod);
	}

	@Override
	public double availabilityContract() {
		return 100.0;
	}

	@Override
	public double compensation(Period estimatedDownTime) {
		return 100.0 - availability(estimatedDownTime);
	}

	@Override
	public double compensation() {
		return 100.0 - availability();
	}
}
