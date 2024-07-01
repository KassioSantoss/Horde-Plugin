package brcomkassin.horde.cooldown;

import org.bukkit.entity.Player;

public interface Cooldown {
    boolean isOnCooldown(Player player);

    void startCooldown(Player player);

    void handleCooldown(Player player);

}
