/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.sla;

import il.ac.technion.misc.PeriodCoverter;

import java.util.Map;
import java.util.Set;

import org.joda.time.Period;

import com.google.java.contract.Requires;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Custom Implementation of Service License Agreement. Configurable from XML
 * files, validated by sla.xsd
 */
@XStreamAlias("tableSLA")
public class TableSLA extends SLA {

	private Map<Range, Double> compensationTable;
	private double contractAvailability;

	@Requires({
		"billingPeriod != null", 
		"compensationTable != null"
	})
	public TableSLA(Period billingPeriod, Map<Range, Double> compensationTable) {
		super(billingPeriod);
		if (billingPeriod.equals(Period.ZERO))
			throw new IllegalArgumentException(
					"SLA definition must have a non-empty billing period");
		if (!verifyTable(compensationTable))
			throw new IllegalArgumentException("Invalid compensationTable");
		this.compensationTable = compensationTable;
		this.contractAvailability = findRange(100.0, compensationTable.keySet()).left;
	}

	@Override
	public double availabilityContract() {
		return contractAvailability;
	}

	@Override
	@Requires("estimatedDownTime != null")
	public double compensation(Period estimatedDownTime) {
		double availability = super.availability();
		availability -= ((double) PeriodCoverter.toSeconds(estimatedDownTime) / PeriodCoverter.toSeconds(billingPeriod)) * 100.0;
		Range r = null;
		if (availability != 0.0)
			r = findRange(availability);
		else 
			r = getLowestRange();
		return compensationTable.get(r);
	}

	private Range getLowestRange() {
		return getLowestRange(compensationTable.keySet());
	}

	private Range findRange(double availability) {
		return findRange(availability,compensationTable.keySet());
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
			rValue = range.left;
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

	public Range getLowestRange(Set<Range> ranges) {
		Range $ = null;
		for (Range range : ranges) {
			if ($ == null || $.left > range.left)
				$ = range;
		}
		return $;
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
