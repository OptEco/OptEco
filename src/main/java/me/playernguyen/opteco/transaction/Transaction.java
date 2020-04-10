package me.playernguyen.opteco.transaction;

import me.playernguyen.opteco.OptEcoImplementation;
import me.playernguyen.opteco.event.OptEcoPlayerPendingEvent;
import me.playernguyen.opteco.event.OptEcoPlayerReceivedEvent;
import me.playernguyen.opteco.event.OptEcoTransactionChangeStateEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Transaction extends OptEcoImplementation {

    private final long DELAY = 20L;
    private final long PERIOD = 20L;

    private final UUID player;
    private final UUID target;
    private final Double amount;
    private final BukkitRunnable runnable;
    private TransactionState state;
    private String id;

    public Transaction (UUID player, UUID target, Double amount, BukkitRunnable runnable) {
        this.player = player;
        this.target = target;
        this.amount = amount;
        this.runnable = runnable;

        // Generate random id
        this.id = String.valueOf(Math.random() * Integer.MAX_VALUE);
        // Init with pending state
        this.setState(TransactionState.PENDING);
        // Call an async task to count down
        this.runnable.runTaskTimerAsynchronously(getPlugin(), this.DELAY, this.PERIOD);
        // Call the event
        OptEcoPlayerPendingEvent e = new OptEcoPlayerPendingEvent(getPlayer(), this);
        Bukkit.getServer().getPluginManager().callEvent(e);
        // Debugger log out
        getPlugin().getDebugger().info(String.format("generated the transaction id #%s", getId()));
    }

    public String getId() {
        return id;
    }

    public UUID getTarget() {
        return target;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    public UUID getPlayer() {
        return player;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), ()-> {
            this.state = state;
            // Call the event without async error
            Bukkit.getScheduler().runTask(getPlugin(),
                    () -> Bukkit.getPluginManager().callEvent(new OptEcoTransactionChangeStateEvent(this)));
        });
    }

    /**
     * Confirm the transaction
     * @throws IllegalStateException Throw while the state are not pending
     * @return Is confirm or not
     */
    public boolean confirm() {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), ()-> {
            if (!getState().equals(TransactionState.PENDING)) {
                throw new IllegalStateException("Cannot accept non-pending transaction!");
            }
            // Call received listener
            OptEcoPlayerReceivedEvent event =
                    new OptEcoPlayerReceivedEvent(this);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // Change state
            this.setState(TransactionState.CONFIRMED);
        });
        // Return such as ...
        return getPlugin().getAccountManager().takeBalance(getPlayer(), getAmount())
        && getPlugin().getAccountManager().addBalance(getTarget(), getAmount())
        && this.clean();
    }

    /***
     * Set cancel to the transaction
     * @throws IllegalStateException Throw while the state are not pending
     * @return Is cancel (clean up or not)
     */
    public boolean cancel() {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), ()-> {
            if (!getState().equals(TransactionState.PENDING)) {
                throw new IllegalStateException("Cannot cancel non-pending transaction!");
            }
            // Change state
            this.setState(TransactionState.CANCELLED);
        });
        // Clean
        return this.clean();
    }

    /**
     * Clean up the transaction information
     * @return Is clean up or not
     */
    public boolean clean() {
        // Cancel runnable
        this.runnable.cancel();
        // No memory leak here :D
        return getPlugin().getTransactionManager().removeTransaction(getPlayer());
    }
}
