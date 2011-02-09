package com.echo28.bukkit.craftdispenser;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;



public class CraftDispenserBlockListener extends BlockListener
{
	private final CraftDispenser plugin;

	public CraftDispenserBlockListener(CraftDispenser instance)
	{
		plugin = instance;
	}

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block dispenser = plugin.nextToDispenser(event.getBlock());
		if ((dispenser != null) && (event.getNewCurrent() > 0) && plugin.isValidCraftDispenser(dispenser))
		{
			// cancel the default event
			plugin.runCrafts(dispenser);
			event.setNewCurrent(0);
			//event.setCancelled(true);
		}
	}

}
