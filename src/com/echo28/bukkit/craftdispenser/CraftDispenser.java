package com.echo28.bukkit.craftdispenser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.echo28.bukkit.craftdispenser.crafts.Craft;


/**
 * CraftDispenser for Bukkit
 * 
 * @author Nodren
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
	public Boolean hellBlocks = true;

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
		hellBlocks = getConfiguration().getBoolean("hell-blocks", true);
	}

	public void onDisable()
	{
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " unloaded.");
	}

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Highest, this);

		log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");
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
			if (b.getType().name() == "WOOL") { return true; }
		}
		return false;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	public void runCrafts(Block block)
	{
		for (Crafts craft : Crafts.values())
		{
			try
			{
				Class c = Class.forName("com.echo28.bukkit.craftdispenser.crafts." + craft.getClassName());

				Constructor constructor = c.getConstructor(new Class[]
				{ CraftDispenser.class, Block.class });
				Craft crafting = (Craft) constructor.newInstance(this, block);
				if (crafting.make()) { return; }
			}
			catch (ClassNotFoundException ex)
			{
				log.severe("Couldn't find Craft class: " + ex.toString());
			}
			catch (NoSuchMethodException ex)
			{
				log.severe("Couldn't find craft constructor");
			}
			catch (InvocationTargetException ex)
			{
				log.severe("Couldn't call craft constructor");
			}
			catch (IllegalArgumentException ex)
			{
				log.severe("Couldn't call craft constructor");
			}
			catch (InstantiationException ex)
			{
				log.severe("Couldn't call craft constructor");
			}
			catch (IllegalAccessException ex)
			{
				log.severe("Couldn't call craft constructor");
			}
		}
	}

	private enum Crafts
	{
		// Repair diamond tools and armor
		DIAMOND_REPAIR("DiamondRepair"),
		// Repair gold tools and armor
		GOLD_REPAIR("GoldRepair"),
		// Repair iron tools and armor
		IRON_REPAIR("IronRepair"),
		// Repair stone tools
		STONE_REPAIR("StoneRepair"),
		// Repair leather armor
		LEATHER_REPAIR("LeatherRepair"),
		// Repair wood tools
		WOOD_REPAIR("WoodRepair"),
		// Hell Blocks
		HELL_BLOCKS("HellBlocks"),
		// Custom recipes
		CONFIG("Config");

		private final String className;

		private Crafts(String string)
		{
			className = string;
		}

		public String getClassName()
		{
			return className;
		}
	}

}
