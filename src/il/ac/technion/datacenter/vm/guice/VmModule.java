package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VM;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

public class VmModule extends AbstractModule {

	private Class<? extends Provider<VM>> provider_class;
	
	public VmModule(VmType vmType) {
		super();
		this.provider_class = vmType.getProvider();
	}
	
	@Override
	protected void configure() {
		bind(VM.class).toProvider(provider_class);
	}
}
