package brcomkassin.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCustom implements Item {

    public void getItem(Player player, ItemCustomType itemCustomType) {
        ItemStack itemStack = createCustomItem(itemCustomType);

        player.getInventory().addItem(itemStack);
        player.sendMessage("Item recebido com sucesso!");
    }

    @Override
    public ItemStack createCustomItem(ItemCustomType itemCustomType) {
        return ItemBuilder.of(itemCustomType.getMaterial())
                .setName(itemCustomType.getName())
                .setLore(itemCustomType.getLore())
                .build();
    }

}
