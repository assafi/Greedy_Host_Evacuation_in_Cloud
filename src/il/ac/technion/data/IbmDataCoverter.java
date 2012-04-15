/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 08/04/2012
 */
package il.ac.technion.data;

import il.ac.technion.datacenter.physical.Host;
import il.ac.technion.datacenter.physical.Placement;
import il.ac.technion.datacenter.vm.VM;
import il.ac.technion.datacenter.vm.guice.Sequencer;
import il.ac.technion.datacenter.vm.guice.VmModule;
import il.ac.technion.datacenter.vm.guice.VmType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.Period;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class IbmDataCoverter implements DataConverter {

	private static final Period DEFAULT_HOST_BOOT_TIME = Period.seconds(180);
	private static final Period DEFAULT_VM_BOOT_TIME = Period.seconds(240);
	private static final String CSV_DELIMITER = ",";
	private static final double DEFAULT_ACTIVATION_COST = 150.0;

	@Override
	public Placement convert(File csvFile) throws IOException, DataException {
		Scanner scanner = new Scanner(csvFile);
		return extractPlacement(scanner);
	}
	
	@Override
	public Placement convert(InputStream csvIn) throws IOException, DataException {
		Scanner scanner = new Scanner(csvIn);
		return extractPlacement(scanner);
	}

	private Placement extractPlacement(Scanner scanner) throws DataException {
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
				activationCost = DEFAULT_ACTIVATION_COST;
			} 
			
			Period bootTime = DEFAULT_HOST_BOOT_TIME;
			
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
				vm.addBootTime(host, DEFAULT_VM_BOOT_TIME);
			}
			vms.addAll(newVms);
		}
		
		scanner.close();
		return new Placement(new ArrayList<Host>(hosts), new ArrayList<VM>(vms));
	}

	private List<VM> createVMs(int num, VmType type) {
		Injector inj = Guice.createInjector(new VmModule(type));
		List<VM> $ = new ArrayList<VM>(num);
		for (int i = 0; i < num; i++) {
			$.add(inj.getInstance(VM.class));
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
