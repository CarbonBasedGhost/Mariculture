package mariculture.api.fishery.fish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mariculture.api.core.Environment.Salinity;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.fishery.CachedCoords;
import mariculture.api.fishery.RodType;
import mariculture.core.lib.ItemLib;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class FishSpecies {
	//Reference from names to number id, mappings + the List of Fish Species
	private static HashMap<String, Integer> ids = new HashMap();
	public static HashMap<Integer, FishSpecies> species = new HashMap();
	
	//The Products this species produces and the biomes they can live in
	private static final HashMap<String, ArrayList<FishProduct>> products = new HashMap();
	public int[] temperature;
	public Salinity[] salinity;
	
	//The Fish Icon
	private IIcon theIcon;
	private IIcon altIcon;

	//Initialising the FishSpecies
	public FishSpecies(int id) {
		ids.put(getSpecies(), id);
		species.put(id, this);
	}
	
	//Should be ignored, just a helper method for getting the fish id
	public final int getID() {
		return ids.get(getSpecies());
	}
	
	/* Group/Species */
	// This just converts the class name to something to be used for the images. no need to touch it, unless you name your classes differently
	public String getSpecies() {
		return (getClass().getSimpleName().toLowerCase()).substring(4);
	}
	
	//Helper method ignore
	protected final boolean isAcceptedTemperature(int temp) {
		return temp >= temperature[0] && temp <= temperature[1];
	}
	
	/** These are called to set the temperature, and salinity this fish type requires, temperature is two integers, minimum to maximum in degrees C **/
	public abstract int[] setSuitableTemperature();
	public abstract Salinity[] setSuitableSalinity();
	
	/** This determines whether a fish item entity will still die when it's in water **/
	public boolean isLavaFish() {
		return false;
	}
	
	/** Whether or not this fish species is dominant **/
	public abstract boolean isDominant();
	
	/* Default DNA, Average Default is marked in brackets, All of these are the default values for specific DNA, they are overwritten by a fish's DNA */
	/** (25) Lifespan in a tank defined in minutes (note DNA overwrites the fish species) **/
	public abstract int getLifeSpan();
	
	/** (200) This is the number of fish eggs that this fish will generate, whole numbers, Suggested Maximum of 5000 **/
	public abstract int getFertility();
	
	/** (1) This is how many attempts to create a product a fish has, defaults generally, 0 = fails everytime **/
	public int getBaseProductivity() {
		return 1;
	}
	
	/** (1) This is how much food this species of fish will consume everytime it's
	 * time to eat in the Fish Tank  **/
	public int getFoodConsumption() {
		return 1;
	}
	
	//NOT DNA, This will allow a fish to use 0 food when their dna says, only if their species lets them
	public boolean requiresFood() {
		return true;
	}
	
	/** Return the amount of water blocks this fish needs **/
	public int getWaterRequired() {
		return 15;
	}
	
	/** (0) return a bonus for the area the fish can do it's world effects**/
	public int getAreaOfEffectBonus(ForgeDirection dir) {
		return 0;
	}
	
//Fish Products, these are always based on species
	/* Fish Products list, should be ignored, just calls the products you added */
	public final ArrayList<FishProduct> getProductList() {
		return products.get(getSpecies());
	}
	
	/** Called when fish are registered **/
	public abstract void addFishProducts();
	
	/** Helper methods **/
	public final void addProduct(Block block, double chance) {
		addProduct(new ItemStack(block), chance);
	}
	
	public final void addProduct(Item item, double chance) {
		addProduct(new ItemStack(item), chance);
	}
	
	/** Add Products, call this from the addFishProducts call, fish can have a maximum of 6 different products **/
	public final void addProduct(ItemStack stack, double chance) {
		String fish = this.getSpecies();
		ArrayList<FishProduct> list = null;
		if(products.containsKey(fish))
			list = products.get(fish);
		else list = new ArrayList();
		if(list.size() < 6)
		list.add(new FishProduct(stack, chance));
		products.put(fish, list);
	}
	
	/* The product the fish produces, called everytime the bubbles complete a cycle, can be ignored by you */
	public final ItemStack getProduct(Random rand) {
		for(FishProduct product: products.get(getSpecies())) {
			int chance = (int) (product.chance * 10);
			if(rand.nextInt(1000) < chance)
				return product.product.copy();
		}
		
		return null;
	}
	
//Raw Fish Products, These are what you can do use/Raw Fish for
	/** Return whether you can use this fish for a potion or not **/
	public String getPotionEffect(ItemStack stack) {
		return null;
	}
	
	/** How much fish oil the fish will give you when liquified in the liquifier,
	 * this is number of buckets worth So if you return 6, the fish will give
	 * you 6 buckets worth of fish oil, the default is roughly 1/6th of a bucket */
	public double getFishOilVolume() {
		return 0.166;
	}
	
	/** Here you can define a custom product for your fish to return when it is
	 * liquified. **/
	public ItemStack getLiquifiedProduct() {
		return new ItemStack(ItemLib.bone);
	}
	
	/** Set the chance of getting the product, the lower the number the higher
	 * the chance, minimum number = 2; If you set it to 1, there will be a 0%
	 * chance of getting the product, do not return 0 or less */
	public int getLiquifiedProductChance() {
		return 10;
	}
	
	/** How much fish meal this species of fish produces **/
	public int getFishMealSize() {
		return (int) Math.floor(getFishOilVolume());
	}
	
	//Food Based Data
	/** How much food eating this fish restores, return -1 if it's not edible **/
	public int getFoodStat() {
		return (int) Math.ceil(getFishOilVolume());
	}
	
	/** How much saturation this fish restores **/
	public float getFoodSaturation() {
		return (float) (getFishOilVolume() / 10F);
	}
	
	/** How long in ticks, it takes to teat this fish **/
	public int getFoodDuration() {
		return 32;
	}
	
	/** Whether or not this fish can eaten if the player is full **/
	public boolean canAlwaysEat() {
		return false;
	}
	
	/** This is called after a player has eaten a raw fish
	 * 
	 * @param World object
	 * @param The player eating */
	public void onConsumed(World world, EntityPlayer player) {
		return;
	}

	//On Action, these are called when certain things happen to a fish	
	/** Called when you right click a fish
	 * 
	 * @param World Object
	 * @param The Player right clicking
	 * @param The FishStack
	 * @param Random */
	public ItemStack onRightClick(World world, EntityPlayer player, ItemStack stack, Random rand) {
		return stack;
	}

	/** This is called every half a second, and lets you affect the world around
	 * the feeder with your fish The tank type is passed so you can determine
	 * whether you are in or outside the tank, It is only called if your fish
	 * are active
	 * 
	 * @param World Object
	 * @param xCoordinate of FishFeeder
	 * @param yCoordinate of FishFeeder
	 * @param zCoordinate of FishFeeder
	 * @param Coordinates of all the water blocks this tank consists of */
	public void affectWorld(World world, int x, int y, int z, ArrayList<CachedCoords> coords) {
		return;
	}

	/** This is called whenever a living entity is in the water of the tank, you can
	 * have your fish species do something special to them if you like It is
	 * called every half a second, so if a player is in for less than that, the
	 * effect won't apply. This is only called if your fish are active
	 * 
	 * @param The Entity */
	public void affectLiving(EntityLivingBase entity) {
		return;
	}
	
	/** Whether this fish allows RF connections to the fish feeder**/
	public boolean canConnectEnergy(ForgeDirection from) {
		return false;
	}
	
	/** Return the light level this fish gives off, same as normal blocks, from 0-15 **/
	public int getLightValue() {
		return 0;
	}
	
	//Work Based things
	/** Whether this fish can work at this time of day, entirely based on species **/
	public boolean canWork(int time) {
		return true;
	}
	
	//Catching Based, When/Where/How the fish can be caught
	/** Return the rod Quality needed to catch this fish **/
	public abstract RodType getRodNeeded();

	/** Return whether this type of world is suitable to catch these fish**/
	public boolean isWorldCorrect(World world) {
		return !world.provider.isHellWorld && world.provider.dimensionId != 1;
	}
	
	/** Return the catch chance based on the variables, return 0 for no catch
	 *  -- This method is bypassed if ignore biome catch chance is enabled -- **/
	public double getCatchChance(World world, Salinity salt, int temp, int time, int height) {
		return isWorldCorrect(world) && MaricultureHandlers.environment.matches(salt, temp, salinity, temperature)? getCatchChance(world, height, time): 0D;
	}
	
	/** Middle version **/
	public double getCatchChance(World world, int height, int time) {
		return isWorldCorrect(world)? getCatchChance(height, time): 0D;
	}
	
	/** Lower version **/
	public double getCatchChance(int height, int time) {
		return getCatchChance();
	}
	
	// Meant to be a double, will be changed to double in 1.7
	/** Shortest version of catch chance **/
	public int getCatchChance() {
		return 5;
	}
	
	/** Returns the chance for this fish to be caught alive
	 * The world
	 * The Salinity of the Water
	 * The Temperature of the Water
	 * The Time of Day of the World
	 * The Y Height fishing At
	 *  -- This method is bypassed if ignore biome catch chance is enabled -- **/
	public double getCaughtAliveChance(World world, Salinity salt, int temp, int time, int height) {
		return isAcceptedTemperature(temp) && salt == salinity[0]? getCaughtAliveChance(world, height, time): 0D;
	}
	
	/** Middle version **/
	public double getCaughtAliveChance(World world, int height, int time) {
		return getCaughtAliveChance(height, time);
	}
	
	/** Lower version **/
	public double getCaughtAliveChance(int height, int time) {
		return getCaughtAliveChance();
	}
	
	/** Shortest version of alive chance chance **/
	public double getCaughtAliveChance() {
		return 0D;
	}
	
	/* Language/Icon */
	/** Fish's name **/
	public String getName() {
		return StatCollector.translateToLocal("fish.data.species." + this.getSpecies());
	}
	
	/** Gendered Icons?, return true if your fish has a different icon for male or female fish **/
	public boolean hasGenderIcons() {
		return false;
	}
	
	/** Returns your fish icon 
	 * @param gender **/
	public IIcon getIcon(int gender) {
		return hasGenderIcons() && gender == 0? altIcon: theIcon;
	}
	
	/** Called to register your fish icon **/
	public void registerIcon(IIconRegister iconRegister) {
		if(hasGenderIcons()) {
			theIcon = iconRegister.registerIcon("mariculture:fish/" + getSpecies() + "_female");
			altIcon = iconRegister.registerIcon("mariculture:fish/" + getSpecies() + "_male");
		} else {
			theIcon = iconRegister.registerIcon("mariculture:fish/" + getSpecies());
		}
	}
}
