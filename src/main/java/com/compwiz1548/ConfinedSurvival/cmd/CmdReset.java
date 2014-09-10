package com.compwiz1548.ConfinedSurvival.cmd;

import com.compwiz1548.ConfinedSurvival.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdReset extends CSCmd {
    public CmdReset() {
        name = permission = "reset";
        minParams = 0;
        maxParams = 1;

        addCmdExample(nameEmphasized() + "[world] - Resets the counter for the world and locks the nether.");
//		helpText = "If [command] is specified, info for that particular command will be provided.";
    }

    @Override
    public void execute(CommandSender sender, Player player, List<String> params, String worldName) {
        Config.setNetherLock(false);
        Config.setNumKilled(0);
        sender.sendMessage(Config.worldName() + " reset.  Kill " + Config.numLeft() + " more Mutant Zombies to unlock the nether.");
    }
}
