package edu.uph.m24si1.cashguardkelompok4.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "categories")
public class Category {

    public enum CategoryType {
        INCOME,   // Pemasukan
        EXPENSE   // Pengeluaran
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "name")
    public String name;

    // Contoh: INCOME atau EXPENSE
    @ColumnInfo(name = "category_type")
    public String categoryType;

    // Contoh: "USAHA", "PRIBADI", atau "BOTH"
    @ColumnInfo(name = "wallet_scope")
    public String walletScope;

    @ColumnInfo(name = "icon_name")
    public String iconName; // nama icon drawable

    // Constructor
    public Category(String name, CategoryType categoryType, String walletScope, String iconName) {
        this.name = name;
        this.categoryType = categoryType.name();
        this.walletScope = walletScope;
        this.iconName = iconName;
    }

    // Getters
    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getCategoryType() { return categoryType; }
    public String getWalletScope() { return walletScope; }
    public String getIconName() { return iconName; }
}
