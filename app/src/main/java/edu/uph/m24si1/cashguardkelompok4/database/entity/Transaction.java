package edu.uph.m24si1.cashguardkelompok4.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(
        tableName = "transactions",
        foreignKeys = {
                @ForeignKey(
                        entity = Wallet.class,
                        parentColumns = "wallet_id",
                        childColumns = "wallet_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "category_id",
                        onDelete = ForeignKey.SET_NULL
                )
        }
)
public class Transaction {

    public enum TransactionType {
        INCOME,   // Pemasukan
        EXPENSE,  // Pengeluaran
        TRANSFER  // Transfer antar dompet (usaha ↔ pribadi) — ini yang jadi "kebocoran"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    public int transactionId;

    @ColumnInfo(name = "wallet_id", index = true)
    public int walletId;

    @ColumnInfo(name = "category_id", index = true)
    public Integer categoryId; // nullable

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "transaction_type")
    public String transactionType; // "INCOME", "EXPENSE", "TRANSFER"

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "date")
    public long date; // timestamp (millis)

    // Khusus untuk tipe TRANSFER:
    // wallet asal → wallet tujuan (untuk deteksi kebocoran)
    @ColumnInfo(name = "target_wallet_id")
    public Integer targetWalletId; // nullable, hanya diisi jika tipe TRANSFER

    @ColumnInfo(name = "is_leak")
    public boolean isLeak; // true jika transfer dari USAHA → PRIBADI

    @ColumnInfo(name = "created_at")
    public long createdAt;

    // Default constructor for Room
    public Transaction() {}

    // Constructor untuk INCOME/EXPENSE biasa
    @Ignore
    public Transaction(int walletId, Integer categoryId, double amount,
                       TransactionType type, String note, long date) {
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionType = type.name();
        this.note = note;
        this.date = date;
        this.targetWalletId = null;
        this.isLeak = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Constructor untuk TRANSFER antar dompet
    @Ignore
    public Transaction(int fromWalletId, int toWalletId, double amount,
                       String note, long date, boolean isLeak) {
        this.walletId = fromWalletId;
        this.categoryId = null;
        this.amount = amount;
        this.transactionType = TransactionType.TRANSFER.name();
        this.note = note;
        this.date = date;
        this.targetWalletId = toWalletId;
        this.isLeak = isLeak;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public int getWalletId() { return walletId; }
    public Integer getCategoryId() { return categoryId; }
    public double getAmount() { return amount; }
    public String getTransactionType() { return transactionType; }
    public String getNote() { return note; }
    public long getDate() { return date; }
    public Integer getTargetWalletId() { return targetWalletId; }
    public boolean isLeak() { return isLeak; }
    public long getCreatedAt() { return createdAt; }
}