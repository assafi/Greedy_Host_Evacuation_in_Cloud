/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 27 באפר 2012
 */
package il.ac.technion.datacenter.sla.guice;

import il.ac.technion.datacenter.sla.Range;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.TableSLA;

import java.util.Map;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author Assaf
 *
 */
public class CustomSLAModule extends AbstractModule {

	final private Map<Range,Double> table;
	public CustomSLAModule(Map<Range,Double> table) {
		super();
		this.table = table;
	}

	@Override
	protected void configure() {
	}

	@Provides
	public SLA getSLA() {
		return new TableSLA(Period.years(1), table);
	}
}
