package me.async.reports.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Report")
public class Report implements ConfigurationSerializable {

	public final long id;
	public final UUID owner;
	public final UUID creator;
	public final long created;
	public final String reason;
	
	protected Report(long id, UUID owner, UUID creator, String reason) {
		this(id, owner, creator, System.currentTimeMillis(), reason);
	}
	
	protected Report(long id, UUID owner, UUID creator, long time, String reason) {
		this.id = id;
		this.owner = owner;
		this.creator = creator;
		this.created = time;
		this.reason = reason;
	}
	
	private Report(Map<String, Object> args) throws IOException
	{		
		if(args.containsKey("id")) {
			this.id = (long) args.get("id");
		}
		else
		{
			throw new IOException("Report invalid");
		}
		if(args.containsKey("owner")) {
			this.owner = UUID.fromString((String)args.get("owner"));
        }
		else
		{
			throw new IOException("Report invalid");
		}
		if(args.containsKey("creator")) {
			this.creator = UUID.fromString((String) args.get("creator"));
        }
		else
		{
			throw new IOException("Report invalid");
		}
		if(args.containsKey("created")) {
			this.created = (long) args.get("created");
        }
		else
		{
			throw new IOException("Report invalid");
		}
		if(args.containsKey("reason")) {
			this.reason = (String) args.get("reason");
        }
		else
		{
			throw new IOException("Report invalid");
		}
	}
	
	public long getID()
	{
		return this.id;
	}

	public UUID getOwner()
	{
		return this.owner;
	}
	
	public UUID getCreator()
	{
		return this.creator;
	}
	
	public long getCreated()
	{
		return this.created;
	}
	
	public String getReason()
	{
		return this.reason;
	}
	
	public String getMessage()
	{
		Date date = new Date(this.created);
		DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		
		return "Reported by " + Bukkit.getOfflinePlayer(this.creator).getName() 
				+ " @" + df.format(date)
				+ " for " + this.reason;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Report)
		{
			return this.id == ((Report)obj).getID();
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Long.hashCode(this.id);
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("id", this.id);
		result.put("owner", this.owner.toString());
		result.put("creator", this.creator.toString());
        result.put("created", this.created);
        result.put("reason", this.reason);
        return result;
	}
	
	public static Report deserialize(Map<String, Object> args) throws IOException
	{
		return new Report(args);
	}
}
