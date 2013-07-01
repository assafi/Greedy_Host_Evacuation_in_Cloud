/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 27, 2013
 */
package il.ac.technion.misc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class LazyIterator<T> implements Iterator<T> {

	private Iterator<Collection<T>> globalIterator;
	private Iterator<T> localIterator;
	
	@SafeVarargs
	public LazyIterator(Collection<T>... collections) {
		this.globalIterator = CollectionUtils.filter(Arrays.asList(collections), new Predicate<Collection<T>>(){
			@Override
			public boolean accept(Collection<T> t) {
				return !t.isEmpty();
			}
		}).iterator();
		this.localIterator = globalIterator.next().iterator();
	}
	
	@Override
	public boolean hasNext() {
		return localIterator.hasNext() || globalIterator.hasNext();
	}

	@Override
	public T next() {
		if (localIterator.hasNext())
			return localIterator.next();
		localIterator = globalIterator.next().iterator();
		return localIterator.next();
	}

	@Override
	public void remove() {
		localIterator.remove();
	}
}
