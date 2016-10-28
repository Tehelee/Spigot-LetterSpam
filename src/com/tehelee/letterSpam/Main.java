package com.tehelee.letterSpam;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	public static Server server;
	public static PluginManager pluginManager;
	public static ConsoleCommandSender console;
	
	public static Main instance;
	
	public Main()
	{
		Main.instance = this;
	}
	
	@Override
	public void onEnable()
	{
		Main.server = getServer();
		
		Main.pluginManager = server.getPluginManager();
		
		Main.console = Main.server.getConsoleSender();
		
		Main.pluginManager.registerEvents(new LetterSpamListener(), this);
		
		message(null, HelpText.logStart, true);
	}
	
	@Override
	public void onDisable()
	{
		message(null, HelpText.logStop, true);
	}
	
	public static void message(Player player, String message)
	{
		message((CommandSender)player, message);
	}
	
	public static void message(CommandSender sender, String message)
	{
		message(sender, message, false);
	}
	
	public static void message(CommandSender sender, String message, boolean prefix)
	{
		String fancyMessage;
		
		if (prefix)
			fancyMessage = HelpText.PluginName + message;
		else
			fancyMessage = ChatColor.WHITE + message;
			
		
		if ((null != sender) && (sender instanceof Player))
		{
			sender.sendMessage(fancyMessage);
		}
		else
		{
			Main.console.sendMessage(fancyMessage);
		}
	}	
}
