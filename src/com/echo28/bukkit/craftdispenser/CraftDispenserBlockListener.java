package com.echo28.bukkit.craftdispenser;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockFromToEvent;
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
	public void onBlockRedstoneChange(BlockFromToEvent event)
	{
		BlockRedstoneEvent e = (BlockRedstoneEvent) event;
		Block dispenser = plugin.nextToDispenser(e.getBlock());
		if ((dispenser != null) && (e.getNewCurrent() > 0) && plugin.isValidCraftDispenser(dispenser))
		{
			// cancel the default event
			plugin.runCrafts(dispenser);
			e.setNewCurrent(0);
			e.setCancelled(true);
		}
	}

}
