package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class CraftRepair extends Craft
{
	public CraftRepair(CraftDispenser plugin, Block block)
	{
		super(plugin, block);
	}
	
	public boolean make() {
		if (plugin.repairDiamond) {
			if (makeTools(Material.DIAMOND, 
					new Material[] {Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE, 
									Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_HOE}))
				return true;
			if (makeArmors(Material.DIAMOND, new Material[] {Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
						   Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS}))
				return true;
		}
		
		if (plugin.repairGold) {
			if (makeTools(Material.GOLD_INGOT, 
					new Material[] {Material.GOLD_PICKAXE, Material.GOLD_SPADE, 
									Material.GOLD_AXE, Material.GOLD_SWORD, Material.GOLD_HOE}))
				return true;
			if (makeArmors(Material.GOLD_INGOT, new Material[] {Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
						   Material.GOLD_LEGGINGS, Material.GOLD_BOOTS}))
				return true;
		}
		
		if (plugin.repairIron) {
			if (makeTools(Material.IRON_INGOT, 
					new Material[] {Material.IRON_PICKAXE, Material.IRON_SPADE, 
									Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_HOE}))
				return true;
			if (makeArmors(Material.IRON_INGOT, new Material[] {Material.IRON_HELMET, Material.IRON_CHESTPLATE,
						   Material.IRON_LEGGINGS, Material.IRON_BOOTS}))
				return true;
		}
		
		
		if (plugin.repairLeather) {
			if (makeArmors(Material.LEATHER, new Material[] {Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
						   Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS}))
				return true;
		}
		
		
		if (plugin.repairStone) {
			if (makeTools(Material.STONE, 
					new Material[] {Material.STONE_PICKAXE, Material.STONE_SPADE, 
									Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_HOE}))
				return true;
		}
		
		
		if (plugin.repairWood) {
			if (makeTools(Material.WOOD, 
					new Material[] {Material.WOOD_PICKAXE, Material.WOOD_SPADE, 
									Material.WOOD_AXE, Material.WOOD_SWORD, Material.WOOD_HOE}))
				return true;
		}
		
		return false;
	}
	
	private boolean makeTools(Material type, Material[] tools) {
		for (Material tool : tools) {
			if (this.tools(type, tool)) {
				CraftDispenser.dispenseItems(block, new ItemStack(tool, 1));
				return true;
			}
		}
		return false;
	}
	private boolean makeArmors(Material type, Material[] armors) {
		for (Material armor : armors) {
			if (this.armor(type, armor)) {
				CraftDispenser.dispenseItems(block, new ItemStack(armor, 1));
				return true;
			}
		}
		return false;
	}

	private boolean tools(Material type, Material tool)
	{
		int[][] items = {
				{type.getId(), 0, 1, -1},
				{tool.getId(), 0, 1, -1},
				{Material.STICK.getId(), 0, 1, -1}
				};
		if (checkVerticalItems(items)) { return true; }
		return false;
	}

	private boolean armor(Material type, Material armor)
	{
		int[][] items = {
				{type.getId(), 0, 1, -1},
				{armor.getId(), 0, 1, -1}
				};
		if (checkVerticalItems(items)) { return true; }
		return false;
	}
}
