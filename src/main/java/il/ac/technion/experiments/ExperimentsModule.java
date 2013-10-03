/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 20 бреб 2012
 */
package il.ac.technion.experiments;

import il.ac.technion.config.TestConfiguration;
import il.ac.technion.data.DataConverter;
import il.ac.technion.data.DataCoverterImpl;
import il.ac.technion.data.DataException;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.glpk.VMRPLowLevelImpl;
import il.ac.technion.rigid.RigidRecovery;
import il.ac.technion.semi_rigid.SemiRigidLSRecovery;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ExperimentsModule extends AbstractModule {

	private TestConfiguration tConfig = null;
	private Placement placement = null;
	private static DataConverter dc = new DataCoverterImpl();
	
	public ExperimentsModule(TestConfiguration tConfig) throws IOException, DataException {
		this.tConfig = tConfig;
		this.placement = dc.convert(tConfig);
	}
	
	@Override
	protected void configure() {
		bind(Placement.class).toInstance(placement);
	}
	
	@Provides
	@Experiment
	public List<Experimentable> getExperiments(SemiRigidLSRecovery srr, RigidRecovery rr) {
		return Arrays.asList(new Experimentable[]{
//				new RigidActiveRackRecovery_Experiment(rr, placement, tConfig.getNumAffinities()),
//				new RigidInactiveRackRecovery_Experiment(rr, placement, tConfig.getNumAffinities()),
				new SemiRigidRackRecovery_Experiment(srr, placement, tConfig.getNumAffinities()),
//				new SemiRegidHostRecovery_Experiment(srr, placement),
				new OptimalRecovery_Experiment(new VMRPLowLevelImpl(), placement, tConfig.getNumAffinities()),
		});
	}

}
