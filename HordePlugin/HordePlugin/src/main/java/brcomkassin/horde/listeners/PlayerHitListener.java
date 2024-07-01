package brcomkassin.horde.listeners;

import brcomkassin.HordeTask;
import brcomkassin.Main;
import brcomkassin.horde.HordeManager;
import brcomkassin.item.custom.ItemCustomMetaData;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHitListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR || itemInHand.getType() != Material.NETHERITE_SWORD) return;

        if (!getMetaData(itemInHand, ItemCustomMetaData.HORDE_ITEM)) return;

        HordeManager hordeManager = HordeManager.getHordeManager();

        hordeManager.invoke(event);

    }

    public boolean getMetaData(ItemStack itemStack, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();

        return container.has(namespacedKey, PersistentDataType.STRING);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && HordeTask.livingEntityList.contains(entity)) {
            event.setCancelled(true);
        }
    }

}

