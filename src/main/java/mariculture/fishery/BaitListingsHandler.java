package mariculture.fishery;

import java.util.ArrayList;
import java.util.List;

import mariculture.api.fishery.Fishing;
import mariculture.api.fishery.RodType;
import mariculture.core.util.Text;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//Adds the information about the baits rods can use, if they are registered
public class BaitListingsHandler {
	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		List list = event.toolTip;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			RodType quality = Fishing.fishing.getRodType(stack);
			if(quality != null) {
				list.add(Text.INDIGO + StatCollector.translateToLocal("mariculture.string.bait"));
				ArrayList<List> baits = Fishing.fishing.getCanUseList(quality);
				for(List l: baits) {
					ItemStack bait = new ItemStack((Item)l.get(0), 1, (Integer)l.get(1));
					if(bait != null) {
						list.add(bait.getItem().getItemStackDisplayName(bait));
					}
				} 
			}
		} else if( Fishing.fishing.getRodType(stack) != null) {
			list.add(Text.getShiftText("rod"));
		}
	}
}
