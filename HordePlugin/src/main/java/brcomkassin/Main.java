package brcomkassin;

import brcomkassin.item.command.ItemCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("horde").setExecutor(new ItemCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
