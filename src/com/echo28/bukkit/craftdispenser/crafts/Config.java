package com.echo28.bukkit.craftdispenser.crafts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.Configuration;

import com.echo28.bukkit.craftdispenser.CraftDispenser;


public class Config extends Craft
{
	private List<Configuration> configs = new ArrayList<Configuration>();

	public Config(CraftDispenser plugin, Block block)
	{
		super(plugin, block);

		loadCustom();
	}

	private void loadCustom()
	{
		for (File file : plugin.getDataFolder().listFiles())
		{
			if (file.getName().equalsIgnoreCase("config.yml"))
			{
				continue;
			}
			Configuration config = new Configuration(file);
			config.load();
			configs.add(config);
		}
	}

	@Override
	public boolean make()
	{
		Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, 0);
		for (Configuration config : configs)
		{
			if (checkConfig(config))
			{
				block.getWorld().dropItem(location, new ItemStack(config.getInt("crafted-item", 0), config.getInt("crafted-amount", 1)));
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkConfig(Configuration config)
	{
		if (config.getString("craft-type").equalsIgnoreCase("up-down")) { return checkUpDownConfig(config); }
		if (config.getString("craft-type").equalsIgnoreCase("custom")) { return checkCustomConfig(config); }
		return false;
	}

	private boolean checkUpDownConfig(Configuration config)
	{
		List<Integer> mats = config.getIntList("materials", null);
		List<Material> materials = new ArrayList<Material>();
		int i = 0;
		for (int mat : mats)
		{
			materials.add(Material.getMaterial(mat));
			i++;
		}

		Material[] materialArray = new Material[materials.size()];
		materials.toArray(materialArray);
		return craftUpToDown(materialArray);
	}

	private boolean checkCustomConfig(Configuration config)
	{
		List<Integer> slotsToEmpty = new ArrayList<Integer>();
		for (int i = 0; i <= 8; i++)
		{
			int item = config.getInt("material-" + (i + 1), 0);
			if (item == 0)
			{
				continue;
			}
			slotsToEmpty.add(i);
			if (inventory.getItem(i).getTypeId() != item) { return false; }
		}
		Integer[] slots = new Integer[slotsToEmpty.size()];
		slotsToEmpty.toArray(slots);
		subtractItems(slots);
		return true;
	}

}
