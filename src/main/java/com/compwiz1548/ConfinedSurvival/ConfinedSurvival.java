package com.compwiz1548.ConfinedSurvival;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfinedSurvival extends JavaPlugin
{
    public static volatile ConfinedSurvival plugin = null;
    public static volatile CSCommand csCommand = null;


    @Override
    public void onEnable()
    {
        if (plugin == null)
        {
            plugin = this;
        }
        if (csCommand == null)
            csCommand = new CSCommand();

        getCommand("cs").setExecutor(csCommand);
        getServer().getPluginManager().registerEvents(new CSListener(), this);
        Config.load(this, false);
    }
}
