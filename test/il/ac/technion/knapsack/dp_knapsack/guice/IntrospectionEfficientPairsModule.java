/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 12/03/2012
 */
package il.ac.technion.knapsack.dp_knapsack.guice;

import il.ac.technion.knapsack.dp_knapsack.Pair;

import java.util.Collection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class IntrospectionEfficientPairsModule extends AbstractModule {

	private Collection<Pair> colInstance = null;
	
	public IntrospectionEfficientPairsModule(Collection<Pair> _colInstance) {
		this.colInstance = _colInstance;
	}
	
	@Override
	protected void configure() {
		bind(new TypeLiteral<Collection<Pair>>(){})
		.annotatedWith(Names.named("EP_Collection")).toInstance(colInstance);
	}
}
