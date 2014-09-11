package com.compwiz1548.ConfinedSurvival.cmd;

import com.compwiz1548.ConfinedSurvival.Config;
import com.compwiz1548.ConfinedSurvival.ConfinedSurvival;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdReload extends CSCmd
{
    public CmdReload()
    {
        name = permission = "reload";
        minParams = 0;
        maxParams = 0;

        addCmdExample(nameEmphasized() + "- re-load data from config.yml.");
        helpText = "If you make manual changes to config.yml while the server is running, you can use this command " +
                "to make WorldBorder load the changes without needing to restart the server.";
    }

    @Override
    public void execute(CommandSender sender, Player player, List<String> params, String worldName)
    {
        if (player != null)
            Config.log("Reloading config file at the command of player \"" + player.getName() + "\".");

        Config.load(ConfinedSurvival.plugin, true);

        if (player != null)
            sender.sendMessage("ConfinedSurvival configuration reloaded.");
    }
}
