package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class LeatherRepair extends CraftRepair
{

	public LeatherRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.LEATHER);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairLeather) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);

		if (armor(Material.LEATHER_HELMET))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.LEATHER_HELMET, 1));
			return true;
		}
		if (armor(Material.LEATHER_CHESTPLATE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
			return true;
		}
		if (armor(Material.LEATHER_LEGGINGS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.LEATHER_LEGGINGS, 1));
			return true;
		}
		if (armor(Material.LEATHER_BOOTS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.LEATHER_BOOTS, 1));
			return true;
		}
		return false;
	}

}
