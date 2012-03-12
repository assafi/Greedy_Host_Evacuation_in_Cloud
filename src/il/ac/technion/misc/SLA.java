/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.misc;

import org.joda.time.Period;

import com.google.java.contract.Ensures;

/**
 *	Service License Agreement of Cloud Providers.
 *  Exposes the CP Availability commitments, and the 
 *  compensation scale.  
 */
public abstract class SLA {

	final public Period billingPeriod;
	private Period downTime;
	
	/**
	 * @return the current period of unavailability. 
	 */
	public Period getDownTime() {
		return downTime;
	}

	/**
	 * @param billingPeriod E.g: <code>new Period().withYears(1);</code>
	 */
	public SLA(Period billingPeriod) {
		this.billingPeriod = billingPeriod;
		this.downTime = new Period();
	}
	
	/**
	 * @return A percentage of yearly recorded availability.
	 * e.g. 99.95, 99.99, 99.999 
	 */
	@Ensures({
		"result <= 100.0",
		"result >= 0.0"
	})
	public abstract double availability();
	
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
		"downTime.plus(old(estimatedDownTime)).getMillis() <= " +
		"		(100.0-availabilityContract())*billingPeriod.getMillis() => 0"})
	public abstract double compensation(Period estimatedDownTime);
}
