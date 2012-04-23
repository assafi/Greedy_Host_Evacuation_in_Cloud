package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VmDesciption;

import org.joda.time.Period;

import com.google.inject.AbstractModule;

public class SmallVmDescriptionModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(VmDesciption.class).toInstance(new VmDesciption("Small", 1, Period.minutes(3), 276));
	}
}
