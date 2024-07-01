package brcomkassin.horde.listeners;

import brcomkassin.horde.tasks.HordeTask;
import brcomkassin.Main;
import brcomkassin.horde.HordeManager;
import brcomkassin.horde.cooldown.CooldownManager;
import brcomkassin.item.custom.ItemCustomMetaData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHitListener implements Listener {
    private final CooldownManager cooldownManager;

    public PlayerHitListener() {
        cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void onHit(PlayerInteractEvent event) {
        if (!isValidAction(event.getAction())) return;
        if (!isValidInteraction(event)) return;

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!isValidItem(itemInHand)) return;
        if (!hasRequiredMetadata(itemInHand)) return;

        if (cooldownManager.isOnCooldown(player)) {
            cooldownManager.handleCooldown(player);
        } else {
            useItem(player, event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getPlayer().getInventory().getItem(event.getNewSlot());

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component(""));

        if (isNetheriteSword(itemInHand)) {
            if (cooldownManager.isOnCooldown(player)) {
                cooldownManager.handleCooldown(player);
                return;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component("§a§lPronta para uso"));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && HordeTask.livingEntityList.contains(entity)) {
            event.setCancelled(true);
        }
    }

    private boolean isNetheriteSword(ItemStack item) {
        return item != null && item.getType() == Material.NETHERITE_SWORD;
    }

    private boolean isValidAction(Action action) {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }

    private boolean isValidInteraction(PlayerInteractEvent event) {
        return event.getItem() != null && event.getItem().getType() != Material.AIR;
    }

    private boolean isValidItem(ItemStack item) {
        return item.getType() == Material.NETHERITE_SWORD;
    }

    private boolean hasRequiredMetadata(ItemStack item) {
        return getMetaData(item, ItemCustomMetaData.HORDE_ITEM);
    }

    private void useItem(Player player, PlayerInteractEvent event) {
        HordeManager hordeManager = HordeManager.getHordeManager();
        hordeManager.invoke(event);

        cooldownManager.startCooldown(player);
        player.sendMessage("§aInvocando Orda");
    }

    private BaseComponent component(String msg) {
        return new TextComponent(msg);
    }

    private boolean getMetaData(ItemStack itemStack, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(namespacedKey, PersistentDataType.STRING);
    }


}