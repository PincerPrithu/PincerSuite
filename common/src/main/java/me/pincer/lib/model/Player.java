package me.pincer.lib.model;

import java.util.UUID;

public interface Player {

    UUID getUniqueId();

    String getName();

    boolean isOnline();

    String getServerName();  // Returns the server name if applicable, else null.

}
