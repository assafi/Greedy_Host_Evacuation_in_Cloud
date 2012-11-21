/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.misc;

import org.joda.time.Period;

public class PeriodConverter {
	public static long toMillis(Period p) {
		long years = p.getYears();
		long months = p.getMonths();
		long days = p.getDays();
		long hours = p.getHours();
		long minutes = p.getMinutes();
		long seconds = p.getSeconds();
		long millis = p.getMillis();
		return ((((((((years*365 + months*30 + days)*24) + hours)*60) + minutes)*60) + seconds)*1000) + millis;
	}
	
	public static long toSeconds(Period p) {
		long years = p.getYears();
		long months = p.getMonths();
		long days = p.getDays();
		long hours = p.getHours();
		long minutes = p.getMinutes();
		long seconds = p.getSeconds() + p.getMillis() / 1000;
		return ((((((years*365 + months*30 + days)*24) + hours)*60) + minutes)*60) + seconds;
	}
}
