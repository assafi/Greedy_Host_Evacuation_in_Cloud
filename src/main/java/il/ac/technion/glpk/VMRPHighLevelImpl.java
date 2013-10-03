/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jul 1, 2013
 */
package il.ac.technion.glpk;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.PhysicalAffinity;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.misc.CollectionUtils;
import il.ac.technion.misc.Predicate;
import il.ac.technion.misc.Tuple;
import il.ac.technion.rigid.RecoveryPlan;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.xypron.linopt.Problem;
import de.xypron.linopt.Solver;
import de.xypron.linopt.SolverGlpk;

public class VMRPHighLevelImpl implements VMRP {

	private static final String ASSIGNMENT_CONSTRAINT = "C1";
	private static Logger logger = Logger.getLogger(VMRPHighLevelImpl.class);
	private static final String PROB_NAME = "Virtual Machine Recovery Problem";
	private static final String Y_ON_COL = "y^on";
	private static final String Y_OFF_COL = "y^off";
	private static final String M_COL = "m";
	
	private static final String OBJECTIVE = "RecoveryCost";
	private static final double VERY_LARG_COST = 100000;
	private static final double TIMELIME = 60;
	
	public RecoveryPlan solve(List<PhysicalAffinity> pal) throws Exception {
		Problem p = createProblemFormulation(pal);
		Solver s = new SolverGlpk();
		s.setTimeLimit(TIMELIME);
		if (!s.solve(p)) {
			logger.error("The problem could not be solved");
			throw new Exception("The problem could not be solved");
		} 
		if (logger.isEnabledFor(Level.TRACE)) {
			logger.trace(p.problemToString());
		}
		logger.info("Solution found!");
		logger.info("Optimal cost: " + p.objective().getValue());
		return toRecoveryPlan(p,pal);
	}

	private RecoveryPlan toRecoveryPlan(final Problem p, List<PhysicalAffinity> pal) {
		RecoveryPlan rp = new RecoveryPlan(PhysicalAffinity.extractHosts(pal));
		Tuple<List<Host>,List<Host>> H_bar = CollectionUtils.partition(PhysicalAffinity.extractHosts(pal), new Predicate<Host>(){
			@Override
			public boolean accept(Host h) {
				return h.active();
			}
		});
		
		final List<VM> V_bar = PhysicalAffinity.extractVMs(pal);
		for (int i = 0; i < hBarLength(H_bar); i++) {
			logger.debug(p.column(yNameByIndex(H_bar,i), i).getKey()+"="+p.column(yNameByIndex(H_bar,i), i).getValue());
			if (p.column(yNameByIndex(H_bar,i), i).getValue() == 1.0) {
				Host h = hostByIndex(H_bar,i); 
				if (p.column(yNameByIndex(H_bar,i), i).getKey().contains(Y_ON_COL)) {
					h.activate();
				} else {
					h.deactivate();
				}
				final int finalI = i;
				rp.add(h, CollectionUtils.filter(V_bar, new Predicate<VM>(){
					@Override
					public boolean accept(VM vm) {
						return p.column(M_COL, finalI, V_bar.indexOf(vm)).getValue() == 1.0;
					}
				}));
			}
		}
		rp.full();
		if (logger.isEnabledFor(Level.TRACE)) {
			for (int i = 0; i < hBarLength(H_bar); i++) {
				for (int j = 0; j < PhysicalAffinity.extractVMs(pal).size(); j++) {
					logger.trace(p.column(M_COL, i, j).getKey()+"="+p.column(M_COL, i,j).getValue());
				}
			}
		}
		return rp;
	}

	private Problem createProblemFormulation(List<PhysicalAffinity> pal) {
		logger.info("Creating VMRP Formulation...");
		Problem p = new Problem(PROB_NAME);
		
		logger.info("Creating objective function");
		Tuple<List<Host>,List<Host>> H_bar = CollectionUtils.partition(PhysicalAffinity.extractHosts(pal), new Predicate<Host>(){
			@Override
			public boolean accept(Host h) {
				return h.active();
			}
		});
		
		List<VM> V_bar = PhysicalAffinity.extractVMs(pal);
		
		defineColumns(p, H_bar, V_bar);
		defineObjective(p, H_bar,V_bar,pal);

		logger.info("Defining constraints");
		defineVmAssignmentCons(p, H_bar, V_bar);
		defineAssignVmToMarkedHostsCons(p, H_bar, V_bar);
		defineCapacityCons(p,H_bar,V_bar,pal);
		defineSingleActivationCons(p,H_bar);
		return p;
	}

	private void defineSingleActivationCons(Problem p,
			Tuple<List<Host>, List<Host>> h_bar) {
		for (int i = h_bar._1.size(); i < hBarLength(h_bar); i += 2) {
			p.row("C4", i).add(1, yNameByIndex(h_bar,i),i).add(1, yNameByIndex(h_bar,i+1),i+1).bounds(null, 1.); // Y[i] + Y^off[i] <= 1
		}
	}

