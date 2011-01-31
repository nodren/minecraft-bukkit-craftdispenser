package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class WoodRepair extends CraftRepair
{

	public WoodRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.WOOD);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairWood) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (tools(Material.WOOD_PICKAXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.WOOD_PICKAXE, 1));
			return true;
		}
		if (tools(Material.WOOD_SPADE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.WOOD_SPADE, 1));
			return true;
		}
		if (tools(Material.WOOD_AXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.WOOD_AXE, 1));
			return true;
		}
		if (tools(Material.WOOD_SWORD))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.WOOD_SWORD, 1));
			return true;
		}
		if (tools(Material.WOOD_HOE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.WOOD_HOE, 1));
			return true;
		}
		return false;
	}
}
