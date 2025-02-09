package me.bunnky.slimevision.items;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import static org.bukkit.Bukkit.getServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class SFS extends SimpleSlimefunItem<ItemUseHandler> {

    private final Plugin sf;
    private Field asField;
    private Method savePlayers;
    private Method saveBlocks;

    public SFS(ItemGroup itemGroup,
               SlimefunItemStack item,
               RecipeType recipeType,
               ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe);
        this.hidden = true;
        Utilities.setGlow(item);
        this.sf = getServer().getPluginManager().getPlugin("Slimefun");
        init();
    }

    private void init() {
        if (sf != null) {
            try {
                asField = sf.getClass().getDeclaredField("autoSavingService");
                asField.setAccessible(true);

                Object autoSavingServiceInstance = asField.get(sf);
                Class<?> autoSavingServiceClass = autoSavingServiceInstance.getClass();

                savePlayers = autoSavingServiceClass.getDeclaredMethod("saveAllPlayers");
                savePlayers.setAccessible(true);

                saveBlocks = autoSavingServiceClass.getDeclaredMethod("saveAllBlocks");
                saveBlocks.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();
        Player p = e.getPlayer();

        if (!p.isOp()) {
            return;
        }

        BlockStorage.saveChunks();
        save(p);
    }

    private void save(@NotNull Player p) {
        p.sendMessage("§e[粘液可视化] 正在保存粘液科技的玩家与方块数据...");
        SlimeVision.consoleMsg("MANUAL SAVE INITIATED");

        try {
            if (sf != null && asField != null) {
                Object autoSavingServiceInstance = asField.get(sf);
                if (savePlayers != null) {
                    savePlayers.invoke(autoSavingServiceInstance);
                }
                if (saveBlocks != null) {
                    saveBlocks.invoke(autoSavingServiceInstance);
                }
                p.sendMessage("§a§l[粘液可视化] 粘液科技已成功保存!");
                SlimeVision.consoleMsg("MANUAL SAVE FINISHED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage("§c[粘液可视化] 在保存的时候发生了一个错误。");
        }
    }
}