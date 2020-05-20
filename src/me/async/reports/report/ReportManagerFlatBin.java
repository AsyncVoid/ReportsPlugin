package me.async.reports.report;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.async.reports.Main;
import me.async.reports.util.UUIDUtil;

public class ReportManagerFlatBin extends ReportManagerFlat {
	
	private final File reportFile = new File(Main.INSTANCE.getDataFolder() + "\\reports.bin");
	
	@Override
	public void Load() {
		try {
			FileInputStream fis = new FileInputStream(reportFile);
			DataInputStream dis = new DataInputStream(fis);
			while(dis.available() > 1)
			{
				Report report = ReadFromStream(dis);
				AddReport(report);
			}
			dis.close();
			fis.close();
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't load report records from file " + reportFile.getAbsolutePath());
			e.printStackTrace();
		}
	}

	@Override
	public void Save() {
		try {
			if(!reportFile.exists())
			{
				reportFile.createNewFile();
			}		
			FileOutputStream fos = new FileOutputStream(reportFile);
			DataOutputStream dos = new DataOutputStream(fos);
			for(Report report : IDToReport.values())
			{
				WriteToStream(dos, report);
			}
			dos.close();
			fos.close();
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't save report records to file " + reportFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	private static Report ReadFromStream(DataInputStream is) throws IOException
	{
		long id = is.readLong();
		UUID owner = UUIDUtil.ReadFromStream(is);
		UUID creator = UUIDUtil.ReadFromStream(is);
		long time = is.readLong();
		int responselen = is.readInt();
		byte[] responseb = new byte[responselen];
		is.read(responseb);
		String reason = new String(responseb);
		return new Report(id, owner, creator, time, reason);
	}
	
	private static void WriteToStream(DataOutputStream os, Report report) throws IOException
	{
		os.writeLong(report.id);
		UUIDUtil.WriteToStream(os, report.owner);
		UUIDUtil.WriteToStream(os, report.creator);
		os.writeLong(report.created);
		byte[] reasonb = report.reason.getBytes();
		os.writeInt(reasonb.length);
		os.write(reasonb);
	}
}
