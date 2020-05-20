package me.async.reports.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.async.reports.Main;

public class ReportManagerFlatYAML extends ReportManagerFlat {
	
	private final File reportFile = new File(Main.INSTANCE.getDataFolder() + "\\reports.yml");
	private FileConfiguration config;
	
	@Override
	public void Load() {
		config = YamlConfiguration.loadConfiguration(reportFile);
		@SuppressWarnings("unchecked")
		List<Report> reports = (List<Report>) config.get("reports");
		for(Report report : reports)
		{
			AddReport(report);
		}
	}

	@Override
	public void Save() {
		config.set("reports", new ArrayList<Report>(IDToReport.values()));
		try {
			if(!reportFile.exists())
			{
				reportFile.createNewFile();
			}
			config.save(reportFile);
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't save report records to file " + reportFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
}
