package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.VmDesciption;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class MediumVmProvider implements Provider<VM> {

	private Injector inj = Guice.createInjector(new AppEngineSLAModule());
	
	@Override
	public VM get() {
		SLA sla = inj.getInstance(SLA.class);
		VmDesciption vmd = new VmDesciption("Medium", 2, 30.0, sla);
		return new VM(Sequencer.INSTANCE.get(), vmd);
	}
}
