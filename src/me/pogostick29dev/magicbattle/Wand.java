package me.pogostick29dev.magicbattle;

import java.util.Arrays;

import me.pogostick29dev.magicbattle.MessageManager.MessageType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Wand {

	FIRE("Fire", ChatColor.RED, new WandRunnable(), Materia.BLAZE_ROD) {
		public void run(PlayerInteractEvent e) {
			Fireball fb = e.getPlayer().launchProjectile(Fireball.class);
			fb.setIsIncendiary(false);
			fb.setYield(0F);
		}
	}
	
	POISON("Poison", ChatColor.DARK_PURPLE, new WandRunnable(), Materia.STICK) {
		public void run(PlayerInteractEvent e) {
			for (Entity en : e.getPlayer().getNearbyEntities(10, 10, 10)) {
				if (en instanceof Player) {
					((Player) en).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 1));
					MessageManager.getInstance().msg((Player) en, MessageType.INFO, ChatColor.DARK_PURPLE + "You have been poisoned by " + e.getPlayer().getName() + "!");
					MessageManager.getInstance().msg(e.getPlayer(), MessageType.INFO, ChatColor.DARK_PURPLE + "You have poisoned " + ((Player) en).getName() + "!");
				}
			}
		}
	}
	
	SNOW("Snow", ChatColor.LIGHT_BLUE, new WandRunnable(), Material.DIAMOND_HOE) {
		public void run(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Inventory inv = p.getInventory();
			
			Material sn = Material.SNOWBALL
			if (inv.contains(sn)) {
				int snAM = sn.getAmmount(); // <-- ammount in the inv
				int snAF = snAM --;  // <-- ammount after being subtracted by 1 (--)
				
				sn.setAmmount(snAF);
				
				Snowball sno = p.launchProjectile(Snowball.class);
				
				for (Entity  snoe : e.getLocation().getNearbyEntities(10, 10, 10)) {
					if (snoe instanceof Player) {
						((Player) snoe).addPotionEffect(new PotionEffectType.SLOWNESS, 15, 2));
						((Player) snoe).addPotionEffect(new PotionEffectType.BLINDNESS, 15, 2));
						snoe.sendMessage(ChatColor.LIGHT_BLUE + "Burrrr! you were froze by " + ChatColor.RED + p);
					}
				}
				for (Block blar : e.getLocation().subtract(10, 0, 10)) {
					blar.setType(Material.ICE);
					blar.getLocation().getWorld().playEffect(blar.getLocation(), Effect.EXTINGUISH, 10);
				}
				
			}else {
				p.sendMessage(ChatColor.RED + "Not enough ammo!");
				p.playEffect(p.getLocation(), Sound.CLICK, 10, 1);
			}
		}
	}
	
	private String name;
	private ChatColor color;
	private WandRunnable run;
	private Material item;
	
	Wand(String name, ChatColor color, WandRunnable run, Material item) {
		this.name = name;
		this.color = color;
		this.run = run;
		this.item = item;
	}
	
	public String getName() {
		return name;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String getFullName() {
		return color + name;
	}
	
	public void run(PlayerInteractEvent e) {
		run.run(e);
	}
	
	public Material getItem() {
		return item;
	}
	public ItemStack createItemStack() {
		ItemStack i = new ItemStack(item, 1);
		
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(getFullName());
		im.setLore(Arrays.asList("A magic wand!"));
		
		i.setItemMeta(im);
		
		return i;
	}
	
	public static Wand forName(String name) {
		for (Wand w : Wand.values()) {
			if (w.getName().equalsIgnoreCase(name)) return w;
		}
		
		return null;
	}
}

abstract class WandRunnable { public abstract void run(PlayerInteractEvent e); }
