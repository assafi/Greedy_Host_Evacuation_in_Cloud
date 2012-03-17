/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.sla;

import java.util.Map;
import java.util.Set;

import org.joda.time.Period;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Custom Implementation of Service License Agreement. Configurable from XML
 * files, validated by sla.xsd
 */
@XStreamAlias("tableSLA")
public class TableSLA extends SLA {

	private Map<Range, Double> compensationTable;
	private double contractAvailability;

	public TableSLA(Period billingPeriod, Map<Range, Double> compensationTable) {
		super(billingPeriod);
		if (billingPeriod.getMillis() == 0)
			throw new IllegalArgumentException(
					"SLA definition must have a non-empty billing period");
		if (!verifyTable(compensationTable))
			throw new IllegalArgumentException("Invalid compensationTable");
		this.compensationTable = compensationTable;
		this.contractAvailability = findRange(100.0, compensationTable.keySet()).right;
	}

	@Override
	public double availabilityContract() {
		return contractAvailability;
	}

	@Override
	public double compensation(Period estimatedDownTime) {
		double availability = super.availability()
				- (double) estimatedDownTime.getMillis() / billingPeriod.getMillis();
		Range r = findRange(availability, compensationTable.keySet());
		return compensationTable.get(r);
	}

	@Override
	public double compensation() {
		return compensation(new Period());
	}

	private boolean verifyTable(Map<Range, Double> cTable) {
		double rValue = 100.0;
		double lastCompensation = 0.0;

		Range startRange = findRange(rValue, cTable.keySet());
		if (startRange == null || cTable.get(startRange) != 0.0)
			return false;

		while (rValue > 0.0) {
			if (rangeCount(rValue,cTable.keySet()) > 1)
				return false; // Ranges overlaping
			Range range = findRange(rValue, cTable.keySet());
			if (range == null) {
				return false;
			}
			rValue = range.right;
			double compensationPerc = cTable.get(range);
			if (100 >= compensationPerc && compensationPerc >= lastCompensation) {
				lastCompensation = compensationPerc;
				continue;
			}
			return false;
		}
		return rValue == 0.0;
	}

	private int rangeCount(double value, Set<Range> ranges) {
		int count = 0;
		for (Range range : ranges) {
			if (range.inRange(value)) {
				++count;
			}
		}
		return count;
	}

	private Range findRange(double value, Set<Range> ranges) {
		for (Range range : ranges) {
			if (range.inRange(value)) {
				return range;
			}
		}
		return null;
	}
}