	private void defineCapacityCons(Problem p,
			Tuple<List<Host>, List<Host>> h_bar, List<VM> v_bar,
			List<PhysicalAffinity> pal) {
		for (PhysicalAffinity pa: pal) {
			for (int i = 0; i < hBarLength(h_bar); i++) {
				p.row("C3", i).bounds(null, 0.).add(-1, yNameByIndex(h_bar, i),i); // -1*Y[i] <= 0
				List<VM> vms = pa.vms();
				for (VM vm: vms) {
					p.row("C3", i).add(relativeSize(vm, hostByIndex(h_bar,i)), M_COL, i, vms.indexOf(vm)); // +P[i][j]*M[i][j]
				}
			}
		}
	}

	private Host hostByIndex(Tuple<List<Host>, List<Host>> h_bar, int i) {
		if (i < h_bar._1.size())
			return h_bar._1.get(i);
		return h_bar._2.get((i - h_bar._1.size()) / 2);
	}

	private void defineAssignVmToMarkedHostsCons(Problem p,
			Tuple<List<Host>, List<Host>> h_bar, List<VM> v_bar) {
		for (int i = 0; i < hBarLength(h_bar); i++) {
			for (int j = 0; j < v_bar.size(); j++) {
				p.row("C2", i, j).bounds(null, 0.).add(1, M_COL, i, j).add(-1, yNameByIndex(h_bar,i),i).bounds(null, 0.);
			}
		}
	}

	private String yNameByIndex(Tuple<List<Host>, List<Host>> h_bar, int i) {
		return (i >= h_bar._1.size() && i % 2 == 1) ? Y_OFF_COL : Y_ON_COL;
	}

	private void defineVmAssignmentCons(Problem p,
			Tuple<List<Host>, List<Host>> h_bar, List<VM> v_bar) {
		for (int j = 0; j < v_bar.size(); j++) { 
			p.row(ASSIGNMENT_CONSTRAINT, j).bounds(1.,1.); // = 1
			for (int i = 0; i < hBarLength(h_bar); i++) {
				p.row(ASSIGNMENT_CONSTRAINT, j).add(1., M_COL, i, j); // +1*M[i][j]
			}
		}
	}

	private Problem defineObjective(Problem p, Tuple<List<Host>, List<Host>> H_bar, 
			List<VM> V_bar, List<PhysicalAffinity> pal) {
		p.objective(OBJECTIVE, Problem.Direction.MINIMIZE);
		
		for (Host backupHost : H_bar._2) {
			p.objective().add(backupHost.activationCost(),Y_ON_COL, hostIndex(H_bar,backupHost,true));
		}
		
		for (VM vm: V_bar) {
			int j = V_bar.indexOf(vm);
			for (Host placementHost : H_bar._1) {
				p.objective().add(
						costV(pal, vm, placementHost), 
						M_COL, hostIndex(H_bar, placementHost, true), j);
			}

			for (Host backupHost : H_bar._2) {
				backupHost.activate();
				p.objective().add(costV(pal, vm, backupHost), 
						M_COL, hostIndex(H_bar, backupHost, true), j);
				backupHost.deactivate();
				p.objective().add(costV(pal, vm, backupHost), 
						M_COL, hostIndex(H_bar, backupHost, false), j);
			}
		}
		
		return p;
	}
	
	private double costV(List<PhysicalAffinity> pal, VM vm, Host placementHost) {
		return coResident(pal, placementHost, vm) ? VERY_LARG_COST : vm.cost(placementHost);
	}
	
	private int hostIndex(Tuple<List<Host>,List<Host>> H_bar, Host h, boolean on) {
		if (H_bar._1.contains(h)) {
			return H_bar._1.indexOf(h);
		}
		return H_bar._1.size() + H_bar._2.indexOf(h)*2 + ((on ? H_bar._1.size() : H_bar._1.size() + 1) % 2);
	}
	
	private Problem defineColumns(Problem p, Tuple<List<Host>, List<Host>> H_bar, 
			List<VM> V_bar) {
		
		for (int i = 0; i < hBarLength(H_bar); i++) {
			p.column(yNameByIndex(H_bar,i),i).type(Problem.ColumnType.BINARY);
			for (int j = 0; j < V_bar.size(); j++) {
				p.column(M_COL, i, j).type(Problem.ColumnType.BINARY);
			}
		}
		return p;
	}
	
	private int hBarLength(Tuple<List<Host>, List<Host>> H_bar) {
		return H_bar._1.size() + H_bar._2.size()*2;
	}
	
	private boolean coResident(final List<PhysicalAffinity> pal, final Host h, final VM vm) {
		return CollectionUtils.find(pal, new Predicate<PhysicalAffinity>(){
			@Override
			public boolean accept(PhysicalAffinity pa) {
				return pa.hosts().contains(h) && pa.vms().contains(vm);
			}});
	}
	
	private double relativeSize(VM vm, Host host) {
		if (vm.size() > host.freeCapacity()) 
			return 2.0; // Too big to be included in any solution.
		return (double)vm.size() / host.freeCapacity();
	}
}
