/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.data;

import il.ac.technion.config.TestConfiguration;
import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.guice.Sequencer;
import il.ac.technion.datacenter.vm.guice.VmType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.Period;

public class DataCoverterImpl extends DataConverter {

	private static final String CSV_DELIMITER = ",";
	
	@Override
	public Placement convert(TestConfiguration tConfig) throws IOException, DataException {
		Scanner scanner = new Scanner(tConfig.getDataFile());
		return extractPlacement(tConfig, scanner);
	}
	
	private Placement extractPlacement(TestConfiguration tConfig, Scanner scanner) throws DataException {
		Sequencer.INSTANCE.reset();
		
		int hostID = 0;
		List<Host> hosts = new LinkedList<Host>();
		List<VM> vms = new LinkedList<VM>();
		scanner.nextLine();
		while(scanner.hasNext()) {
			String[] split_data = scanner.nextLine().split(CSV_DELIMITER);
			if (split_data.length == 0) {
				continue;
			}
			if (split_data.length != 4) {
				throw new DataException("Invalid CSV format - num columns is " + split_data.length 
						+ " instead of 4");
			}
			int[] parsedData = getInts(split_data);
			validateParams(parsedData);
			int hostCapacity = parsedData[0];
			int numSmall = parsedData[1];
			int numMedium = parsedData[2];
			int numLarge = parsedData[3];
			
			double activationCost = 0.0;
			if (numSmall + numMedium + numLarge == 0) {
				activationCost = tConfig.getHostActivationCost();
			} 
			
			Period bootTime = tConfig.getHostBootTime();
			
			Host host = new Host(hostID++, hostCapacity, activationCost, bootTime);
			
			if (activationCost == 0.0) {
				host.activate();
			} else {
				host.deactivate();
			}
			hosts.add(host);
			
			List<VM> newVms = createVMs(numSmall,VmType.SMALL);
			newVms.addAll(createVMs(numMedium,VmType.MEDIUM));
			newVms.addAll(createVMs(numLarge,VmType.LARGE));
			
			for (VM vm : newVms) {
				host.assign(vm);
				vm.addBootTime(host, tConfig.getVmBootTime());
			}
			vms.addAll(newVms);
		}

		scanner.close();
		
		/* Backup hosts */
		for (Host bHost : tConfig.getBackupHosts()) {
			Host bHostClone = bHost.clone(hostID++);
			for (VM vm : vms) {
				vm.addBootTime(bHostClone, tConfig.getVmBootTime());
			}
			hosts.add(bHostClone);
		}
		
		return new Placement(new ArrayList<Host>(hosts), new ArrayList<VM>(vms));
	}

	private List<VM> createVMs(int num, VmType type) {
		List<VM> $ = new ArrayList<VM>(num);
		for (int i = 0; i < num; i++) {
			$.add(type.createVm());
		}
		return $;
	}

	private void validateParams(int[] parsedData) throws DataException {
		if (parsedData[0] <= 0) {
			throw new DataException("Invalid host capacity value [" + parsedData[0] + "]");
		}
		if (parsedData[1] < 0 || parsedData[2] < 0 || parsedData[3] < 0) {
			throw new DataException("Invalid VM count");
		}
	}

	private int[] getInts(String[] split_data) {
		int[] ints = new int[split_data.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.parseInt(split_data[i]);
		}
		return ints;
	}
}
