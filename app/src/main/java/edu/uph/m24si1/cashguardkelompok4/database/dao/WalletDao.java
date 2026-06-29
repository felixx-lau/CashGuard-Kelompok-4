package edu.uph.m24si1.cashguardkelompok4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import edu.uph.m24si1.cashguardkelompok4.database.entity.Wallet;

@Dao
public interface WalletDao {

    @Insert
    long insert(Wallet wallet);

    @Update
    void update(Wallet wallet);

    @Delete
    void delete(Wallet wallet);

    // Ambil semua dompet milik user
    @Query("SELECT * FROM wallets WHERE user_id = :userId")
    LiveData<List<Wallet>> getWalletsByUser(int userId);

    // Ambil dompet usaha milik user
    @Query("SELECT * FROM wallets WHERE user_id = :userId AND wallet_type = 'USAHA' LIMIT 1")
    LiveData<Wallet> getUsahaWallet(int userId);

    // Ambil dompet pribadi milik user
    @Query("SELECT * FROM wallets WHERE user_id = :userId AND wallet_type = 'PRIBADI' LIMIT 1")
    LiveData<Wallet> getPribadiWallet(int userId);

    // Update saldo setelah transaksi
    @Query("UPDATE wallets SET balance = balance + :amount WHERE wallet_id = :walletId")
    void addBalance(int walletId, double amount);

    @Query("UPDATE wallets SET balance = balance - :amount WHERE wallet_id = :walletId")
    void deductBalance(int walletId, double amount);

    // Ambil saldo saat ini (non-LiveData, untuk kalkulasi)
    @Query("SELECT balance FROM wallets WHERE wallet_id = :walletId")
    double getBalance(int walletId);
}
