package brcomkassin.item.command;

import brcomkassin.item.ItemCustom;
import brcomkassin.item.ItemCustomType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemCommand implements CommandExecutor, TabExecutor {

    private ItemCustom itemCustom;

    public ItemCommand() {
        this.itemCustom = new ItemCustom();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length < 1) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "item_invoke":
                itemCustom.getItem(player, ItemCustomType.INVOKE_ITEM);
                break;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("item_invoke");
        }

        return completions;
    }
}
