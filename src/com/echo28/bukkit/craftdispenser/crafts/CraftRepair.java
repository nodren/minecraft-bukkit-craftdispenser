package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


abstract public class CraftRepair extends Craft
{
	private Material type;

	public CraftRepair(CraftDispenser plugin, Block block, Material type)
	{
		super(plugin, block);
		this.type = type;
	}

	protected boolean tools(Material tool)
	{
		Material[] mats =
		{ type, tool, Material.STICK };
		if (craftUpToDown(mats)) { return true; }
		return false;
	}

	protected boolean armor(Material armor)
	{
		Material[] mats =
		{ type, armor };
		if (craftUpToDown(mats)) { return true; }
		return false;
	}
}
