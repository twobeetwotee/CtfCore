package io.github.elliottleow;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;

import java.io.FileWriter;
import java.util.ArrayList;
//import java.io.FileWriter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
//import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
//import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


import me.nate22233.mcteams.Team;
import me.nate22233.mcteams.TeamPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;


public class Main extends JavaPlugin implements Listener {
	
	/* things to add in config
	 * set x boundary, currently x=0
	 * set spawns
	 */

	
	boolean preGameActive = false; 
	boolean endGameActive = false; 
	World main = Bukkit.getServer().getWorld("world");
	Location red1 = new Location(main, 420690, 100, 420690);
	Location red2 = new Location(main, 420690, 100, 420690);
	Location red3 = new Location(main, 420690, 100, 420690);
	Location blue1 = new Location(main, 420690, 100, 420690);
	Location blue2 = new Location(main, 420690, 100, 420690);
	Location blue3 = new Location(main, 420690, 100, 420690);
	boolean redTaken1 = false;
	boolean redTaken2 = false;
	boolean redTaken3 = false;
	boolean blueTaken1 = false;
	boolean blueTaken2 = false;
	boolean blueTaken3 = false;
	boolean redConfirm = false;
	boolean blueConfirm = false;
	
	
	FileWriter writer;
	
	
	Scoreboard score;
	Objective objective;
	Score status;
	Score scoreRed;
	Score scoreBlue;
	
	ItemStack redFlag = new ItemStack(Material.BANNER, 1);
	BannerMeta redBanner = (BannerMeta) redFlag.getItemMeta();
	
	ItemStack blueFlag = new ItemStack(Material.BANNER, 1);
	BannerMeta blueBanner = (BannerMeta) blueFlag.getItemMeta();
	
