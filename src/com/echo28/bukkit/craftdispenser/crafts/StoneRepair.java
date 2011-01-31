package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class StoneRepair extends CraftRepair
{

	public StoneRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.COBBLESTONE);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairStone) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (tools(Material.STONE_PICKAXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.STONE_PICKAXE, 1));
			return true;
		}
		if (tools(Material.STONE_SPADE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.STONE_SPADE, 1));
			return true;
		}
		if (tools(Material.STONE_AXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.STONE_AXE, 1));
			return true;
		}
		if (tools(Material.STONE_SWORD))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.STONE_SWORD, 1));
			return true;
		}
		if (tools(Material.STONE_HOE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.STONE_HOE, 1));
			return true;
		}
		return false;
	}

}
