/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 27, 2013
 */
package il.ac.technion.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

	public static <T> List<T> filter(Collection<T> col, Predicate<T> predicate) {
		List<T> $ = new ArrayList<T>(col.size());
		for(T t: col) {
			if (predicate.accept(t)) {
				$.add(t);
			}
		}
		return $;
	}
	
	public static <T> Tuple<List<T>,List<T>> partition(Collection<T> col, Predicate<T> predicate) {
		List<T> l1 = new ArrayList<T>(col.size());
		List<T> l2 = new ArrayList<T>(col.size());
		Tuple<List<T>,List<T>> $ = new Tuple<>(l1, l2);
		for (T t: col) {
			if (predicate.accept(t)) {
				$._1.add(t);
			} else {
				$._2.add(t);
			}
		}
		return $;
	}
	
	public static <T> boolean find(Collection<T> col, Predicate<T> predicate) {
		for (T t: col) {
			if (predicate.accept(t))
				return true;
		}
		return false;
	}
	
	public static <T> List<Tuple<T,Integer>> zip(Collection<T> col) {
		List<Tuple<T,Integer>> $ = new ArrayList<>(col.size());
		int idx = 0;
		for (T t: col) {
			$.add(new Tuple<>(t,idx++));
		}
		return $;
	}
}
