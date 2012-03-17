/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.sla;

import java.io.File;

import org.joda.time.Period;

/**
 * Custom Implementation of Service License Agreement.
 * Configurable from XML files, validated by sla.xsd 
 */
public class CustomSLA extends SLA {

	/**
	 * @param billingPeriod
	 */
	public CustomSLA(File xml) {
		super(billingPeriod);
	}

	@Override
	public double availability() {
		return 0;
	}

	@Override
	public double availabilityContract() {
		return 0;
	}

	@Override
	public double compensation(Period estimatedDownTime) {
		return 0;
	}

	@Override
	public double compensation() {
		return 0;
	}

}
