/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 19/04/2012
 */
package il.ac.technion.datacenter.sla.guice;

import il.ac.technion.datacenter.sla.Range;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.TableSLA;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class HA_TableSLAModule extends AbstractModule {

	private Period billingPeriod = Period.years(1);
	
	public HA_TableSLAModule() {
	}
	
	public HA_TableSLAModule(Period billingPeriod) {
		super();
		this.billingPeriod = billingPeriod;
	}

	@Override
	protected void configure() {
	}

	@Provides
	public SLA getSLA() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.9999,100.0), 0.0);
		badTable.put(new Range(99.999,99.9999), 0.1);
		badTable.put(new Range(99.99,99.999), 0.15);
		badTable.put(new Range(99.95,99.99), 0.20);
		badTable.put(new Range(0.0,99.95), 0.5);
		return new TableSLA(billingPeriod,badTable);
	}
}
