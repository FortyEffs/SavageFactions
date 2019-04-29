package com.massivecraft.factions.zcore.fupgrades;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.SavageFactions;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.Crops;

import java.util.concurrent.ThreadLocalRandom;

public class CropUpgrades implements Listener {

	@EventHandler
	public void onCropGrow(BlockGrowEvent e) {
		FLocation floc = new FLocation(e.getBlock().getLocation());
		Faction factionAtLoc = Board.getInstance().getFactionAt(floc);

		if (!factionAtLoc.isWilderness()) {
			int level = factionAtLoc.getUpgrade(Upgrade.CROP);
			if (level != 0) {
				int chance = -1;

				switch (level) {
					case 1:
						chance = SavageFactions.plugin.getConfig().getInt("fupgrades.MainMenu.Crops.Crop-Boost.level-1");
						break;
					case 2:
						chance = SavageFactions.plugin.getConfig().getInt("fupgrades.MainMenu.Crops.Crop-Boost.level-2");
						break;
					case 3:
						chance = SavageFactions.plugin.getConfig().getInt("fupgrades.MainMenu.Crops.Crop-Boost.level-3");
						break;
				}

				if (chance >= 0) {
					int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
					if (randomNum <= chance)
						growCrop(e);
				}
			}
		}
	}

	private void growCrop(BlockGrowEvent e) {

		Material cropType = e.getBlock().getType();
		
		if (cropType.equals(SavageFactions.plugin.CROPS) 
				|| cropType.toString().equals("WHEAT")
				|| cropType.toString().equals("CARROTS")
				|| cropType.toString().equals("POTATOES")
				|| cropType.toString().equals("BEETROOTS")
				){
			e.setCancelled(true);
			
			
			Crops myCrops = (Crops) e.getBlock().getState().getData();
			BlockState bs = e.getBlock().getState();
			
			for (CropState cropState : CropState.values()) {
				if (myCrops.getState().equals(cropState)){
					if (cropState.ordinal() <= 5) {
						myCrops.setState(CropState.values()[cropState.ordinal()+2]);
						
						bs.setData(myCrops);
						bs.update();
						return;
					}
					else if (cropState.ordinal() == 6) {
						myCrops.setState(CropState.values()[cropState.ordinal()+1]);
						bs.setData(myCrops);
						bs.update();
						return;
					}
					
				}
			}
			
		}
		

		Block below = e.getBlock().getLocation().subtract(0, 1, 0).getBlock();
		if (below.getType() == SavageFactions.plugin.SUGAR_CANE_BLOCK) {
			Block above = e.getBlock().getLocation().add(0, 1, 0).getBlock();

			if (above.getType() == Material.AIR && above.getLocation().add(0, -2, 0).getBlock().getType() != Material.AIR) {
				above.setType(SavageFactions.plugin.SUGAR_CANE_BLOCK);
			}

		} else if (below.getType() == Material.CACTUS) {
			Block above = e.getBlock().getLocation().add(0, 1, 0).getBlock();

			if (above.getType() == Material.AIR && above.getLocation().add(0, -2, 0).getBlock().getType() != Material.AIR) {
				above.setType(Material.CACTUS);
			}
		}
	}
}
