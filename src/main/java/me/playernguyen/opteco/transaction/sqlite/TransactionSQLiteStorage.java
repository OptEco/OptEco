package me.playernguyen.opteco.transaction.sqlite;

import me.playernguyen.opteco.OptEco;
import me.playernguyen.opteco.OptEcoConfiguration;
import me.playernguyen.opteco.sql.SQLiteEstablish;
import me.playernguyen.opteco.transaction.TransactionSQL;

import java.util.ArrayList;
import java.util.Arrays;

public class TransactionSQLiteStorage extends TransactionSQL {

    public static final String TABLE =
            OptEco.getInstance().getConfigurationLoader().getString(OptEcoConfiguration.SQL_TRANSACT_TABLE_NAME);

    public static final ArrayList<String> TRANSACTION_INIT =
            new ArrayList<>(
                    Arrays.asList(
                            "id INT(32) NOT NULL AUTO_INCREMENT PRIMARY KEY",
                            "transaction_id VARCHAR(255) NOT NULL",
                            "sender VARCHAR(255) NOT NULL",
                            "receiver VARCHAR(255) NOT NULL",
                            "amount REAL NOT NULL",
                            "state VARCHAR(255) NOT NULL",
                            "time VARCHAR(255) NOT NULL"
                    )
            );

    public TransactionSQLiteStorage() {
        super(new SQLiteEstablish(TABLE, TRANSACTION_INIT));
    }


}