	@Override
	public void onEnable() {
		PluginManager manager = getServer().getPluginManager();
	    manager.registerEvents(this, this);
	    score = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
	    objective = score.registerNewObjective("CTF", "1");
		status = objective.getScore(ChatColor.GREEN + "Mins to pregame end: ");
		scoreRed = objective.getScore(ChatColor.RED + "Red Team Score: ");
		scoreBlue = objective.getScore(ChatColor.BLUE + "Blue Team Score: ");
		
		redBanner.setDisplayName(ChatColor.RED + "Red's Flag");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Red's Flag, bring this back to your base.");
		redBanner.setLore(lore);
		redBanner.addEnchant(Enchantment.DURABILITY, 20, true);
		redBanner.setBaseColor(DyeColor.RED);
		redFlag.setItemMeta(redBanner);
		
		blueBanner.setDisplayName(ChatColor.BLUE + "Blue's Flag");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.WHITE + "Blue's Flag, bring this back to your base.");
		blueBanner.setLore(lore1);
		blueBanner.addEnchant(Enchantment.DURABILITY, 20, true);
		blueBanner.setBaseColor(DyeColor.RED);
		blueFlag.setItemMeta(blueBanner);
		
	}
	
	@Override
	public void onDisable() {
		//getLogger().info("CTF CORE is OFF");
	}
	
	@EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) 
    {
		Player p = e.getEntity();
		TeamPlayer tp = new TeamPlayer(p.getName());
		Team teamName = tp.getTeam();
		if (Objects.equals(teamName.getName(), "Red")) {
			Location spawn = new Location(p.getWorld(), 999, p.getWorld().getHighestBlockAt(999, 0).getY(), 0);
			Chunk chunk = p.getWorld().getChunkAt(spawn);
			chunk.load(true);
			p.getServer().getWorld("world").setSpawnLocation(999, p.getWorld().getHighestBlockAt(999, 0).getY(), 0);
		}
		if (Objects.equals(teamName.getName(), "Blue")) {
			Location spawn = new Location(p.getWorld(), -999, p.getWorld().getHighestBlockAt(-999, 0).getY(), 0);
			Chunk chunk = p.getWorld().getChunkAt(spawn);

			chunk.load(true);
			p.getServer().getWorld("world").setSpawnLocation(-999, p.getWorld().getHighestBlockAt(-999, 0).getY(), 0);
		}
    }
	
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
		if (preGameActive) {
			Player p = e.getPlayer();
			int x = p.getLocation().getBlockX();
			
			//if (x > 100 || x < -100) return;
			//getLogger().info(p.getName());
			//getLogger().info(Integer.toString(x));
			TeamPlayer tp = new TeamPlayer(p.getName());
			Team teamName = tp.getTeam();
			
			
	
			if ((Objects.equals(teamName.getName(), "Red")) && x <= 0) {
				Location t = e.getFrom();
				t.setX(1.1);
				t.setY(p.getWorld().getHighestBlockAt(1, p.getLocation().getBlockZ()).getY());
				//getLogger().info(Integer.toString(x) + " Red");
				e.setCancelled(true);
				p.teleport(e.getFrom());
				return;
			}
			
			if ((Objects.equals(teamName.getName(), "Blue")) && x > 0) {
				
				Location t = e.getFrom();
				t.setX(0.9);
				t.setY(p.getWorld().getHighestBlockAt(0, p.getLocation().getBlockZ()).getY());
				e.setCancelled(true);
				p.teleport(e.getFrom());
				return;
			}
			return;
		}
		TeamPlayer tp = new TeamPlayer(e.getPlayer().getName());
		Team teamName = tp.getTeam();
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), red1.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), red1.getBlockZ())) && redTaken1 == false) {	
			if (Objects.equals(teamName.getName(), "Blue")) {
				e.getPlayer().getInventory().addItem(redFlag);
				redTaken1=true;
			}
		}
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), red2.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), red2.getBlockZ())) && redTaken2 == false) {	
			if (Objects.equals(teamName.getName(), "Blue")) {
				e.getPlayer().getInventory().addItem(redFlag);
				redTaken2=true;
			}
		}
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), red3.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), red3.getBlockZ())) && redTaken3 == false) {	
			if (Objects.equals(teamName.getName(), "Blue")) {
				e.getPlayer().getInventory().addItem(redFlag);
				redTaken3=true;
			}
		}
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), blue1.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), blue1.getBlockX())) && blueTaken1 == false) {	
			if (Objects.equals(teamName.getName(), "Red")) {
				e.getPlayer().getInventory().addItem(blueFlag);
				blueTaken1=true;
			}
		}
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), blue2.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), blue2.getBlockZ())) && blueTaken2 == false) {	
			if (Objects.equals(teamName.getName(), "Blue")) {
				e.getPlayer().getInventory().addItem(blueFlag);
				blueTaken2=true;
			}
		}
		if ((Objects.equals(e.getPlayer().getLocation().getBlockX(), blue3.getBlockX())) && (Objects.equals(e.getPlayer().getLocation().getBlockZ(), blue3.getBlockZ())) && blueTaken3 == false) {	
			if (Objects.equals(teamName.getName(), "Blue")) {
				e.getPlayer().getInventory().addItem(blueFlag);
				blueTaken3=true;
			}
		}
		return;
		
    }
	
	
	String notif = "0";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("You are not a player"); 
			return true;
		}
		Timer PreGameTimer = new Timer();
		
		
		Player p = (Player) sender; 
	    TeamPlayer tp = new TeamPlayer(p.getName());
		Team t = tp.getTeam();
		
		Timer timer = new Timer();
		
		if (cmd.getName().equalsIgnoreCase("startgame")) {
			PreGameTimer.cancel();
			PreGameTimer.purge();
			preGameActive = true;
			//PLACE FLAGS
			PreGameTimer.schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	preGameActive = false;
			            	startEndGame();
			            }
			        }, 3600000);
			
			
			
			TimerTask task = new TimerTask()
			{
			    int minutes = 60;
			    int i = 0;
			    @Override
			    public void run()
			    {
			       i++;

			       if(i % minutes == 0) {
			    	   timer.cancel();
						timer.purge();
			       }
			       else {
			           notif = ("" + (minutes - (i %minutes)) );
			           getLogger().info(notif);
			           updateScoreboard();
			       }
			       
			    }
			};
			timer.schedule(task, 0, 60000);

		}
		
		if (cmd.getName().equalsIgnoreCase("end")) {
			if (args.length != 1) return true;
			if (args[1].equalsIgnoreCase("pregame")) {
				PreGameTimer.cancel();
				PreGameTimer.purge();
				timer.cancel();
				timer.purge();
				preGameActive = false;
				endGameActive = true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("getteam")) {
			System.out.println(t + " " + t.getName());
			sender.sendMessage(t.getName());
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tests")) {
			if (Objects.equals(args[0], "redflag")) {

				p.getInventory().addItem(redFlag);
				p.sendMessage("redflag given");
			}
			Timer timertest = new Timer();
			TimerTask task = new TimerTask()
			{
			    int seconds = 5;
			    int i = 0;
			    @Override
			    public void run()
			    {
			       i++;

			       if(i % seconds == 0) {
			    	   notif = "0";
			    	   updateScoreboard();
			    	   timertest.cancel();
						timertest.purge();
						startEndGame();
			       }
			       else {
			           notif = ("" + (seconds - (i %seconds)) );
			           getLogger().info(notif);
			           updateScoreboard();
			       }
			       
			    }
			};
			
			timertest.schedule(task, 0, 1000);
			preGameActive = true;
			setScoreboard(p);
			return true;
			
			
		}
	
		
		if (cmd.getName().equalsIgnoreCase("setflag")) {  
			
			if (Objects.equals(args[0] , "help")) {
				p.sendMessage("Enter coordinates, usage: /setflag x z <flag to set>");
				p.sendMessage("you have 3 flags in the game, so enter 1 2 or 3 for <flag to set>");
				p.sendMessage("Example: /setflag 300 300 2");
				p.sendMessage("This sets a flag 2 to be at (300, 300)");
			}
			
			if (args.length != 3) {
				p.sendMessage("Enter coordinates, usage: /setflag x z <flag to set>");
				p.sendMessage("you have 3 flags in the game, so enter 1 2 or 3 for <flag to set>");
				p.sendMessage("for more help type /setflag help");
				return true;
				
			}
			
			if ((Objects.equals(t.getName(), "Red") && redConfirm == true) || (Objects.equals(t.getName(), "Blue") && blueConfirm == true)) {
				p.sendMessage(ChatColor.RED + "You have already confirmed flag placement, you cannot change them.");
				return true;
			}
			if (Objects.equals(t.getName(), "Red") && Integer.parseInt(args[0]) <=0) {
				p.sendMessage(ChatColor.DARK_RED + "Please place the flag in your boundry (x > 0)");
				return true;
			}
			if (Objects.equals(t.getName(), "Blue") && Integer.parseInt(args[0]) >= 0) {
				p.sendMessage(ChatColor.DARK_RED + "Please place the flag in your boundry (x < 0)");
				return true;
			}
			if (Integer.parseInt(args[1]) > 2000 || Integer.parseInt(args[1]) < -2000) {
				p.sendMessage(ChatColor.DARK_RED + "Please place the flag in the boundry (z < 2000, z > -2000)");
				return true;
			}
			
			
			
			
			
			if (Objects.equals(t.getName(), "Red")) {
				if (Integer.parseInt(args[2]) == 1) {
					red1.setX(Integer.parseInt(args[0]));
					red1.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					red1.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 1 at: (" + red1.getBlockX() + ", " + red1.getBlockZ() + ")");
				} else if (Integer.parseInt(args[2]) == 2) {
					red2.setX(Integer.parseInt(args[0]));
					red2.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					red2.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 2 at: (" + red2.getBlockX() + ", " + red2.getBlockZ() + ")");
				} else if (Integer.parseInt(args[2]) == 3) {
					red3.setX(Integer.parseInt(args[0]));
					red3.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					red3.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 3 at: (" + red3.getBlockX() + ", " + red3.getBlockZ() + ")");
				} else {
					p.sendMessage(ChatColor.RED + "Set flag 1, 2, or 3. To see where your current flags are placed, type /seeflags.");
				}
			}
			
			if (Objects.equals(t.getName(), "Blue")) {
				if (Integer.parseInt(args[2]) == 1) {
					blue1.setX(Integer.parseInt(args[0]));
					blue1.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					blue1.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 1 at: (" + blue1.getBlockX() + ", " + blue1.getBlockZ() + ")");
				} else if (Integer.parseInt(args[2]) == 2) {
					blue2.setX(Integer.parseInt(args[0]));
					blue2.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					blue2.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 2 at: (" + blue2.getBlockX() + ", " + blue2.getBlockZ() + ")");
				} else if (Integer.parseInt(args[2]) == 3) {
					blue3.setX(Integer.parseInt(args[0]));
					blue3.setY(p.getWorld().getHighestBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1])).getY());
					blue3.setZ(Integer.parseInt(args[1]));
					p.sendMessage(ChatColor.GREEN + "You have set flag 3 at: (" + blue3.getBlockX() + ", " + blue3.getBlockZ() + ")");
				} else {
					p.sendMessage(ChatColor.RED + "Set flag 1, 2, or 3. To see where your current flags are placed, type /seeflags.");
				}
			}
			
			return true;
			
		} 
		
		if (cmd.getName().equalsIgnoreCase("seeflags")) {
			if (Objects.equals(t.getName(), "Blue")) {
				if (blue1.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 1: " + Integer.toString(blue1.getBlockX()) + ", " + Integer.toString(blue1.getBlockY()) + ", " + Integer.toString(blue1.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 1 has not been set");
				}
				if (blue2.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 2: " + Integer.toString(blue2.getBlockX()) + ", " + Integer.toString(blue2.getBlockY()) + ", " + Integer.toString(blue2.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 2 has not been set");
				}
				if (blue3.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 3: " + Integer.toString(blue3.getBlockX()) + ", " + Integer.toString(blue3.getBlockY()) + ", " + Integer.toString(blue3.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 3 has not been set");
				}
				
				return true;
				
			}
			if (Objects.equals(t.getName(), "Red")) {
				if (red1.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 1: " + Integer.toString(red1.getBlockX()) + ", " + Integer.toString(red1.getBlockY()) + ", " + Integer.toString(red1.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 1 has not been set");
				}
				if (red2.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 2: " + Integer.toString(red2.getBlockX()) + ", " + Integer.toString(red2.getBlockY()) + ", " + Integer.toString(red2.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 2 has not been set");
				}
				if (red3.getBlockX() != 420690) {
					p.sendMessage(ChatColor.GREEN + "Flag 3: " + Integer.toString(red3.getBlockX()) + ", " + Integer.toString(red3.getBlockY()) + ", " + Integer.toString(red3.getBlockZ()));
				} else {
					p.sendMessage(ChatColor.GREEN + "Flag 3 has not been set");
				}
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("confirmflags")) {
			if (Objects.equals(t.getName(), "Red")) {
				if (red1.getBlockX() == 420690 || red2.getBlockX() == 420690 || red3.getBlockX() == 420690) {
					p.sendMessage(ChatColor.RED + "You have not set all your flags yet. Use /seeflags to see which flags you have not set yet. Then use /setflag to set those flags");
					return true;
				}
				IChatBaseComponent comp = ChatSerializer.a("[\"\",{\"text\":\"[CONFIRMATION] You cannot change your flags after you confirm. Do you want to confirm? Click: \"},{\"text\":\"[YES]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ctf yes\"}},{\"text\":\" or \"},{\"text\":\"[NO]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ctf no\"}}]");
				PacketPlayOutChat chat = new PacketPlayOutChat(comp);
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
				//redConfirm = true;
						
			}
			if (Objects.equals(t.getName(), "Blue")) {
				if (blue1.getBlockX() == 420690 || blue2.getBlockX() == 420690 || blue3.getBlockX() == 420690) {
					p.sendMessage(ChatColor.RED + "You have not set all your flags yet. Use /seeflags to see which flags you have not set yet. Then use /setflag to set those flags");
					return true;
				}
				IChatBaseComponent comp = ChatSerializer.a("[\"\",{\"text\":\"[CONFIRMATION] You cannot change your flags after you confirm. Do you want to confirm? Click: \"},{\"text\":\"[YES]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ctf yes\"}},{\"text\":\" or \"},{\"text\":\"[NO]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ctf no\"}}]");
				PacketPlayOutChat chat = new PacketPlayOutChat(comp);
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
				//blueConfirm = true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("ctf")) {
			if (Objects.equals(args[0], "yes")) {
				if (Objects.equals(t.getName(), "Red")) {
					redConfirm = true;
					p.sendMessage(ChatColor.GREEN + "Confirmed flag placement.");
					Location place = new Location(p.getWorld(), red1.getBlockX(), p.getWorld().getHighestBlockAt(red1.getBlockX(), red1.getBlockZ()).getY(), red1.getBlockZ());
					Location place1 = new Location(p.getWorld(), red2.getBlockX(), p.getWorld().getHighestBlockAt(red2.getBlockX(), red2.getBlockZ()).getY(), red2.getBlockZ());
					Location place2 = new Location(p.getWorld(), red3.getBlockX(), p.getWorld().getHighestBlockAt(red3.getBlockX(), red3.getBlockZ()).getY(), red3.getBlockZ());
					Chunk chunk = p.getWorld().getChunkAt(place);
					Chunk chunk1 = p.getWorld().getChunkAt(place1);
					Chunk chunk2 = p.getWorld().getChunkAt(place2);
					chunk.load(true);
					p.getWorld().getBlockAt(place).setType(Material.BEDROCK);
					chunk1.load(true);
					p.getWorld().getBlockAt(place1).setType(Material.BEDROCK);
					chunk2.load(true);
					p.getWorld().getBlockAt(place2).setType(Material.BEDROCK);
					
					
				}
				if (Objects.equals(t.getName(), "Blue")) {
					blueConfirm = true;
					p.sendMessage(ChatColor.GREEN + "Confirmed flag placement.");
					Location place = new Location(p.getWorld(), blue1.getBlockX(), p.getWorld().getHighestBlockAt(blue1.getBlockX(), blue1.getBlockZ()).getY(), blue1.getBlockZ());
					Location place1 = new Location(p.getWorld(), blue2.getBlockX(), p.getWorld().getHighestBlockAt(blue2.getBlockX(), blue2.getBlockZ()).getY(), blue2.getBlockZ());
					Location place2 = new Location(p.getWorld(), blue3.getBlockX(), p.getWorld().getHighestBlockAt(blue3.getBlockX(), blue3.getBlockZ()).getY(), blue3.getBlockZ());
					Chunk chunk = p.getWorld().getChunkAt(place);
					Chunk chunk1 = p.getWorld().getChunkAt(place1);
					Chunk chunk2 = p.getWorld().getChunkAt(place2);
					chunk.load(true);
					p.getWorld().getBlockAt(place).setType(Material.BEDROCK);
					chunk1.load(true);
					p.getWorld().getBlockAt(place1).setType(Material.BEDROCK);
					chunk2.load(true);
					p.getWorld().getBlockAt(place2).setType(Material.BEDROCK);
				}
			}
			if (Objects.equals(args[0], "no")) {
				p.sendMessage(ChatColor.GREEN + "Did not confirm flag placement.");
			}
		}
		return true;
	
		
		
	}

	
	
	public void setScoreboard(Player p) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			objective.getScoreboard().resetScores(ChatColor.GREEN + "Pregame has ended! ");
			status = objective.getScore(ChatColor.GREEN + "Mins to pregame end: ");
			objective.setDisplayName("Capture the Flag");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			status.setScore(Integer.parseInt(notif));
			scoreRed.setScore(0);
			scoreBlue.setScore(0);
			online.setScoreboard(score);
		}
	}
	
	
	public void updateScoreboard() {
			status.setScore(Integer.parseInt(notif));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (preGameActive || endGameActive) e.getPlayer().setScoreboard(score);
		return;
	}
	public void startEndGame() {
		
		objective.getScoreboard().resetScores(ChatColor.GREEN + "Mins to pregame end: ");
		status = objective.getScore(ChatColor.GREEN + "Pregame has ended! ");
		status.setScore(-1);
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			online.setScoreboard(score);
		}
		preGameActive = false;
		endGameActive = true;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damageTaker = e.getEntity();
	
		if (damageTaker instanceof Player) {
		    Player taker = (Player) damageTaker;
		    if (damager instanceof Player) {
		        Player damagerPlayer = (Player) damager;
			    TeamPlayer tp = new TeamPlayer(taker.getName());
				Team t = tp.getTeam();
				TeamPlayer tp1 = new TeamPlayer(damagerPlayer.getName());
				Team t1 = tp1.getTeam();
		        if ((Objects.equals(t.getName(), "Red") && Objects.equals(t1.getName(), "Red")) || (Objects.equals(t.getName(), "Blue") && Objects.equals(t1.getName(), "Blue"))) {
		        	e.setCancelled(true);
		        }
		    }
		}	
	}
	
	public void GetFlag() {
		
	}
}
