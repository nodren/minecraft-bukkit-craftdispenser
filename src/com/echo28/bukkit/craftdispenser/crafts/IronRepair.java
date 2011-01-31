package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class IronRepair extends CraftRepair
{

	public IronRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.IRON_INGOT);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairIron) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (tools(Material.IRON_PICKAXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_PICKAXE, 1));
			return true;
		}
		if (tools(Material.IRON_SPADE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_SPADE, 1));
			return true;
		}
		if (tools(Material.IRON_AXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_AXE, 1));
			return true;
		}
		if (tools(Material.IRON_SWORD))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_SWORD, 1));
			return true;
		}
		if (tools(Material.IRON_HOE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_HOE, 1));
			return true;
		}
		if (armor(Material.IRON_HELMET))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_HELMET, 1));
			return true;
		}
		if (armor(Material.IRON_CHESTPLATE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_CHESTPLATE, 1));
			return true;
		}
		if (armor(Material.IRON_LEGGINGS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_LEGGINGS, 1));
			return true;
		}
		if (armor(Material.IRON_BOOTS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.IRON_BOOTS, 1));
			return true;
		}
		return false;
	}
}
