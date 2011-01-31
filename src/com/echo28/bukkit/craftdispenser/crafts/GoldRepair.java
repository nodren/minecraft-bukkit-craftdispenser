package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class GoldRepair extends CraftRepair
{

	public GoldRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block, Material.GOLD_INGOT);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairGold) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (tools(Material.GOLD_PICKAXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_PICKAXE, 1));
			return true;
		}
		if (tools(Material.GOLD_SPADE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_SPADE, 1));
			return true;
		}
		if (tools(Material.GOLD_AXE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_AXE, 1));
			return true;
		}
		if (tools(Material.GOLD_SWORD))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_SWORD, 1));
			return true;
		}
		if (tools(Material.GOLD_HOE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_HOE, 1));
			return true;
		}
		if (armor(Material.GOLD_HELMET))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_HELMET, 1));
			return true;
		}
		if (armor(Material.GOLD_CHESTPLATE))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_CHESTPLATE, 1));
			return true;
		}
		if (armor(Material.GOLD_LEGGINGS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_LEGGINGS, 1));
			return true;
		}
		if (armor(Material.GOLD_BOOTS))
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GOLD_BOOTS, 1));
			return true;
		}
		return false;
	}
}
