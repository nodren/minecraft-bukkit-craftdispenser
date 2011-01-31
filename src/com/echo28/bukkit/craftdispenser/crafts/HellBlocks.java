package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class HellBlocks extends Craft
{
	public HellBlocks(CraftDispenser plugin, Block block)
	{
		super(plugin, block);
	}

	@Override
	public boolean make()
	{
		if (!plugin.repairGold) { return false; }
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		if (hellMud())
		{
			block.getWorld().dropItem(location, new ItemStack(Material.SOUL_SAND, 8));
			return true;
		}
		if (hellStone())
		{
			block.getWorld().dropItem(location, new ItemStack(Material.NETHERRACK, 8));
			return true;
		}
		if (glowStone())
		{
			block.getWorld().dropItem(location, new ItemStack(Material.GLOWSTONE, 1));
			return true;
		}
		return false;
	}

	private boolean hellMud()
	{
		Integer[] dirtSlots =
		{ 0, 1, 2, 3, 5, 6, 7, 8 };
		if (isMaterial(dirtSlots, Material.DIRT))
		{
			if (inventory.getItem(4).getType() == Material.WATER_BUCKET)
			{
				subtractItems(dirtSlots);
				emptyBucket(4);
				// remove mats
				return true;
			}
		}
		return false;
	}

	private boolean hellStone()
	{
		Integer[] cobbleSlots =
		{ 0, 1, 2, 3, 5, 6, 7, 8 };
		if (isMaterial(cobbleSlots, Material.COBBLESTONE))
		{
			if (inventory.getItem(4).getType() == Material.SULPHUR)
			{
				subtractItems(cobbleSlots);
				subtractItem(4);
				// remove mats
				return true;
			}
		}
		return false;
	}

	private boolean glowStone()
	{
		Material[] mats =
		{ Material.SAND, Material.TORCH, Material.NETHERRACK };
		if (craftUpToDown(mats)) { return true; }
		return false;
	}

}
