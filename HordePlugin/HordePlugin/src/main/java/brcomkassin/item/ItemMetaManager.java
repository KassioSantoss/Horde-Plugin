package brcomkassin.item;

import brcomkassin.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemMetaManager {

    public static ItemStack setItemMetaData(ItemStack item, String key) {

        ItemMeta meta = item.getItemMeta();

        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), key);
        container.set(namespacedKey, PersistentDataType.STRING, key);
        item.setItemMeta(meta);

        return item;

    }

    public static String getItemMetaData(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), key);
        return container.get(namespacedKey, PersistentDataType.STRING);
    }


}
