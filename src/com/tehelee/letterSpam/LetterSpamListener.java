package com.tehelee.letterSpam;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class LetterSpamListener implements Listener
{
	public static int incrementWarningLog(UUID id)
	{
		if (id == null) return 0;
		
		File file = new File(Main.instance.getDataFolder(), "//" + "warningHistory.yml");
		FileConfiguration history = YamlConfiguration.loadConfiguration(file);

		String uuid = id.toString();
		
		int count = history.getInt(uuid, 0);
		
		count++;
		
		history.set(uuid, count);
		
		try
		{
			history.save(file);
		}
		catch (IOException e)
		{
			Main.message(null, "Failed to save challenge progress for " + uuid + "!", true);
		}
		
		return count;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (!p.hasPermission("permissions.letterSpam.noFilter"))
		{
			String offender = "", message = e.getMessage().replaceAll(" ", "");
			
			int count = 0;
			
			boolean hasSpammed = false;
			
			/*String postformat = "";
			
			char current, last = ' ';
			
			for (int i = 0; i < preformat.length(); i++)
			{
				current = preformat.charAt(i);
				
				if (current == last)
				{
					count++;
				}
				else
				{
					count = 0;
					last = current;
					postformat += last;
				}
				
				if (count >= 3)
				{
					hasSpammed = true;
				}
			}*/
			
			for (int len = Math.min((int)Math.floor(message.length()/4), message.length()); len > 0; len--)
			{
				for (int x = 0; x+len <= message.length(); x+= len)
				{
					for (int y = x; y+len <= message.length(); y+=len)
					{
						if (message.regionMatches(true, x, message, y, len))
						{
							count++;
							offender = message.substring(y, y+len);
						}
						else
						{
							count = 0;
						}
						
						if (count > 3)
						{
							hasSpammed = true;
							break;
						}
					}
					
					if (hasSpammed) break;
					
					count = 0;
				}
				
				if (hasSpammed) break;
			}
			
			if (hasSpammed)
			{
				int history = incrementWarningLog(p.getUniqueId());
				
				e.setCancelled(true);
				
				offender = offender.toLowerCase();
			
				String log = String.format(HelpText.Log, p.getName(), offender, Integer.toString(history));
				
				Main.message(p, p.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.DARK_GRAY + e.getMessage());
				Main.message(p, String.format(HelpText.Warning, offender), true);
				Main.message(null, ChatColor.GRAY + log, true);
				
				Collection<? extends Player> players = Main.server.getOnlinePlayers();
			
				for (Player other : players)
				{
					if (other.hasPermission("permissions.letterSpam.moderator"))
					{
						Main.message(other, ChatColor.YELLOW + log, true);
					}
				}
			}
		}
	}
}
