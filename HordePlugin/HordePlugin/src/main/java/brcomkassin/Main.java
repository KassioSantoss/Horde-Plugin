package brcomkassin;

import brcomkassin.horde.HordeManager;
import brcomkassin.horde.listeners.EntityTargetListener;
import brcomkassin.horde.listeners.PlayerHitListener;
import brcomkassin.item.command.ItemCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public HordeManager hordeManager;

    @Override
    public void onEnable() {

        getCommand("specialSwords").setExecutor(new ItemCommand());
        getServer().getPluginManager().registerEvents(new PlayerHitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
