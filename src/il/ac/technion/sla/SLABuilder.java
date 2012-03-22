/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 22/03/2012
 */
package il.ac.technion.sla;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;

public class SLABuilder {
	
	private Period billingPeriod = Period.years(1);
	
	public SLABuilder() {}
	
	public SLABuilder(Period billingPeriod) {
		this.billingPeriod = billingPeriod;
	}
	
	public void billingPeriod(Period p) {
		this.billingPeriod = p;
	}
	
	public SLA appEngineSLA() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.95,100.0), 0.0);
		badTable.put(new Range(99.0,99.95), 0.1);
		badTable.put(new Range(95.0,99.0), 0.25);
		badTable.put(new Range(0.0,95.0), 0.5);
		return new TableSLA(billingPeriod,badTable);
	}
}
