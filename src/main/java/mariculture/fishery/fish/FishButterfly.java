package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.SALINE;
import static mariculture.core.lib.ItemLib.dropletWater;
import static mariculture.core.lib.ItemLib.feather;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.core.Environment.Time;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.item.ItemStack;

public class FishButterfly extends FishSpecies {
	public FishButterfly(int id) {
		super(id);
	}
	
	@Override
	public int[] setSuitableTemperature() {
		return new int[] { 23, 30 };
	}
	
	@Override
	public Salinity[] setSuitableSalinity() {
		return new Salinity[] { SALINE };
	}

	@Override
	public boolean isDominant() {
		return false;
	}

	@Override
	public int getLifeSpan() {
		return 10;
	}

	@Override
	public int getFertility() {
		return 500;
	}

	@Override
	public int getWaterRequired() {
		return 30;
	}

	@Override
	public void addFishProducts() {
		addProduct(dropletWater, 10D);
		addProduct(feather, 4D);
	}

	@Override
	public double getFishOilVolume() {
		return 0.120D;
	}
	
	@Override
	public ItemStack getLiquifiedProduct() {
		return new ItemStack(feather);
	}
	
	@Override
	public int getLiquifiedProductChance() {
		return 5;
	}

	@Override
	public int getFishMealSize() {
		return 1;
	}

	@Override
	public RodType getRodNeeded() {
		return RodType.GOOD;
	}

	@Override
	public int getCatchChance() {
		return 20;
	}

	@Override
	public double getCaughtAliveChance(int height, int time) {
		return Time.isDawn(time)? 5D: 0D;
	}
}
