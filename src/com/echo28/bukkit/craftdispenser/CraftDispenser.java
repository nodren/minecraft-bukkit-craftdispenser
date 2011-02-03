package com.echo28.bukkit.craftdispenser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.echo28.bukkit.craftdispenser.crafts.CraftConfig;
import com.echo28.bukkit.craftdispenser.crafts.CraftRepair;


/**
 * CraftDispenser for Bukkit
 * 
 * Huge thanks to revcompgeek for his hard work
 * 
 * @author Nodren
 * @author revcompgeek
 */
public class CraftDispenser extends JavaPlugin
{
	private final CraftDispenserBlockListener blockListener = new CraftDispenserBlockListener(this);
	private final Logger log = Logger.getLogger("Minecraft");

	public Boolean repairDiamond = true;
	public Boolean repairGold = true;
	public Boolean repairIron = true;
	public Boolean repairStone = true;
	public Boolean repairLeather = true;
	public Boolean repairWood = true;

	private Material controlBlock = Material.WOOL;

	private List<CraftConfig> configCrafts = new ArrayList<CraftConfig>();
	private CraftRepair repairCraft;

	public CraftDispenser(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader)
	{
		super(pluginLoader, instance, desc, folder, plugin, cLoader);

		folder.mkdirs();

		File yml = new File(getDataFolder(), "config.yml");
		if (!yml.exists())
		{
			try
			{
				yml.createNewFile();
			}
			catch (IOException ex)
			{
			}
		}
		repairDiamond = getConfiguration().getBoolean("repair-diamond", true);
		repairGold = getConfiguration().getBoolean("repair-gold", true);
		repairIron = getConfiguration().getBoolean("repair-iron", true);
		repairStone = getConfiguration().getBoolean("repair-stone", true);
		repairLeather = getConfiguration().getBoolean("repair-leather", true);
		repairWood = getConfiguration().getBoolean("repair-wood", true);
		controlBlock = Material.matchMaterial(getConfiguration().getString("control-block", "wool"));
	}

	public void onDisable()
	{
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " unloaded.");
	}

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Highest, this);
		loadCrafts();

		log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");
	}

	private void loadCrafts()
	{
		for (File file : getDataFolder().listFiles())
		{
			if (file.getName().equalsIgnoreCase("config.yml")) continue;

			Configuration config = new Configuration(file);
			config.load();
			try
			{
				configCrafts.add(new CraftConfig(this, config, file.getName()));
			}
			catch (BadItemException e)
			{
				// ignore
			}
		}
		// configCraft = new CraftConfig(this);
		repairCraft = new CraftRepair(this);
	}

	public boolean isNextToDispenser(Block block)
	{
		if (nextToDispenser(block) != null) { return true; }
		return false;
	}

	public Block nextToDispenser(Block block)
	{
		Block b = block.getRelative(0, 1, 0);
		if (b.getType().name() == "DISPENSER") { return b; }
		b = block.getRelative(0, -1, 0);
		if (b.getType().name() == "DISPENSER") { return b; }
		b = block.getRelative(1, 0, 0);
		if (b.getType().name() == "DISPENSER") { return b; }
		b = block.getRelative(-1, 0, 0);
		if (b.getType().name() == "DISPENSER") { return b; }
		b = block.getRelative(0, 0, 1);
		if (b.getType().name() == "DISPENSER") { return b; }
		b = block.getRelative(0, 0, -1);
		if (b.getType().name() == "DISPENSER") { return b; }
		return null;
	}

	public Boolean isValidCraftDispenser(Block block)
	{
		Block b = block.getFace(BlockFace.DOWN);
		if (b != null)
		{
			if (controlBlock.equals(b.getType())) { return true; }
		}
		return false;
	}

	public void runCrafts(Block block)
	{
		for (CraftConfig cc : configCrafts)
		{
			if (cc.make(block)) return;
		}
		if (repairCraft.make(block)) return;
		// log.info("crafts failed");
	}
}
