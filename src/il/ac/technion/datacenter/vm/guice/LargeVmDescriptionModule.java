package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VmDesciption;

import org.joda.time.Period;

import com.google.inject.AbstractModule;

public class LargeVmDescriptionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VmDesciption.class).toInstance(new VmDesciption("Large", 6, Period.minutes(5), 1104.0));
	}
}
