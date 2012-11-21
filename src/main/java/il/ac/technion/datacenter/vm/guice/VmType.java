package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.config.TestConfiguration;
import il.ac.technion.datacenter.sla.SLA;
import il.ac.technion.datacenter.sla.guice.AppEngineSLAModule;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.VmDesciption;

import com.google.inject.Guice;
import com.google.inject.Injector;

public enum VmType {	
	SMALL {
		private Injector vmdInj = Guice.createInjector(new SmallVmDescriptionModule());

		@Override
		public VmDesciption getVmDescription() {
			return vmdInj.getInstance(VmDesciption.class);
		} 
	},
	MEDIUM {
		private Injector vmdInj = Guice.createInjector(new MediumVmDescriptionModule());

		@Override
		public VmDesciption getVmDescription() {
			return vmdInj.getInstance(VmDesciption.class);
		} 
	} ,
	LARGE {
		private Injector vmdInj = Guice.createInjector(new LargeVmDescriptionModule());
		
		@Override
		public VmDesciption getVmDescription() {
			return vmdInj.getInstance(VmDesciption.class);
		} 
	},
	;
	
	private static Injector slaInjector = Guice.createInjector(new AppEngineSLAModule()); // Default SLA injector
	
	public VM createVm() {
		VmDesciption vmd = getVmDescription();
		SLA sla = slaInjector.getInstance(SLA.class);
		return new VM(Sequencer.INSTANCE.get(), sla, vmd);
	}
	
	public abstract VmDesciption getVmDescription();
	
	public static void setSlaInjector(Injector _slaInjector) {
		slaInjector = _slaInjector;
	}
	
	public static void configure(TestConfiguration tConfig) {
		slaInjector = tConfig.getSlaInjector();
	}
}
