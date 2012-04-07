/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 26/03/2012
 */
package il.ac.technion.misc;

import org.joda.time.Period;

public class PeriodCoverter {
	public static long toMillis(Period p) {
		int years = p.getYears();
		int months = p.getMonths();
		int days = p.getDays();
		int hours = p.getHours();
		int minutes = p.getMinutes();
		int seconds = p.getSeconds();
		int millis = p.getMillis();
		return (((((((((years*365 + months*30) + days)*24) + hours)*60) + minutes)*60) + seconds)*1000) + millis;
	}
	
	public static long toSeconds(Period p) {
		int years = p.getYears();
		int months = p.getMonths();
		int days = p.getDays();
		int hours = p.getHours();
		int minutes = p.getMinutes();
		int seconds = p.getSeconds() + p.getMillis() / 1000;
		return (((((((years*365 + months*30) + days)*24) + hours)*60) + minutes)*60) + seconds;
	}
}
