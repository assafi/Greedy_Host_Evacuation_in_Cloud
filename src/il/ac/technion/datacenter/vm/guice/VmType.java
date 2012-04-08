package il.ac.technion.datacenter.vm.guice;

import il.ac.technion.datacenter.vm.VM;

import com.google.inject.Provider;

public enum VmType {
	SMALL {
		@Override
		public Class<? extends Provider<VM>> getProvider() {
			return SmallVmProvider.class;
		}
	},
	MEDIUM {
		@Override
		public Class<? extends Provider<VM>> getProvider() {
			return MediumVmProvider.class;
		}
	} ,
	LARGE {
		@Override
		public Class<? extends Provider<VM>> getProvider() {
			return LargeVmProvider.class;
		}
	},
	;
	
	public abstract Class<? extends Provider<VM>> getProvider();
}
