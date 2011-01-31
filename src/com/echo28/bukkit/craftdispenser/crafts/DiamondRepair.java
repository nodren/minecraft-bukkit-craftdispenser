package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class DiamondRepair extends CraftRepair
{

	public DiamondRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.DIAMOND);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairDiamond) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (tools(Material.DIAMOND_PICKAXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_PICKAXE, 1));
			return true;
		}
		if (tools(Material.DIAMOND_SPADE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_SPADE, 1));
			return true;
		}
		if (tools(Material.DIAMOND_AXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_AXE, 1));
			return true;
		}
		if (tools(Material.DIAMOND_SWORD))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_SWORD, 1));
			return true;
		}
		if (tools(Material.DIAMOND_HOE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_HOE, 1));
			return true;
		}
		if (armor(Material.DIAMOND_HELMET))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_HELMET, 1));
			return true;
		}
		if (armor(Material.DIAMOND_CHESTPLATE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
			return true;
		}
		if (armor(Material.DIAMOND_LEGGINGS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_LEGGINGS, 1));
			return true;
		}
		if (armor(Material.DIAMOND_BOOTS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.DIAMOND_BOOTS, 1));
			return true;
		}

		return false;
	}

}
