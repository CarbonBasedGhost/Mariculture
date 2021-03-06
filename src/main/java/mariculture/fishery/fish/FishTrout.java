package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.FRESH;
import static mariculture.core.lib.ItemLib.blueDye;
import static mariculture.core.lib.ItemLib.greenDye;
import static mariculture.core.lib.ItemLib.orangeDye;
import static mariculture.core.lib.ItemLib.pinkDye;
import static mariculture.core.lib.ItemLib.purpleDye;
import static mariculture.core.lib.ItemLib.redDye;
import static mariculture.core.lib.ItemLib.yellowDye;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.item.ItemStack;

public class FishTrout extends FishSpecies {
	public FishTrout(int id) {
		super(id);
	}

	@Override
	public int[] setSuitableTemperature() {
		return new int[] { -1, 35 };
	}

	@Override
	public Salinity[] setSuitableSalinity() {
		return new Salinity[] { FRESH };
	}

	@Override
	public boolean isDominant() {
		return false;
	}

	@Override
	public int getLifeSpan() {
		return 8;
	}

	@Override
	public int getFertility() {
		return 200;
	}

	@Override
	public int getWaterRequired() {
		return 40;
	}

	@Override
	public void addFishProducts() {
		addProduct(redDye, 5D);
		addProduct(orangeDye, 4D);
		addProduct(yellowDye, 5D);
		addProduct(greenDye, 3.5D);
		addProduct(blueDye, 3.5D);
		addProduct(purpleDye, 2.5D);
	}

	@Override
	public double getFishOilVolume() {
		return 3.600D;
	}
	
	@Override
	public ItemStack getLiquifiedProduct() {
		return pinkDye;
	}

	@Override
	public RodType getRodNeeded() {
		return RodType.OLD;
	}

	@Override
	public int getCatchChance() {
		return 25;
	}

	@Override
	public double getCaughtAliveChance(int height, int time) {
		return height > 100 && height < 110 ? 5D : 0D;
	}
}
