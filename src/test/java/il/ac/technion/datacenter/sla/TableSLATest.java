/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.datacenter.sla;

import il.ac.technion.misc.PeriodConverter;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;


public class TableSLATest {
	
	private Period p = new Period(1,0,0,0,0,0,0,0); // one year
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPeriod() {
		new TableSLA(new Period(),null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyTable() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable1() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(0,100), -1.0);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable2() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(0,99.0), 1.0);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable3() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(0.0,101.0), 1.0);
		new TableSLA(p,badTable);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable4() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.99,100.0), 0.0);
		badTable.put(new Range(99.0,99.95), 0.25);
		badTable.put(new Range(95.0,99.0), 0.1);
		badTable.put(new Range(0.0,95.0), 0.5);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOverlap() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.99,100.0), 0.0);
		badTable.put(new Range(99.0,99.95), 0.1);
		badTable.put(new Range(95.0,99.0), 0.25);
		badTable.put(new Range(0.0,97.0), 0.5);
		new TableSLA(p,badTable);
	}
	
	@Test
	public void testGoogleAppEngineSLA() {
		Map<Range,Double> goodTable = new HashMap<Range, Double>();
		goodTable.put(new Range(99.95,100.0), 0.0);
		goodTable.put(new Range(99.0,99.95), 0.1);
		goodTable.put(new Range(95.0,99.0), 0.25);
		goodTable.put(new Range(0.0,95.0), 0.5);
		new TableSLA(p,goodTable);
	}
	
	@Test
	public void testRandomDowntime_noNoise() {
		Map<Range,Double> slaTable = new HashMap<Range, Double>();
		slaTable.put(new Range(99.95,100.0), 0.0);
		slaTable.put(new Range(99.0,99.95), 0.1);
		slaTable.put(new Range(95.0,99.0), 0.25);
		slaTable.put(new Range(0.0,95.0), 0.5);
		SLA sla = new TableSLA(p,slaTable);
		Period downtime = sla.addRandomDowntime(0);
		Assert.assertTrue(PeriodConverter.toMillis(downtime) <= 0.0005 * PeriodConverter.toMillis(p));
	}
	
	@Test
	public void testRandomDowntime_maxNoise() {
		Map<Range,Double> slaTable = new HashMap<Range, Double>();
		slaTable.put(new Range(99.95,100.0), 0.0);
		slaTable.put(new Range(99.0,99.95), 0.1);
		slaTable.put(new Range(95.0,99.0), 0.25);
		slaTable.put(new Range(0.0,95.0), 0.5);
		SLA sla = new TableSLA(p,slaTable);
		Period downtime = sla.addRandomDowntime(1);
		Assert.assertTrue(PeriodConverter.toMillis(downtime) >= 0.05 * PeriodConverter.toMillis(p));
	}
}
