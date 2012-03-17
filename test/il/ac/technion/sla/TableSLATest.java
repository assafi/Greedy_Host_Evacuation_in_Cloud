/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.sla;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Period;
import org.junit.Test;


public class TableSLATest {
	
	private Period p = new Period(100);
	
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
		badTable.put(new Range(100,0), -1.0);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable2() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(99.0,0), 1.0);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable3() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(101.0,0.0), 1.0);
		new TableSLA(p,badTable);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidTable4() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(100.0,99.99), 0.0);
		badTable.put(new Range(99.95,99.0), 0.25);
		badTable.put(new Range(99.0,95.0), 0.1);
		badTable.put(new Range(95.0,0.0), 0.5);
		new TableSLA(p,badTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOverlap() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(100.0,99.99), 0.0);
		badTable.put(new Range(99.95,99.0), 0.1);
		badTable.put(new Range(99.0,95.0), 0.25);
		badTable.put(new Range(97.0,0.0), 0.5);
		new TableSLA(p,badTable);
	}
	
	public void testGoogleAppEngineSLA() {
		Map<Range,Double> badTable = new HashMap<Range, Double>();
		badTable.put(new Range(100.0,99.99), 0.0);
		badTable.put(new Range(99.95,99.0), 0.1);
		badTable.put(new Range(99.0,95.0), 0.25);
		badTable.put(new Range(95.0,0.0), 0.5);
		new TableSLA(p,badTable);
	}
}
