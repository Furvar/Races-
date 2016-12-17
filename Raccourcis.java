package be.Vilevar.Races.Rituels;

public class Raccourcis {

/*	public static ArrayList<Material>ritBrace = new ArrayList<Material>(); 
	public static ArrayList<Material>ritBchance = new ArrayList<Material>();
	
	public Raccourcis(){
		ritBrace.add(Material.DIAMOND_ORE);
		
		ritBchance.add(Material.GOLD_BLOCK); // 1/10
		ritBchance.add(Material.IRON_BLOCK); // 1/7
		ritBchance.add(Material.DIAMOND_BLOCK); // 1/3
		ritBchance.add(Material.EMERALD_BLOCK); // 1/5
	}
	
	public static void setRitNain(Player player, int chance){
		if(player == null || (chance < 1 || chance > 10) || !Raccourcis.isNainRit(player)){
			return;
		}
		player.getWorld().strikeLightning(player.getLocation());
		player.getLocation().add(0, 3, 0).getBlock().setType(Material.AIR);
		player.getLocation().getBlock().setType(Material.LAVA);
		if(!player.isDead()){
			player.damage(player.getMaxHealth());
		}
		Random rand = new Random();
		int c = rand.nextInt(chance-1);
		if(c == 1){
			WolvMCAPI.setRace(player, "nain");
		}
	}
	
	/*
	 A UTILISER SEULEMENT DANS CETTE CLASS : POUR FACILITER VERIF PLUS POUSSEES
	 *//*
	@SuppressWarnings("deprecation")
	public static boolean isRituel(Player player){
		if(player == null) return false;
		ItemStack is = player.getItemInHand();
		Location loc = player.getLocation();
		if(is != null &&  is.getType() == Material.GOLD_HOE){
			//BLOCS NON NULL
			if(loc.getBlock() != null && loc.add(0, 2, 0).getBlock().getType() != null && loc.add(0, 1, 0).getBlock().getType() != null &&
			loc.add(1, 0, 0).getBlock().getType() != null && loc.add(-1, 0, 0).getBlock().getType() != null && loc.add(0, 0, 1).getBlock().getType() != null && loc.add(0, 0, -1).getBlock().getType() != null && 
			loc.add(1, 0, 1).getBlock().getType() != null && loc.add(-1, 0, 1).getBlock().getType() != null && loc.add(1, 0, -1).getBlock().getType() != null && loc.add(-1, 0, -1).getBlock().getType() != null){
				//BLOCS Y
				if(loc.getBlock().getType() == Material.WATER && ritBrace.contains(loc.add(0, 2, 0).getBlock().getType()) && ritBchance.contains(loc.add(0, 3, 0).getBlock().getType())){
					//BLOCS X ET Z
					if(loc.add(1, 0, 0).getBlock().getType() == Material.SOUL_SAND && loc.add(-1, 0, 0).getBlock().getType() == Material.SOUL_SAND && loc.add(0, 0, 1).getBlock().getType() == Material.SOUL_SAND && loc.add(0, 0, -1).getBlock().getType() == Material.SOUL_SAND){
						//BLOCS X + Z
						if(loc.add(1, 0, 1).getBlock().getType() == Material.OBSIDIAN && loc.add(-1, 0, 1).getBlock().getType() == Material.OBSIDIAN && loc.add(1, 0, -1).getBlock().getType() == Material.OBSIDIAN && loc.add(-1, 0, -1).getBlock().getType() == Material.OBSIDIAN){
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public static boolean isNainRit(Player player){
		if(player == null)return false;
		if(Raccourcis.isRituel(player) && player.getLocation().add(0, 2, 0).getBlock().getType() == Material.DIAMOND_ORE){
			return true;
		}else{
			return false;
		}
	}
	
	public static Integer getChance(Player player){
		if(player == null)return null;
		if(Raccourcis.isRituel(player)){
			switch(player.getLocation().add(0, 3, 0).getBlock().getType()){
				case GOLD_BLOCK:
					return 10;
				case IRON_BLOCK:
					return 7;
				case EMERALD_BLOCK:
					return 5;
				case DIAMOND_BLOCK:
					return 3;
				default:
					break;
			}
		}
		return null;
	}*/
}
