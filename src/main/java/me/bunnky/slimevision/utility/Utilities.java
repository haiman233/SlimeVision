package me.bunnky.slimevision.utility;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.bunnky.slimevision.items.GoldenSlimeFish;
import me.bunnky.slimevision.items.ParticleItem;
import me.bunnky.slimevision.items.slimeeyes.SlimeEye;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeGod;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Utilities {


    public static void setGlow(@NotNull SlimefunItemStack i) {
        ItemMeta m = i.getItemMeta();
        if (m != null) {
            m.addEnchant(Enchantment.LURE, 1, true);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            i.setItemMeta(m);
        }
    }

    public static @NotNull String getColorName(@NotNull ChatColor c) {
        if (c.equals(ChatColor.RED)) {
            return "Red";
        } else if (c.equals(ChatColor.DARK_RED)) {
            return "Dark Red";
        } else if (c.equals(ChatColor.DARK_AQUA)) {
            return "Dark Aqua";
        } else if (c.equals(ChatColor.AQUA)) {
            return "Aqua";
        } else if (c.equals(ChatColor.YELLOW)) {
            return "Yellow";
        } else if (c.equals(ChatColor.GREEN)) {
            return "Green";
        } else if (c.equals(ChatColor.DARK_GREEN)) {
            return "Dark Green";
        } else if (c.equals(ChatColor.DARK_PURPLE)) {
            return "Dark Purple";
        } else if (c.equals(ChatColor.LIGHT_PURPLE)) {
            return "Light Purple";
        } else if (c.equals(ChatColor.BLUE)) {
            return "Blue";
        } else if (c.equals(ChatColor.DARK_BLUE)) {
            return "Dark Blue";
        } else if (c.equals(ChatColor.BLACK)) {
            return "Black";
        } else if (c.equals(ChatColor.WHITE)) {
            return "White";
        } else if (c.equals(ChatColor.GRAY)) {
            return "Gray";
        } else if (c.equals(ChatColor.DARK_GRAY)) {
            return "Dark Gray";
        } else {
            return "Unknown";
        }
    }

    public static void placeBlock(@NotNull Block block, @NotNull BlockFace face, @NotNull Material material) {
        Block placeBlock = block.getRelative(face);
        placeBlock.setType(material);
    }

    public static int sendCountMessage(Player p, int count, String messageType, int msgCooldown, int prevCount) {
        if (msgCooldown <= 0 || count != prevCount) {
            p.sendMessage("§e" + count + " §a" + messageType + " in range");
            msgCooldown = 5;
        } else {
            msgCooldown--;
        }
        return msgCooldown;
    }

    public static int sendNullCountMessage(Player p, List<String> nullMachines, int msgCooldown, int prevCount) {
        if (msgCooldown <= 0 || nullMachines.size() != prevCount) {
            if (!nullMachines.isEmpty()) {
                StringBuilder message = new StringBuilder("§cCleared Null Machines:\n");
                for (String coords : nullMachines) {
                    message.append("§7").append(coords).append("\n");
                }
                p.sendMessage(message.toString());
            }
            msgCooldown = 5;
        } else {
            msgCooldown--;
        }
        return msgCooldown;
    }

    public static boolean isGSF(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof GoldenSlimeFish;
    }

    public static boolean isSlimeEye(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof SlimeEye;
    }

    public static boolean isSlimeEyeGod(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof SlimeEyeGod;
    }

    public static boolean isParticleItem(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof ParticleItem;
    }

    public static Vector getPlayerVector(Player player) {
        return player.getLocation().toVector();
    }

    public static boolean isAirOrLiquid(Block block) {
        return block.getType().isAir() || block.isLiquid();
    }

    public static boolean hasBlockStorage(Block block) {
        return BlockStorage.hasBlockInfo(block);
    }

    public static int getClampedCoordinate(int coordinate, int radius, int min, int max) {
        return Math.min(max, Math.max(min, coordinate + radius));
    }
}
