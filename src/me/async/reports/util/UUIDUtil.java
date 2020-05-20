package me.async.reports.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {
	public static UUID asUUID(byte[] bytes) {
	    ByteBuffer bb = ByteBuffer.wrap(bytes);
	    long firstLong = bb.getLong();
	    long secondLong = bb.getLong();
	    return new UUID(firstLong, secondLong);
	}

	public static byte[] asBytes(UUID uuid) {
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    return bb.array();
	}
	
	public static UUID ReadFromStream(DataInputStream is) throws IOException {
	    long firstLong = is.readLong();
	    long secondLong = is.readLong();
	    return new UUID(firstLong, secondLong);
	}
	
	public static void WriteToStream(DataOutputStream os, UUID uuid) throws IOException {
	    os.writeLong(uuid.getMostSignificantBits());
	    os.writeLong(uuid.getLeastSignificantBits());
	}
}