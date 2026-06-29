package edu.uph.m24si1.cashguardkelompok4.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.uph.m24si1.cashguardkelompok4.database.CashGuardDatabase;
import edu.uph.m24si1.cashguardkelompok4.database.dao.TransactionDao;
import edu.uph.m24si1.cashguardkelompok4.database.dao.WalletDao;
import edu.uph.m24si1.cashguardkelompok4.database.entity.Transaction;
import edu.uph.m24si1.cashguardkelompok4.database.entity.Wallet;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final WalletDao walletDao;

    public TransactionRepository(Application application) {
        CashGuardDatabase db = CashGuardDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        walletDao = db.walletDao();
    }

    // ── Insert Transaksi ───────────────────────────────────────

    // Insert pemasukan atau pengeluaran biasa
    public void insertTransaction(Transaction transaction) {
        CashGuardDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.insert(transaction);

            // Update saldo dompet otomatis
            if (transaction.getTransactionType().equals(Transaction.TransactionType.INCOME.name())) {
                walletDao.addBalance(transaction.getWalletId(), transaction.getAmount());
            } else if (transaction.getTransactionType().equals(Transaction.TransactionType.EXPENSE.name())) {
                walletDao.deductBalance(transaction.getWalletId(), transaction.getAmount());
            }
        });
    }

    // Insert transfer antar dompet (usaha ↔ pribadi)
    // Otomatis tandai isLeak jika dari USAHA → PRIBADI
    public void insertTransfer(int fromWalletId, int toWalletId, double amount, String note) {
        CashGuardDatabase.databaseWriteExecutor.execute(() -> {
            // Cek tipe dompet asal
            double fromWalletBalance = walletDao.getBalance(fromWalletId);
            Wallet fromWallet = null; // ambil dari DB langsung (non-LiveData)

            // Deteksi apakah ini kebocoran (dari USAHA ke PRIBADI)
            // Cara sederhana: kita tandai berdasarkan logika bisnis
            // Untuk implementasi lengkap, ambil wallet object dulu
            boolean isLeak = detectLeak(fromWalletId, toWalletId);

            Transaction transfer = new Transaction(
                    fromWalletId, toWalletId, amount, note,
                    System.currentTimeMillis(), isLeak
            );

            transactionDao.insert(transfer);
            walletDao.deductBalance(fromWalletId, amount);
            walletDao.addBalance(toWalletId, amount);
        });
    }

    // Deteksi kebocoran: jika dompet asal = USAHA dan tujuan = PRIBADI
    private boolean detectLeak(int fromWalletId, int toWalletId) {
        // Implementasi: query wallet_type dari DB
        // Untuk kesederhanaan kita bisa simpan pemetaan walletId→type di SharedPreferences
        // atau query langsung seperti ini:
        // String fromType = walletDao.getWalletType(fromWalletId);
        // String toType   = walletDao.getWalletType(toWalletId);
        // return fromType.equals("USAHA") && toType.equals("PRIBADI");

        // Placeholder — implementasikan sesuai kebutuhan
        return false;
    }

    // ── Query ──────────────────────────────────────────────────

    public LiveData<List<Transaction>> getTransactionsByWallet(int walletId) {
        return transactionDao.getTransactionsByWallet(walletId);
    }

    public LiveData<List<Transaction>> getTransactionsByDateRange(
            int walletId, long startDate, long endDate) {
        return transactionDao.getTransactionsByDateRange(walletId, startDate, endDate);
    }

    public LiveData<List<Transaction>> getRecentTransactions(int walletId) {
        return transactionDao.getRecentTransactions(walletId);
    }

    // ── Laporan Kebocoran ──────────────────────────────────────

    public LiveData<List<Transaction>> getAllLeakTransactions() {
        return transactionDao.getAllLeakTransactions();
    }

    public LiveData<Double> getTotalLeakAmount(long startDate, long endDate) {
        return transactionDao.getTotalLeakAmount(startDate, endDate);
    }

    public LiveData<Integer> getLeakCount(long startDate, long endDate) {
        return transactionDao.getLeakCount(startDate, endDate);
    }

    // ── Summary ────────────────────────────────────────────────

    public LiveData<Double> getTotalIncome(int walletId) {
        return transactionDao.getTotalIncome(walletId);
    }

    public LiveData<Double> getTotalExpense(int walletId) {
        return transactionDao.getTotalExpense(walletId);
    }

    public void deleteTransaction(Transaction transaction) {
        CashGuardDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
    }
}
