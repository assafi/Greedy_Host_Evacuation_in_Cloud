/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 18/04/2012
 */
package il.ac.technion.datacenter.sla;

import il.ac.technion.misc.PeriodConverter;

import java.util.Random;

import org.joda.time.Period;

public class LinearSLA extends SLA {
	private static Random rand = new Random(System.currentTimeMillis()); 

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

	@Override
	public Period addRandomDowntime(double noise) {
		Period p = Period.seconds((int) (Math.abs(rand.nextGaussian()) * noise / 100.0 * PeriodConverter.toSeconds(billingPeriod)));
		reportDownTime(p);
		return p;
	}
}
