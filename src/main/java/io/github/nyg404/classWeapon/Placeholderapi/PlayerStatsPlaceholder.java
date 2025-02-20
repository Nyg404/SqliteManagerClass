package io.github.nyg404.classWeapon.Placeholderapi;

import io.github.nyg404.classWeapon.API.PlayerStatsApi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerStatsPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "classundweapon";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nagust";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.1.2";
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        PlayerStatsApi stats = new PlayerStatsApi(player);

        switch (identifier.toLowerCase()){
            case "level":
                return String.valueOf(stats.getLevel());
            case "oldlevel":
                return String.valueOf(stats.getOldlevel());
            case "currentxp":
                return String.valueOf(stats.getCurrentXp());
            case "requiredxp":
                return String.valueOf(stats.getRequiredXp());
            default:
                return null;
        }

    }
}
