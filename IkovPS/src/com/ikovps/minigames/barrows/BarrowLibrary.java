package com.ikovps.minigames.barrows;

import org.parabot.environment.api.utils.Timer;
import org.rev317.min.api.wrappers.Tile;
import org.rev317.min.api.wrappers.TilePath;

/*
 * Data class for IkovBarrows
 */
public class BarrowLibrary 
{
	
	public static String status = "";
	public static String imageLink = "";
	
	public static Timer renewTime = new Timer();
	public static Timer killTime = new Timer();
	
	public static final int AHRIM_TOMB  = 6821, AHRIM_STAIRS  = 6702,
			          		DHAROK_TOMB = 6771, DHAROK_STAIRS = 6703,
			          		GUTHAN_TOMB = 6773, GUTHAN_STAIRS = 6704, 
			          		KARIL_TOMB  = 6822, KARIL_STAIRS  = 6705,
			          		TORAG_TOMB  = 6772, TORAG_STAIRS  = 6706,
			          		VERAC_TOMB  = 6823, VERAC_STAIRS  = 6707, 
			          		CHEST  = 10284,     BANK = 2213;	
	
	
	public static int killCount;
	public static int totalRuns;
	
	public static boolean meleeIsOn,
			       		  mageIsOn,
			              rangeIsOn,
			              firstRenewDrink,
			              somethingWrong,
			              falseLoot,
			              autoCastOn,
			              healthIsLow,
			              exceptionThrown,
			              paintIsUpdated;
	
	
	private static final Tile[] VERAC_ROUTE = {new Tile (3563, 3306, 0), new Tile(3560, 3303, 0), new Tile(3557, 3299, 0)},
						        AHRIM_ROUTE = {new Tile (3558, 3295, 0), new Tile (3563, 3293, 0), new Tile(3565, 3290, 0)},
						        DHAROK_ROUTE = {new Tile (3568, 3290, 0), new Tile (3571, 3292, 0), new Tile(3574, 3296, 0)},
						        GUTHAN_ROUTE = {new Tile (3575, 3294, 0), new Tile(3575, 3590, 0), new Tile(3577, 3283, 0)},
						        KARIL_ROUTE = {new Tile (3571, 3282, 0), new Tile(3566, 3279, 0), new Tile(3566, 3275, 0)},
						        TORAG_ROUTE = {new Tile (3557, 3280, 0), new Tile(3553, 3283, 0)},
						        TORVER_ROUTE = {new Tile (3554, 3286, 0), new Tile(3555, 3292, 0), new Tile(3557, 3298, 0)},
						        AHRIM_SAFE_ROUTE = {new Tile (3557, 9701, 3), new Tile(3557, 9698, 3)},
						        BANK_ROUTE = {new Tile (3092, 3500, 0), new Tile(3096, 3495, 0)};
	
	public static final TilePath BANK_PATH = new TilePath(BANK_ROUTE),
			               		 VERAC_PATH = new TilePath(VERAC_ROUTE),
			                     AHRIM_PATH = new TilePath(AHRIM_ROUTE),
			                     AHRIM_SPATH = new TilePath(AHRIM_SAFE_ROUTE),
			                     DHAROK_PATH = new TilePath(DHAROK_ROUTE),
			                     GUTHAN_PATH = new TilePath(GUTHAN_ROUTE),
			                     KARIL_PATH = new TilePath(KARIL_ROUTE),
			                     TORAG_PATH = new TilePath(TORAG_ROUTE),
			                     TORVER_PATH = new TilePath(TORVER_ROUTE);
	
	
	public static final int SPADE = 953, SHARK = 386, GOLD = 996, FIRE = 555,  WATER = 556, AIR = 557, 
							EARTH = 558, MIND = 559, BODY = 560, CHAOS = 563, LAW = 564, COSMIC = 565, 
							BLOODS = 566, DEATHS = 561, RENEWAL4 = 21631, RENEWAL3 = 21633, 
							RENEWAL2 = 21635, RENEWAL1 = 21637, VIAL = 230;
	
	public static final int RENEWALS[] = {RENEWAL1, RENEWAL2, RENEWAL3, RENEWAL4},
			                TRASH[] = {VIAL, FIRE, WATER, AIR, EARTH, MIND, BODY, CHAOS, LAW, COSMIC};
	
	
	public static final Zone BANK_ZONE   = new Zone(new Tile(3087, 3503), new Tile(3099, 3487)),
					   		 START_ZONE  = new Zone(new Tile(3562, 3310), new Tile(3567, 3305)),
					   		 VERAC_ZONE  = new Zone(new Tile(3554, 3300), new Tile(3559, 3296)),
					   		 AHRIM_ZONE  = new Zone(new Tile(3562, 3291), new Tile(3567, 3286)),
					   		 DHAROK_ZONE = new Zone(new Tile(3573, 3301), new Tile(3578, 3294)),
					   		 GUTHAN_ZONE = new Zone(new Tile(3574, 3285), new Tile(3581, 3279)),
					   		 KARIL_ZONE  = new Zone(new Tile(3563, 3278), new Tile(3569, 3274)),
					   		 TORAG_ZONE  = new Zone(new Tile(3551, 3286), new Tile(3556, 3280)),
					   		 LOOT_ZONE   = new Zone(new Tile(3550, 9691), new Tile(3564, 9688));

}
