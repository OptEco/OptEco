package me.playernguyen.opteco.listener;

import me.playernguyen.opteco.account.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class OptEcoPlayerJoinListener extends OptEcoListener {

    /**
     * On player join into the server
     *
     * @param e {@link PlayerJoinEvent} event
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        // If account not found
        if (!getPlugin().getAccountManager().hasAccount(player.getUniqueId())) {
            // Init new account
            getPlugin().getAccountManager().save(new Account(player.getUniqueId()));
        }
    }

}
