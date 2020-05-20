package me.async.reports.cmd;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.async.reports.Main;
import me.async.reports.report.Report;

public class ReportCommand implements CommandExecutor {

	// report <player> <reason>
	// report view [player]
	// report help
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("view"))
			{
				if(sender instanceof Player)
				{
					if(sender.hasPermission("report.viewself"))
					{
						SendReports(sender, ((Player) sender).getUniqueId());
					}
					else
					{
						SendNoPermission(sender);
					}
					return true;
				}
				else
				{
					SendMustBePlayer(sender);
					return false;
				}
			}
			else if(args[0].equalsIgnoreCase("help"))
			{
				SendHelp(sender);
				return true;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("view"))
			{
				if(sender.hasPermission("report.viewothers"))
				{
					OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
					if(player != null)
					{
						SendReports(sender, player.getUniqueId());
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Couldn't find player \"" + args[1] + "\"");
					}
				}
				else
				{
					SendNoPermission(sender);
				}
				return true;
			}
		}
		if(args.length >= 2)
		{
			if(sender.hasPermission("report.make"))
			{
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if(target != null)
				{
					StringBuilder builder = new StringBuilder();
					for(int i = 1; i < args.length; i++)
					{
						builder.append(args[i] + " ");
					}
					builder.deleteCharAt(builder.length() - 1);
					String reason = builder.toString();
					
					UUID uuid = null;
					if(sender instanceof Player)
					{
						uuid = ((Player) sender).getUniqueId();
					}
					else
					{
						uuid = Bukkit.getOfflinePlayer("CONSOLE").getUniqueId();
					}
					
					//Report report = Main.getReportManager().ReportPlayer(target.getUniqueId(), uuid, reason);
					//sender.sendMessage("Player " + args[0] + " " + report.getMessage());
					Main.getReportManager().ReportPlayer(target.getUniqueId(), uuid, reason);
					sender.sendMessage(ChatColor.GREEN + "Player " + args[0] + " reported.");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Couldn't find player \"" + args[1] + "\"");
				}
			}
			else
			{
				SendNoPermission(sender);
			}
			
			return true;
		}
		return false;
	}

	public static void SendHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.GREEN + "--------------------------------");
		sender.sendMessage(ChatColor.GREEN + " /report help");
		sender.sendMessage(ChatColor.GREEN + " /report view [player]");
		sender.sendMessage(ChatColor.GREEN + " /report <player> <reason>");
		sender.sendMessage(ChatColor.GREEN + "--------------------------------");
	}
	
	public static void SendMustBePlayer(CommandSender sender)
	{
		sender.sendMessage(ChatColor.RED + "You must be a player to perform this command!");
	}
	
	public static void SendNoPermission(CommandSender sender)
	{
		sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
	}
	
	public static void SendReports(CommandSender sender, UUID uuid)
	{
		List<Report> reports = Main.getReportManager().getReports(uuid);
		if(reports.size() < 1)
		{
			sender.sendMessage(ChatColor.GOLD + "No reports found.");
			return;
		}
		sender.sendMessage(ChatColor.GREEN + "--------------------------------");
		sender.sendMessage(ChatColor.GREEN + "Reports for user " + Bukkit.getOfflinePlayer(uuid).getName());
		for(Report report : reports)
		{
			sender.sendMessage(ChatColor.GOLD + report.getMessage());
		}
		sender.sendMessage(ChatColor.GREEN + "--------------------------------");
	}
}
