package com.echo28.bukkit.craftdispenser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.echo28.bukkit.craftdispenser.crafts.Config;
import com.echo28.bukkit.craftdispenser.crafts.CraftRepair;


/**
 * CraftDispenser for Bukkit
 * 
 * @author Nodren
 */
public class CraftDispenser extends JavaPlugin
{
	private final CraftDispenserBlockListener blockListener = new CraftDispenserBlockListener(this);
	private final Logger log = Logger.getLogger("CraftDispenser");

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
		
		Items.setupItems();
	}
	
	public static void dispenseItems(Block block, ItemStack dispenseItem) {
		dispenseItems(block, new ItemStack[] {dispenseItem});
	}
	
	public static void dispenseItems(Block block, ItemStack[] dispenseItems) {
		org.bukkit.craftbukkit.block.CraftDispenser cd = new org.bukkit.craftbukkit.block.CraftDispenser(block);
		ItemStack[] contents = cd.getInventory().getContents();
		ItemStack[] dispenseContents = new ItemStack[9];
		
		int totalItems = 0;
		for (int i = 0; i < dispenseItems.length; i++) {
			totalItems += dispenseItems[i].getAmount();
			dispenseContents[i] = dispenseItems[i];
		}
		
		cd.getInventory().setContents(dispenseContents);
		for (int i = 0; i < totalItems; i++)
			cd.dispense();
		cd.getInventory().setContents(contents);
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

	
	public void runCrafts(Block block)
	{
		if (new Config(this, block).make()) { return; }
		if (new CraftRepair(this, block).make()) { return; }
	}
}
