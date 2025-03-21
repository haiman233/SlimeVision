package me.bunnky.slimevision.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeInverted;
import me.bunnky.slimevision.utility.Utilities;

public class SlimeScribe extends SlimeEyeInverted {

    public SlimeScribe(ItemGroup itemGroup,
                       SlimefunItemStack item,
                       RecipeType recipeType,
                       ItemStack[] recipe,
                       int radius,
                       int cooldown
    ) {
        super(itemGroup, item, recipeType, recipe, radius, cooldown);
    }

    @Override
    public String getTypeName() {
        return "Slime Scribe";
    }

    protected ItemStack createBook(Player p, List<Block> found) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle("[SV] 不可见机器的位置");
        meta.setAuthor(p.getName());

        StringBuilder firstPage = new StringBuilder();
        firstPage.append("Player Location\n")
            .append(p.getLocation().getBlockX()).append(", ")
            .append(p.getLocation().getBlockY()).append(", ")
            .append(p.getLocation().getBlockZ()).append("\n\n");

        for (int i = 0; i < found.size(); i++) {
            Block b = found.get(i);
            firstPage.append((i + 1)).append(". ")
                .append(b.getX()).append(", ")
                .append(b.getY()).append(", ")
                .append(b.getZ()).append("\n");

            if (firstPage.toString().split("\n").length >= 14) {
                meta.addPage(firstPage.toString());
                firstPage = new StringBuilder();
            }
        }

        if (!firstPage.isEmpty()) {
            meta.addPage(firstPage.toString());
        }
        book.setItemMeta(meta);
        return book;
    }

    @Override
    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player p = e.getPlayer();

        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage("§c库存已满。");
            return;
        }

        UUID pUUID = p.getUniqueId();
        Long lastTime = lastActionTime.get(pUUID);
        long currentTime = System.currentTimeMillis();
        int cooldown = getCooldown();

        if (lastTime != null && (currentTime - lastTime) < cooldown) {
            long remainingTime = (cooldown - (currentTime - lastTime)) / 1000;
            p.sendMessage("§c您必须等待 " + remainingTime + " 秒。");
            return;
        }

        String typeName = getTypeName();
        slimeEyeUsage.put(typeName, slimeEyeUsage.getOrDefault(typeName, 0) + 1);
        users.add(p.getName());

        lastActionTime.put(pUUID, currentTime);

        List<Block> foundMachines = checkArea(p);

        if (foundMachines.isEmpty()) {
            p.sendMessage("§c没有发现不可见机器。");
        } else {
            ItemStack book = createBook(p, foundMachines);
            p.getInventory().addItem(book);
            p.sendMessage("§a不可见机器的位置已被记录在书上。");
        }
    }

    protected List<Block> checkArea(@NotNull Player p) {
        World w = p.getWorld();
        Vector loc = Utilities.getPlayerVector(p);

        int pX = loc.getBlockX();
        int pY = loc.getBlockY();
        int pZ = loc.getBlockZ();

        int minX = pX - r;
        int minY = Utilities.getClampedCoordinate(pY, -r, -64, 320);
        int minZ = pZ - r;

        int maxX = pX + r;
        int maxY = Utilities.getClampedCoordinate(pY, r, -64, 320);
        int maxZ = pZ + r;

        List<Block> foundMachines = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = w.getBlockAt(x, y, z);

                    if (Utilities.hasBlockStorage(b) && (Utilities.isAirOrLiquid(b))) {
                        foundMachines.add(b);
                    }
                }
            }
        }
        return foundMachines;
    }
}