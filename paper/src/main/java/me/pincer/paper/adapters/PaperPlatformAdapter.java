package me.pincer.paper.adapters;

import me.pincer.lib.adapter.PlatformAdapter;

import java.util.UUID;
import java.util.function.BooleanSupplier;

public class PaperPlatformAdapter implements PlatformAdapter {
    @Override
    public me.pincer.lib.model.Player convertPlayer(Object platformPlayer) {
        org.bukkit.entity.Player bukkitPlayer = (org.bukkit.entity.Player) platformPlayer;
        return new CommonPlayer(
            bukkitPlayer.getUniqueId(),
            bukkitPlayer.getName(),
            bukkitPlayer::isOnline
        );
    }

    private static class CommonPlayer implements me.pincer.lib.model.Player {
        private final UUID uuid;
        private final String name;
        private final BooleanSupplier onlineCheck;

        CommonPlayer(UUID uuid, String username, BooleanSupplier onlineCheck) {
            this.uuid = uuid;
            this.name = username;
            this.onlineCheck = onlineCheck;
        }

        @Override public UUID getUniqueId() { return uuid; }
        @Override public String getName() { return name; }
        @Override public boolean isOnline() { return onlineCheck.getAsBoolean(); }

        @Override
        public String getServerName() {
            return "PaperServer"; // Placeholder, Paper doesn't have a direct server name API
        }
    }
}
