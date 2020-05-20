package me.async.reports;

import java.io.File;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import me.async.reports.cmd.ReportCommand;
import me.async.reports.report.IReportManager;
import me.async.reports.report.Report;
import me.async.reports.report.ReportManagerDB;
import me.async.reports.report.ReportManagerFlatBin;
import me.async.reports.report.ReportManagerFlatYAML;

public class Main extends JavaPlugin {

	public static Main INSTANCE;
	public static IReportManager reportManager;
	
	static
	{
		ConfigurationSerialization.registerClass(Report.class);
	}
	
	public Main() {
		INSTANCE = this;
	}

	public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
		INSTANCE = this;
	}
	
	@Override
	public void onEnable()
	{
		//saveDefaultConfig();
		getConfig().options().copyDefaults(true);
        
		getCommand("report").setExecutor(new ReportCommand());
		
		if(getConfig().getString("type").equalsIgnoreCase("DATABASE"))
		{
			String host = getConfig().getString("host");
			int port = getConfig().getInt("port");
			String database = getConfig().getString("database");
			String table = getConfig().getString("table");
			String username = getConfig().getString("username");
			String password = getConfig().getString("password");
			reportManager = new ReportManagerDB(host, port, database, table, username, password);
		}
		else if (getConfig().getString("type").equalsIgnoreCase("FLATBIN"))
		{
			reportManager = new ReportManagerFlatBin();
		}
		else
		{
			reportManager = new ReportManagerFlatYAML();
		}
		
		reportManager.Load();
	}
	
	@Override
	public void onDisable()
	{
		reportManager.Save();
		saveConfig();
	}
	
	public static IReportManager getReportManager()
	{
		return reportManager;
	}
}
