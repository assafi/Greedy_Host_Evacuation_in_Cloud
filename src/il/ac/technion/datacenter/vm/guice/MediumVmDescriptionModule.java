package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VmDesciption;

import org.joda.time.Period;

import com.google.inject.AbstractModule;

public class MediumVmDescriptionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VmDesciption.class).toInstance(new VmDesciption("Medium", 2,Period.minutes(4), 552.0));
	}
}
