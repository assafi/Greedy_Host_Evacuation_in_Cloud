package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VmDesciption;

import org.joda.time.Period;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LargeVmDescriptionModule extends AbstractModule {

	@Override
	protected void configure() {
	}
	
	@Provides 
	VmDesciption getVmd() {
		return new VmDesciption("Large", 6, Period.minutes(5), SmallVmDescriptionModule.BASE * 4);
	}
}
