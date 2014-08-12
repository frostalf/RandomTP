package com.atomic.rtp.Handlers;

import com.atomic.rtp.RandomTeleport;
import com.iCo6.system.Account;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 05, 2014 *
 */
public class VaultHandler {
    RandomTeleport plugin;
    Player player;
    Account account;
    double amountRemoved;

    public VaultHandler(RandomTeleport plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        account = getAccount(player);
    }


    public void remove(double amount) {
        account.getHoldings().subtract(amount);
        amountRemoved = amount;
    }

    public boolean check(double amount) {
       return account.getHoldings().hasEnough(amount);
    }

    public void add(double amount) {
        account.getHoldings().add(amount);
    }

    public double getHoldings() {
        return account.getHoldings().getBalance();
    }

    public String getMoneyMessage() {
        String r1 = ChatColor.AQUA + "" + amountRemoved + ChatColor.DARK_AQUA + " was removed from your account for teleporting!";
        String r2 = ChatColor.DARK_AQUA + "You have " + ChatColor.AQUA + getHoldings() + ChatColor.DARK_AQUA + " left in your account";
        return r1 + "\n" + r2;
    }

    protected Account getAccount(Player player) {
        Account account = new Account(player.getName());
        return account;
    }
}
