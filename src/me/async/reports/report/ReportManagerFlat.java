package me.async.reports.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public abstract class ReportManagerFlat implements IReportManager {
	
	protected Map<UUID, List<Long>> PlayerToReportIDs = new HashMap<UUID, List<Long>>();
	protected Map<Long, Report> IDToReport = new HashMap<Long, Report>();
	protected Random random = new Random();

	@Override
	public Report ReportPlayer(UUID owner, UUID creator, String reason)
	{
		long id = random.nextLong();
		while(IDToReport.containsKey(id))
		{
			id = random.nextLong();
		}
		
		Report report = new Report(id, owner, creator, reason);
		
		if(PlayerToReportIDs.containsKey(owner))
		{
			PlayerToReportIDs.get(owner).add(id);
		}
		else
		{
			List<Long> reports = new ArrayList<Long>();
			reports.add(id);
			PlayerToReportIDs.put(owner, reports);
		}
		IDToReport.put(id, report);
		
		return report;
	}
	
	@Override
	public boolean RemoveReport(long id)
	{
		if(!IDToReport.containsKey(id))
			return false;
		
		Report report = IDToReport.get(id);
		
		return RemoveReport(report);
	}
	
	@Override
	public boolean RemoveReport(Report report)
	{
		if(!PlayerToReportIDs.containsKey(report.getOwner()))
			return false;
		List<Long> list = PlayerToReportIDs.get(report.getOwner());
		list.remove(report.getID());
		IDToReport.remove(report.id);
		return true;
	}
	
	@Override
	public List<Report> getReports(UUID player)
	{
		List<Report> result = new ArrayList<Report>();
		if(!PlayerToReportIDs.containsKey(player))
			return result;
		List<Long> reportIDs = PlayerToReportIDs.get(player);
		for(Long id : reportIDs)
		{
			result.add(IDToReport.get(id));
		}
		return result;
	}
	
	public void AddReport(Report report)
	{
		if(PlayerToReportIDs.containsKey(report.getOwner()))
		{
			PlayerToReportIDs.get(report.getOwner()).add(report.getID());
		}
		else
		{
			List<Long> reportIDs = new ArrayList<Long>();
			reportIDs.add(report.getID());
			PlayerToReportIDs.put(report.getOwner(), reportIDs);
		}
		IDToReport.put(report.getID(), report);
	}
}
