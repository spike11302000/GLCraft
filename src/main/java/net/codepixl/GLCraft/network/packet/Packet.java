package net.codepixl.GLCraft.network.packet;

import net.codepixl.GLCraft.network.Compressor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

public class Packet implements Serializable{
	public byte[] getBytes() throws IOException{
		return Compressor.compress(SerializationUtils.serialize(this));
	}
}
