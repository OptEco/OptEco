package me.playernguyen.opteco.api;

import me.playernguyen.opteco.OptEco;
import me.playernguyen.opteco.OptEcoConfiguration;

import java.util.UUID;

public class OptEcoAPI implements IOptEcoAPI {

    private final UUID player;
    private final OptEco plugin;

    /**
     * Get the OptEco API
     *
     * @param uuid who you want to get?
     */
    public OptEcoAPI(UUID uuid) {
        this.player = uuid;
        this.plugin = OptEco.getPlugin();
    }

    /**
     * Check whether the account that exist or not
     *
     * @return boolean has this account on database or not
     */
    @Override
    public boolean hasAccount() {
        return this.plugin
                .getAccountManager().hasAccount(player);
    }

    /**
     * Set the balance of player
     *
     * @param amount amount you want to set
     * @return boolean is set or not
     */
    @Override
    public boolean setPoints(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must greater than 0");
        return this.plugin
                .getAccountManager().setBalance(player, amount);
    }

    /**
     * Get balance/points of player
     *
     * @return double balance of that player
     */
    @Override
    public double getPoints() {
        return this.plugin
                .getAccountManager().getBalance(player);
    }

    /**
     * Check whether player is on pending transaction or not
     *
     * @return boolean is pending or not
     */
    @Override
    public boolean isPending() {
        return this.plugin
                .getTransactionManager().hasTransaction(player);
    }

    /**
     * API Player interactive
     *
     * @return Player
     */
    @Override
    public UUID getPlayer() {
        return this.player;
    }

    /**
     * Add points into player's account
     *
     * @param amount how much
     * @return is added or not
     */
    @Override
    public boolean addPoints(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must greater than 0");
        return this.plugin.getAccountManager().addBalance(getPlayer(), amount);
    }

    /**
     * Take points out of player's account
     *
     * @param amount how much
     * @return is taken or not
     */
    @Override
    public boolean takePoints(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must greater than 0");
        return this.plugin.getAccountManager().takeBalance(getPlayer(), amount);
    }

    /**
     * Get the currency symbol
     *
     * @return String currency symbol
     */
    @Override
    public String getCurrencySymbol() {
        return this.plugin.getConfigurationLoader().getString(OptEcoConfiguration.CURRENCY_SYMBOL);
    }
}
