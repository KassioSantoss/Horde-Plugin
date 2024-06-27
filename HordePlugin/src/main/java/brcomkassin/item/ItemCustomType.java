package brcomkassin.item;

import org.bukkit.Material;

public enum ItemCustomType {
    INVOKE_ITEM("Item místico: Invocar ordas",
            "Com esse item você tem chance de invocar uma orda de zumbis para te ajudar em combate!",
            Material.NETHERITE_SWORD
    );

    private final String name;
    private final String lore;
    private final Material material;

    ItemCustomType(String name, String lore, Material material) {
        this.name = name;
        this.lore = lore;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

}
