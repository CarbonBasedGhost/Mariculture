package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.FRESH;
import static mariculture.core.lib.ItemLib.dropletAqua;
import static mariculture.core.lib.ItemLib.dropletRegen;
import static mariculture.core.lib.ItemLib.dropletWater;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.core.Environment.Time;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FishKoi extends FishSpecies {
	public FishKoi(int id) {
		super(id);
	}

	@Override
	public int[] setSuitableTemperature() {
		return new int[] { 5, 30 };
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
		return 50;
	}

	@Override
	public int getFertility() {
		return 350;
	}

	@Override
	public int getFoodConsumption() {
		return 2;
	}

	@Override
	public int getWaterRequired() {
		return 300;
	}

	@Override
	public int getAreaOfEffectBonus(ForgeDirection dir) {
		return dir != dir.UP && dir != dir.DOWN ? 2 : 0;
	}

	@Override
	public void addFishProducts() {
		addProduct(dropletWater, 5D);
		addProduct(dropletAqua, 3D);
		addProduct(dropletRegen, 4D);
	}

	@Override
	public double getFishOilVolume() {
		return 5.450D;
	}

	@Override
	public int getFishMealSize() {
		return 7;
	}

	@Override
	public int getFoodStat() {
		return 4;
	}

	@Override
	public float getFoodSaturation() {
		return 0.6F;
	}

	@Override
	public int getFoodDuration() {
		return 48;
	}

	@Override
	public void onConsumed(World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 2));
	}

	@Override
	public void affectLiving(EntityLivingBase entity) {
		entity.addPotionEffect(new PotionEffect(Potion.regeneration.id, 33, 1, true));
	}

	@Override
	public RodType getRodNeeded() {
		return RodType.FLUX;
	}

	@Override
	public double getCatchChance(int height, int time) {
		return Time.isDawn(time) ? 10D : Time.isDusk(time) ? 8D : 2D;
	}

	@Override
	public double getCaughtAliveChance(int height, int time) {
		return Time.isDusk(time) && height > 48 && height < 58 ? 5D : 0D;
	}
}
