/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.datacenter.sla;

import il.ac.technion.misc.PeriodConverter;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
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
	private static Random coin = new Random(System.currentTimeMillis());

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
		double availability = availability(estimatedDownTime);
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
		return compensation(Period.ZERO);
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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TableSLA))
			return false;
		TableSLA sla = (TableSLA)obj;
		if (!sla.compensationTable.equals(this.compensationTable)
				|| sla.contractAvailability != this.contractAvailability)
			return false;
		return true;
	}

	@Override
	@Requires("old(noise) <= 1.0 && old(noise) >= 0")
	public Period addRandomDowntime(double noise) {
		Iterator<Range> iter = new RangeIterator(compensationTable.keySet());
		Range r = iter.next();
		while (iter.hasNext()) {
			if (coin.nextDouble() >= noise) 
				break;
			r = iter.next();
		}
		double downTimePerc = 100.0 - (coin.nextDouble()*r.size + r.left);
		long downtimeInMillis = (long) (PeriodConverter.toMillis(billingPeriod) * (downTimePerc / 100.0));
		Period $ = new Period(downtimeInMillis);
		reportDownTime($);
		return $;
	}
	
	/**
	 * Range iterator, starting from the highest range to the lower.
	 * Ranges must be mutually exclusive and complete.
	 */
	private class RangeIterator implements Iterator<Range> {
		
		private Set<Range> ranges;
		private Range current = null;
		private Range next = null;
		
		public RangeIterator(Set<Range> ranges) {
			this.ranges = ranges;
			next = getHighestRange();
		}
		
		private Range getLowestRange() {
			Range $ = null;
			for (Range range : ranges) {
				if ($ == null || $.left > range.left)
					$ = range;
			}
			return $;
		}
		
		private Range getHighestRange() {
			Range $ = null;
			for (Range range : ranges) {
				if ($ == null || $.right < range.right)
					$ = range;
			}
			return $;
		}
		
		private Range findRange(double value) {
			for (Range range : ranges) {
				if (range.inRange(value)) {
					return range;
				}
			}
			return null;
		}
		
		private int rangeCount(double value) {
			int count = 0;
			for (Range range : ranges) {
				if (range.inRange(value)) {
					++count;
				}
			}
			return count;
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Range next() {
			current = next;
			if (next != null) {
				next = findRange(current.left);
			}
			return current;
		}

		@Override
		public void remove() {
			if (current != null) {
				ranges.remove(current);	
			}
		}
	}
}
