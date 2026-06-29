package edu.uph.m24si1.cashguardkelompok4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import edu.uph.m24si1.cashguardkelompok4.database.entity.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    long insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    // ── Riwayat transaksi ──────────────────────────────────────

    // Semua transaksi dari satu dompet, diurutkan terbaru
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByWallet(int walletId);

    // Transaksi dalam rentang tanggal
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId " +
            "AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByDateRange(int walletId, long startDate, long endDate);

    // Transaksi berdasarkan kategori
    @Query("SELECT * FROM transactions WHERE category_id = :categoryId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByCategory(int categoryId);

    // ── Laporan Kebocoran (core feature CashGuard) ─────────────

    // Semua transfer dari usaha → pribadi (is_leak = 1)
    @Query("SELECT * FROM transactions WHERE is_leak = 1 ORDER BY date DESC")
    LiveData<List<Transaction>> getAllLeakTransactions();

    // Total kebocoran dalam periode tertentu
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
            "WHERE is_leak = 1 AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalLeakAmount(long startDate, long endDate);

    // Jumlah kejadian kebocoran bulan ini
    @Query("SELECT COUNT(*) FROM transactions " +
            "WHERE is_leak = 1 AND date BETWEEN :startDate AND :endDate")
    LiveData<Integer> getLeakCount(long startDate, long endDate);

    // ── Ringkasan / Summary ────────────────────────────────────

    // Total pemasukan dompet tertentu
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
            "WHERE wallet_id = :walletId AND transaction_type = 'INCOME'")
    LiveData<Double> getTotalIncome(int walletId);

    // Total pengeluaran dompet tertentu
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
            "WHERE wallet_id = :walletId AND transaction_type = 'EXPENSE'")
    LiveData<Double> getTotalExpense(int walletId);

    // Total pemasukan dalam rentang tanggal
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
            "WHERE wallet_id = :walletId AND transaction_type = 'INCOME' " +
            "AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalIncomeInRange(int walletId, long startDate, long endDate);

    // Total pengeluaran dalam rentang tanggal
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
            "WHERE wallet_id = :walletId AND transaction_type = 'EXPENSE' " +
            "AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalExpenseInRange(int walletId, long startDate, long endDate);

    // 5 transaksi terbaru (untuk dashboard)
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY date DESC LIMIT 5")
    LiveData<List<Transaction>> getRecentTransactions(int walletId);
}
