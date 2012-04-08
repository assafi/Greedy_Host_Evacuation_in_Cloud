package il.ac.technion.datacenter.sla.guice;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.datacenter.sla.Range;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.TableSLA;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AppEngineSLAModule extends AbstractModule {

	private Period billingPeriod = Period.years(1);
	
	public AppEngineSLAModule() {
		super();
	}
	
	public AppEngineSLAModule(Period billingPeriod) {
		super();
		this.billingPeriod = billingPeriod;
	}

	@Override
	protected void configure() {
	}

	@Provides
	public SLA getSLA() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.95,100.0), 0.0);
		badTable.put(new Range(99.0,99.95), 0.1);
		badTable.put(new Range(95.0,99.0), 0.25);
		badTable.put(new Range(0.0,95.0), 0.5);
		return new TableSLA(billingPeriod,badTable);
	}
}
