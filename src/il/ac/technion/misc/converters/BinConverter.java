/**
 * Greedy_Recovery - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2012
 * Created: 17/03/2012
 */
package il.ac.technion.misc.converters;

import il.ac.technion.misc.Bin;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BinConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Bin.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Bin bin = (Bin)source;
		writer.startNode("id");
		writer.setValue(String.valueOf(bin.id));
		writer.endNode();
		writer.startNode("capacity");
		writer.setValue(String.valueOf(bin.capacity));
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		int id = -1, capacity = -1;

		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("id".equals(reader.getNodeName()))
				id = Integer.valueOf(reader.getValue());
			else if ("capacity".equals(reader.getNodeName())) 
				capacity = Integer.valueOf(reader.getValue());
			else
				throw new ConversionException("Invalid node encountered when trying to unmarshal Bin");
			reader.moveUp();
		}
		
		if (id == -1 || capacity == -1) 
			throw new ConversionException("Missing details when trying to unmarshal Bin");
		
		return new Bin(id,capacity);
	}

}
