package me.async.reports.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class ReportManagerDB implements IReportManager {

	private Connection connection = null;
	
	private final String host;
	private final int port;
	private final String database;
	private final String table;
	private final String username;
	private final String password;
	
	public ReportManagerDB(String host, int port, String database, String table, String username, String password)
	{
		this.host = host;
		this.port = port;
		this.database = database;
		this.table = table;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Report ReportPlayer(UUID owner, UUID creator, String reason) {
		try {
			PreparedStatement ps = this.connection.prepareStatement("INSERT INTO `" + this.table + "`(`Owner`, `Creator`, `Reason`) VALUES (?,?,?)");
			ps.setString(1, owner.toString());
			ps.setString(2, creator.toString());
			ps.setString(3, reason);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean RemoveReport(long id) {
		
		return false;
	}

	@Override
	public boolean RemoveReport(Report report) {
		
		return false;
	}
	
	@Override
	public List<Report> getReports(UUID player) {
		List<Report> result = new ArrayList<Report>();
		try {
			PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM `" + this.table + "` WHERE `Owner`=?");
			ps.setString(1, player.toString());
			ResultSet resultset = ps.executeQuery();
			while(resultset.next())
			{
				result.add(ReportFromRow(resultset));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void Load() {
		try {
			if(this.connection == null || this.connection.isClosed())
			{
				Class.forName("com.mysql.jdbc.Driver");
				this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				Bukkit.getLogger().log(Level.INFO, "Connected to database");
			}
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't access database");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't find com.mysql.jdbc.Driver");
			e.printStackTrace();
		}
	}

	@Override
	public void Save() {
		
	}
	
	private static Report ReportFromRow(ResultSet resultset) throws SQLException
	{
		long id = resultset.getLong(1);
		UUID owner = UUID.fromString(resultset.getString(2));
		UUID creator = UUID.fromString(resultset.getString(3));
		long created = resultset.getTimestamp(4).getTime();
		String reason = resultset.getString(5);
		
		return new Report(id, owner, creator, created, reason);
	}
}
