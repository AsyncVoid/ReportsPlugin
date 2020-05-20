package me.async.reports.report;

import java.util.List;
import java.util.UUID;

public interface IReportManager {
	public Report ReportPlayer(UUID owner, UUID creator, String reason);
	public boolean RemoveReport(long id);
	public boolean RemoveReport(Report report);
	public List<Report> getReports(UUID player);
	public void Load();
	public void Save();
}
