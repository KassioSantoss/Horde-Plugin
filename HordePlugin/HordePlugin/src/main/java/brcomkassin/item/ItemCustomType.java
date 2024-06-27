package brcomkassin.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

abstract class ItemCustomType {

    public static ItemStack HORDE_ITEM;

    static {
        HORDE_ITEM = ItemBuilder.of(Material.NETHERITE_SWORD)
                .setName("&6&lItem Místico: Ordas De Zumbis")
                .setLore("&6Com esse item você tem a chance de invocar um orda de zumbis",
                        "&6enquanto ataca seus oponentes")
                .build();
    }


}
