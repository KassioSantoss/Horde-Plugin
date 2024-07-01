package brcomkassin.horde.cooldown;

import brcomkassin.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager implements Cooldown {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_TIME = 15 * 1000;
    private final Map<UUID, BukkitRunnable> bukkitRunnableMap = new HashMap<>();

    @Override
    public boolean isOnCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long timeSinceLastUse = System.currentTimeMillis() - cooldowns.get(playerId);
            return timeSinceLastUse < COOLDOWN_TIME;
        }
        return false;
    }

    @Override
    public void startCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void handleCooldown(Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                long timeSinceLastUse = System.currentTimeMillis() - cooldowns.get(player.getUniqueId());
                long remainingTime = (COOLDOWN_TIME - timeSinceLastUse) / 1000;

                if (remainingTime < 0) {
                    cancel();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component("§a§lPronta para uso"));
                    return;
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        component("§4§l[§4Habilidade carregando§4§l] §4faltam §f" + remainingTime + " §4segundos"));
            }
        };
        runnable.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
        bukkitRunnableMap.put(player.getUniqueId(), runnable);
    }

    private BaseComponent component(String msg) {
        return new TextComponent(msg);
    }
}

