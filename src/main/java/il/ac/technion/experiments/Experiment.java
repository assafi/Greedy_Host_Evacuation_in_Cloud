/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 20 бреб 2012
 */
package il.ac.technion.experiments;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface Experiment {
	
}
