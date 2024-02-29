package me.loving11ish.clansliteapitest.listeners;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.clans.api.ClansLiteAPI;
import me.loving11ish.clans.api.models.Clan;
import me.loving11ish.clansliteapitest.ClansLiteAPITest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    private final FoliaLib foliaLib = ClansLiteAPITest.getFoliaLib();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Check if ClansLiteAPI is found
        if (ClansLiteAPITest.getPlugin().getClansLiteAPI() == null) {
            player.sendMessage("ClansLiteAPI not found!");
            return;
        }

        // Get ClansLiteAPI from the plugin
        ClansLiteAPI clansLiteAPI = ClansLiteAPITest.getPlugin().getClansLiteAPI();

        // Check if the player is in a clan
        if (clansLiteAPI.getClanByBukkitPlayer(player) != null) {
            player.sendMessage("You are in a clan!");
        } else {
            player.sendMessage("You are not in a clan!");
        }

        // Check if the player is in a clan, if not, let's create a clan
        if (clansLiteAPI.getClanByBukkitPlayer(player) != null) {
            player.sendMessage("You are in a clan!");
            Clan clan = clansLiteAPI.getClanByBukkitPlayer(player);
            player.sendMessage("Your clan: " + clan.getClanFinalName());

        } else {

            // Create a clan
            player.sendMessage("You are not in a clan!");
            player.sendMessage("Let's create a clan!");

            // Let's create a clan, but run this task later to avoid world loading lag
            foliaLib.getImpl().runAtEntityLater(player, (task) -> {
                Clan clan = clansLiteAPI.createClan(player, "TestClan");
                if (clan != null) {
                    player.sendMessage("Clan created!");

                    // Get the clan information
                    player.sendMessage("Your clan: " + clan.getClanFinalName());
                    player.sendMessage("Your clan tag: " + clan.getClanPrefix());
                    player.sendMessage("Your clan points: " + clan.getClanPoints());
                    player.sendMessage("Your clan members: " + clan.getClanMembers());
                    player.sendMessage("Your clan allies: " + clan.getClanAllies());
                    player.sendMessage("Your clan enemies: " + clan.getClanEnemies());

                } else {
                    // Failed to create a clan
                    player.sendMessage("Clan not created!");
                }
            }, 20L * 10L); // 10 seconds later

        }
    }
}
