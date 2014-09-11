package com.compwiz1548.ConfinedSurvival.cmd;

import com.compwiz1548.ConfinedSurvival.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdCheck extends CSCmd
{
    public CmdCheck()
    {
        name = permission = "check";
        minParams = 0;
        maxParams = 0;

        addCmdExample(nameEmphasized() + "- Checks whether or not the nether is locked.");
    }

    @Override
    public void execute(CommandSender sender, Player player, List<String> params, String worldName)
    {
        if (Config.nether())
        {
            sender.sendMessage("The nether is unlocked!");
        } else
        {
            sender.sendMessage("The nether is locked!  Kill " + Config.numLeft() + " more Mutant Zombies to unlock it!");
        }
    }
}
