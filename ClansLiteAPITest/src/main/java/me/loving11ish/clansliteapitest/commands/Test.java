package me.loving11ish.clansliteapitest.commands;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.clans.api.ClansLiteAPI;
import me.loving11ish.clans.api.models.Clan;
import me.loving11ish.clans.api.models.ClanPlayer;
import me.loving11ish.clansliteapitest.ClansLiteAPITest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Test implements CommandExecutor {

    private final FoliaLib foliaLib = ClansLiteAPITest.getFoliaLib();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Check if sender is a player
        if (sender instanceof Player) {
            // Get player and cast to Player object
            Player player = (Player) sender;

            // Check if ClansLiteAPI is loaded
            if (ClansLiteAPITest.getPlugin().getClansLiteAPI() == null) {
                player.sendMessage("ClansLiteAPI not found!");
                return true;
            }

            // Get ClansLiteAPI instance
            ClansLiteAPI clansLiteAPI = ClansLiteAPITest.getPlugin().getClansLiteAPI();

            // Run task at player
            foliaLib.getImpl().runAtEntity(player, (task) -> {
                player.sendMessage("ClansLiteAPI found!");

                // See if player is in a clan, and if so, get the clan
                if (clansLiteAPI.getClanByBukkitPlayer(player) != null) {
                    player.sendMessage("You are in a clan!");
                    Clan clan = clansLiteAPI.getClanByBukkitPlayer(player);
                    player.sendMessage("Your clan: " + clan.getClanFinalName());
                } else {
                    player.sendMessage("You are not in a clan!");
                }

                // Get top clans
                if (clansLiteAPI.getTopClansByClanPointsCache() != null) {
                    List<Clan> topClans = clansLiteAPI.getTopClansByClanPointsCache();
                    for (Clan clan : topClans) {
                        player.sendMessage(clan.getClanFinalName() + " - " + clan.getClanPoints() + " points.");
                    }
                }

                // Get top clan players
                if (clansLiteAPI.getTopClanPlayersByPlayerPointsCache() != null) {
                    List<ClanPlayer> topClanPlayers = clansLiteAPI.getTopClanPlayersByPlayerPointsCache();
                    for (ClanPlayer clanPlayer : topClanPlayers) {
                        player.sendMessage(clanPlayer.getLastPlayerName() + " - " + clanPlayer.getPointBalance() + " points.");
                        player.sendMessage(clanPlayer.getLastPlayerName() + " - " + clanPlayer.getJavaUUID());
                    }
                }


            });

        // If sender is console, let them know they must be a player
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You must be a player to use this command!");
        }

        return true;
    }
}
