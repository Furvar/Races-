package be.Vilevar.Races.RacesList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import be.Vilevar.Races.Races;
import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.WolvMCAPI;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;
import fr.nashoba24.wolvmc.events.WolvMCReloadEvent;

public class Nain implements Listener{

	public static boolean enabled = true;

	private String chargeTrue = ChatColor.GREEN+"Vous êtes ne pleine charge !";
	private String chargeFalse = ChatColor.RED+"Vous n'êtes plus en charge !";
	private static boolean charge = false;
	private ArrayList<Player> canCharge = new ArrayList<>();
	
	private String notNain = ChatColor.RED+"Vous n'êtes pas un nain.";
	private static String joyauItem = ChatColor.GREEN+" du joyau vert";
	private Integer cooldownCharge = 60*20;
	private double damageSword = 1.8;
	private double damageAxe = 1.8;
	private double damageEnderman = 2;
	private boolean swordModifier = true;
	private boolean axeModifier = true;
	private boolean endermanModifier = true;
	private boolean notBow = true;
	private boolean solidityIron = true;
	private boolean dropIronIngot = true;
	private boolean dropGoldIngot = true;
	private static boolean joyauxPower = true;
	
	@EventHandler
	public void onInitEffects(WolvMCInitEffectsEvent e){
		Player p = e.getPlayer();
		if(e.getRace().equals("nain")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 0), true);
			if(WolvMCAPI.hasFinishMission(p, "nain.2")){
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 1), true);
			}else{
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2147483647, 0), true);
			}
		}
	}
	
	@EventHandler
	public void onCharge(final PlayerInteractEvent e){
		final Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("nain") && e.getAction() == Action.LEFT_CLICK_BLOCK && p.isSneaking() && !charge && !canCharge.contains(p)){
			p.sendMessage(chargeTrue);
			charge = true;
			canCharge.add(p);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 4), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15*20, 4), true);
			Bukkit.getScheduler().runTaskLater(Races.getPlugin(Races.class), new Runnable(){
				@Override
				public void run() {
					p.sendMessage(chargeFalse);
					charge = false;
					WolvMCInitEffectsEvent event = new WolvMCInitEffectsEvent(e.getPlayer());
	            	Bukkit.getServer().getPluginManager().callEvent(event);
				}
			}, 15*20+5);
			//cooldowns impro
			Bukkit.getScheduler().runTaskLater(Races.getPlugin(Races.class), new Runnable(){
				@Override
				public void run() {
					if(canCharge.contains(p))canCharge.remove(p);
				}
			}, cooldownCharge);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChangeDegats(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			if(WolvMC.getRace(p.getName()).equals("nain")){
				if(p.getItemInHand().getType().toString().contains("SWORD") && swordModifier){
					e.setDamage(e.getDamage()/damageSword);
					WolvMCAPI.addNumberToPlayerMission(p, "nain.1", e.getFinalDamage());
				}
				if(p.getItemInHand().getType().toString().contains("AXE") && axeModifier)e.setDamage(e.getDamage()*damageAxe);
				if(p.getItemInHand().getType() == Material.BOW && notBow)e.setCancelled(true);
			}
		}
		else if(e.getDamager() instanceof Enderman && e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(WolvMC.getRace(p.getName()).equals("nain") && endermanModifier){
				e.setDamage(e.getDamage()*damageEnderman);
			}
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e){
		Player p = (Player) e.getWhoClicked();
		if(e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(joyauItem) && !WolvMC.getRace(p.getName()).equals("nain")){
			e.setCancelled(true);
			p.sendMessage(notNain);
		}
		if(WolvMC.getRace(p.getName()).equals("nain") && solidityIron && (e.getCurrentItem().getType() == Material.IRON_AXE || e.getCurrentItem().getType() == Material.IRON_BOOTS || e.getCurrentItem().getType() == Material.IRON_CHESTPLATE || e.getCurrentItem().getType() == Material.IRON_HELMET || e.getCurrentItem().getType() == Material.IRON_HOE || e.getCurrentItem().getType() == Material.IRON_LEGGINGS || e.getCurrentItem().getType() == Material.IRON_PICKAXE || e.getCurrentItem().getType() == Material.IRON_SWORD || e.getCurrentItem().getType() == Material.IRON_SPADE)){
			p.getInventory().removeItem(e.getCurrentItem());
			p.getInventory().addItem(createItem(e.getCurrentItem().getType(), (byte)0, Enchantment.DURABILITY, 1));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(WolvMC.getRace(p.getName()).equals("nain")){
			switch(e.getBlock().getType()){
			case DIAMOND_ORE:
				WolvMCAPI.addNumberToPlayerMission(p, "nain.2", (double)1);
				break;
			case IRON_ORE:
				if(!dropIronIngot)return;
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				if(p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)){
					Random rand = new Random();
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, rand.nextInt(p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))+2));
				}else{
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
				}
				break;
			case GOLD_ORE:
				if(!dropGoldIngot)return;
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				if(p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)){
					Random rand = new Random();
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, rand.nextInt(p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))+2));
				}else{
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
				}
				break;
			case EMERALD_ORE:
				if(!joyauxPower)return;
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), createItem(Material.EMERALD, (byte)0, ChatColor.GREEN+"Joyau Vert"));
			default:
				break;
			}
			if(e.getBlock().getType() == Material.GRASS_PATH)e.setCancelled(true);;
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e){
		Player p = e.getPlayer();
		if((e.getItem().getType() == Material.APPLE || e.getItem().getType() == Material.GOLDEN_APPLE || e.getItem().getType() == Material.CARROT || e.getItem().getType() == Material.MELON) && WolvMC.getRace(p.getName()).equals("nain")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15*20, 4, true), true);
		}
	}
	
	public static void initNain(){
		WolvMC.addMission("nain.1", (double)500, "nain", "Done %goal% unit of damages met sword", "Charge capacity");
		WolvMC.addMission("nain.2", (double)50, "nain", "Mine %goal% diamond_ore's", "Haste II");
		WolvMCAPI.addRace("nain", ChatColor.BLUE+"Nain", new ItemStack(Material.DIAMOND_PICKAXE));
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Nain Class loaded!");
		
		if(joyauxPower){
			ShapedRecipe casque = new ShapedRecipe(createItem(Material.IRON_HELMET, (byte)0, Material.IRON_HELMET.name()+joyauItem));
			casque.shape(new String[] {" J ", " C ", "   "}).shape(new String[] {"   ", " C ", " J "}).shape(new String[] {"   ", " CJ", "   "}).shape(new String[] {"   ", "JC ", "   "});
			casque.setIngredient('C', Material.IRON_HELMET).setIngredient('J', Material.EMERALD);
			Races.getInstance().getServer().addRecipe(casque);
			ShapedRecipe plastron = new ShapedRecipe(createItem(Material.IRON_CHESTPLATE, (byte)0, Material.IRON_CHESTPLATE.name()+joyauItem));
			plastron.shape(new String[] {" J ", " C ", "   "}).shape(new String[] {"   ", " C ", " J "}).shape(new String[] {"   ", " CJ", "   "}).shape(new String[] {"   ", "JC ", "   "});
			plastron.setIngredient('C', Material.IRON_CHESTPLATE).setIngredient('J', Material.EMERALD);
			Races.getInstance().getServer().addRecipe(plastron);
			ShapedRecipe jambiere = new ShapedRecipe(createItem(Material.IRON_LEGGINGS, (byte)0, Material.IRON_LEGGINGS.name()+joyauItem));
			jambiere.shape(new String[] {" J ", " C ", "   "}).shape(new String[] {"   ", " C ", " J "}).shape(new String[] {"   ", " CJ", "   "}).shape(new String[] {"   ", "JC ", "   "});
			jambiere.setIngredient('C', Material.IRON_LEGGINGS).setIngredient('J', Material.EMERALD);
			Races.getInstance().getServer().addRecipe(jambiere);
			ShapedRecipe bottes = new ShapedRecipe(createItem(Material.IRON_BOOTS, (byte)0, Material.IRON_BOOTS.name()+joyauItem));
			bottes.shape(new String[] {" J ", " C ", "   "}).shape(new String[] {"   ", " C ", " J "}).shape(new String[] {"   ", " CJ", "   "}).shape(new String[] {"   ", "JC ", "   "});
			bottes.setIngredient('C', Material.IRON_BOOTS).setIngredient('J', Material.EMERALD);
			Races.getInstance().getServer().addRecipe(bottes);
		
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Races.getPlugin(Races.class), new Runnable(){
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()){
						if(WolvMC.getRace(p.getName()).equals("nain") && !charge){
							if(p.getInventory().getHelmet().getItemMeta()!=null && p.getInventory().getHelmet().getItemMeta().getLore()!=null && p.getInventory().getHelmet().getItemMeta().getLore().get(0).contains(joyauItem)){
								if(p.hasPotionEffect(PotionEffectType.NIGHT_VISION))p.removePotionEffect(PotionEffectType.NIGHT_VISION);
								p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*15, 4), true);
							}
							if(p.getInventory().getChestplate().getItemMeta()!=null && p.getInventory().getChestplate().getItemMeta().getLore()!=null && p.getInventory().getChestplate().getItemMeta().getLore().get(0).contains(joyauItem)){
								if(p.hasPotionEffect(PotionEffectType.REGENERATION))p.removePotionEffect(PotionEffectType.REGENERATION);
								p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*15, 0), true);
							}
							if(p.getInventory().getLeggings().getItemMeta()!=null && p.getInventory().getLeggings().getItemMeta().getLore()!=null && p.getInventory().getLeggings().getItemMeta().getLore().get(0).contains(joyauItem)){
								if(p.hasPotionEffect(PotionEffectType.JUMP))p.removePotionEffect(PotionEffectType.JUMP);
								p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*15, 2), true);
							}
							if(p.getInventory().getBoots().getItemMeta()!=null && p.getInventory().getBoots().getItemMeta().getLore()!=null && p.getInventory().getBoots().getItemMeta().getLore().get(0).contains(joyauItem)){
								if(p.hasPotionEffect(PotionEffectType.SPEED))p.removePotionEffect(PotionEffectType.SPEED);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*15, 2), true);
							}
						}
					}
				}
			}, 20, 20);
		}
	}
	
	@EventHandler
	public void reloadNain(WolvMCReloadEvent e){
		File file = new File(Races.getPlugin(Races.class).getDataFolder() + "/nain.yml");
		if(!file.exists()) {
			  InputStream stream = WolvMC.class.getClassLoader().getResourceAsStream("be/Vilevar/Races/Config/nain.yml");
			  FileOutputStream fos = null;
			  try {
			      fos = new FileOutputStream(WolvMC.getPlugin(WolvMC.class).getDataFolder() + "/nain.yml");
			      byte[] buf = new byte[2048];
			      int r = stream.read(buf);
			      while(r != -1) {
			          fos.write(buf, 0, r);
			          r = stream.read(buf);
			      }
			      fos.close();
			  } catch (IOException e1) {
				e1.printStackTrace();
			  }
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isSet("enabled")){
			enabled = config.getBoolean("enabled");
		}
		if(config.isSet("cooldown-charge")){
			cooldownCharge = config.getInt("cooldown-charge");
			cooldownCharge = cooldownCharge*20;
		}
		if(config.isSet("damage-sword")){
			damageSword = config.getDouble("damage-sword");
		}
		if(config.isSet("damage-axe")){
			damageAxe = config.getDouble("damage-axe");
		}
		if(config.isSet("damage-enderman")){
			damageEnderman = config.getDouble("damage-enderman");
		}
		if(config.isSet("sword-modifier")){
			swordModifier = config.getBoolean("sword-modifier");
		}
		if(config.isSet("axe-modifier")){
			axeModifier = config.getBoolean("axe-modifier");
		}
		if(config.isSet("enderman-modifier")){
			endermanModifier = config.getBoolean("enderman-modifier");
		}
		if(config.isSet("not-bow")){
			notBow = config.getBoolean("not-bow");
		}
		if(config.isSet("solidity-iron")){
			solidityIron = config.getBoolean("solidity-iron");
		}
		if(config.isSet("drop-iron-ingot")){
			dropIronIngot = config.getBoolean("drop-iron-ingot");
		}
		if(config.isSet("drop-gold-ingot")){
			dropGoldIngot = config.getBoolean("drop-gold-ingot");
		}
		if(config.isSet("joyaux")){
			joyauxPower= config.getBoolean("joyaux");
		}
	}
	
	
	
	
	
	private static ItemStack createItem(Material mat, byte data, String name){
		if(mat == null)return null;
		ItemStack is = new ItemStack(mat, 1, (byte)data);
		ItemMeta im = is.getItemMeta();
		if(name != null){
			im.setDisplayName(name);
			ArrayList<String>desc = new ArrayList<String>();
			desc.add(name);
			im.setLore(desc);
		}
		is.setItemMeta(im);
		return is;
	}
	
	private ItemStack createItem(Material mat, byte data, Enchantment ench, int lvl){
		if(mat == null)return null;
		ItemStack is = new ItemStack(mat, 1, (byte)data);
		ItemMeta im = is.getItemMeta();
		if(ench != null){
			im.addEnchant(ench, lvl, true);
		}
		is.setItemMeta(im);
		return is;
	}
}
