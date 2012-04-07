/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter.sla;

import il.ac.technion.misc.PeriodCoverter;

import org.joda.time.Period;

import com.google.java.contract.Ensures;

/**
 *	Service License Agreement of Cloud Providers.
 *  Exposes the CP Availability commitments, and the 
 *  compensation scale.  
 */
public abstract class SLA {

	final public Period billingPeriod;
	
	private Period totalDownTime;
	
	/**
	 * @param billingPeriod E.g: <code>new Period().withYears(1);</code>
	 */
	public SLA(Period billingPeriod) {
		this.billingPeriod = billingPeriod;
		this.totalDownTime = new Period();
	}

	/**
	 * @return the current period of unavailability. 
	 */
	public Period getDownTime() {
		return totalDownTime;
	}
	
	/**
	 * Reports a period of unavailability. All reported
	 * periods are accumulated, thus a compensation to the client
	 * may be needed.
	 */
	public void reportDownTime(Period downTime) {
		totalDownTime = totalDownTime.plus(downTime);
	}

	/**
	 * @return A percentage of yearly recorded availability.
	 * e.g. 99.95, 99.99, 99.999 
	 */
	@Ensures({
		"result <= 100.0",
		"result >= 0.0"
	})
	public double availability() {
		return ((double)PeriodCoverter.toMillis(billingPeriod.minus(totalDownTime)) / 
		PeriodCoverter.toMillis(billingPeriod)) * 100.0;
	}
	
	/**
	 * @return A percentage of yearly availability the CP commits upon.
	 * e.g. 99.95, 99.99, 99.999 
	 */
	@Ensures({
		"result <= 100.0",
		"result > 0.0"
	})
	public abstract double availabilityContract();
	
	/**
	 * Returns the amount of compensation the client will be entitled
	 * to, in case the total down-time is increased by the <code>estimatedDownTime</code>.  
	 * @return If <code>estimatedDownTime</code> is a 0-length period will return 
	 * the compensation entitled to the client due to <code>getDownTime() + estimatedDownTime</code>
	 * period exceeding (or not) the allowed availability in the SLA. 
	 * In case the time period does not exceed the allowed down time period, the returned
	 * compensation is 0.   
	 */
	@Ensures({
		"zeroCostCheck(old(estimatedDownTime),result)",
		"result >= 0.0"}) 
	public abstract double compensation(Period estimatedDownTime);
	
	/**
	 * Returns the amount of compensation the client will be entitled
	 * to.  
	 * @return The compensation entitled to the client due to <code>getDownTime()</code>
	 * period exceeding (or not) the allowed availability in the SLA. 
	 * In case the time period does not exceed the allowed down time period, the returned
	 * compensation is 0.
	 */
	@Ensures("result >= 0.0")
	public abstract double compensation();
	
	@SuppressWarnings("unused")
	private boolean zeroCostCheck(Period estimatedDownTime, double result) {
		if (PeriodCoverter.toMillis(totalDownTime.plus(estimatedDownTime)) < 
					(100.0-availabilityContract())*PeriodCoverter.toMillis(billingPeriod)) 
			return result == 0.0;
		return true;
	}
}
