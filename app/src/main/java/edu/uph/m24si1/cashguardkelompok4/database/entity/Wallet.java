package edu.uph.m24si1.cashguardkelompok4.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
        tableName = "wallets",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Wallet {

    // Tipe dompet — hanya dua jenis di CashGuard
    public enum WalletType {
        USAHA,    // Dana usaha/bisnis
        PRIBADI   // Dana pribadi
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    public int walletId;

    @ColumnInfo(name = "user_id", index = true)
    public int userId;

    @ColumnInfo(name = "wallet_name")
    public String walletName;

    @ColumnInfo(name = "wallet_type")
    public String walletType; // "USAHA" atau "PRIBADI"

    @ColumnInfo(name = "balance")
    public double balance;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    // Constructor
    public Wallet(int userId, String walletName, WalletType walletType, double balance) {
        this.userId = userId;
        this.walletName = walletName;
        this.walletType = walletType.name();
        this.balance = balance;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters
    public int getWalletId() { return walletId; }
    public int getUserId() { return userId; }
    public String getWalletName() { return walletName; }
    public String getWalletType() { return walletType; }
    public double getBalance() { return balance; }
    public long getCreatedAt() { return createdAt; }

    // Helper: cek apakah dompet ini milik usaha
    public boolean isUsaha() {
        return WalletType.USAHA.name().equals(walletType);
    }
}
