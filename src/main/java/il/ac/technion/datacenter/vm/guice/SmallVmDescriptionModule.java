package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VmDesciption;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class SmallVmDescriptionModule extends AbstractModule {
	
	public static final double ALPHA = 300.0;
	public static final long BASE = (long) (276 * ALPHA);
	
	@Override
	protected void configure() {
	}
	
	@Provides
	public VmDesciption getVmd() {
		return new VmDesciption("Small", 1, Period.minutes(3), BASE);
	}
}
