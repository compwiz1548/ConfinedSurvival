package com.compwiz1548.ConfinedSurvival;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CSListener implements Listener
{

    //Stop a player from entering the nether before the correct number of Mutant Zombies have been killed.
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnterNetherPortal(PlayerPortalEvent event)
    {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
        {
            if (!Config.nether())
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage("The nether is locked!  Go kill " + Config.numLeft() + " more Mutant Zombies!");
            }
        }
    }

    //Count the number of times a Mutant Zombie is killed.
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(EntityDeathEvent event)
    {
        String entityName = event.getEntity().toString();
        if (entityName.contains("MutantZombie"))
            Config.incrementKilled();
    }
}
