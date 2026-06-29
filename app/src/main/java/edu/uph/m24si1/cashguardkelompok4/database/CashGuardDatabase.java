package edu.uph.m24si1.cashguardkelompok4.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uph.m24si1.cashguardkelompok4.database.dao.CategoryDao;
import edu.uph.m24si1.cashguardkelompok4.database.dao.TransactionDao;
import edu.uph.m24si1.cashguardkelompok4.database.dao.UserDao;
import edu.uph.m24si1.cashguardkelompok4.database.dao.WalletDao;
import edu.uph.m24si1.cashguardkelompok4.database.entity.Category;
import edu.uph.m24si1.cashguardkelompok4.database.entity.Transaction;
import edu.uph.m24si1.cashguardkelompok4.database.entity.User;
import edu.uph.m24si1.cashguardkelompok4.database.entity.Wallet;

@Database(
        entities = {
                User.class,
                Wallet.class,
                Category.class,
                Transaction.class
        },
        version = 1,
        exportSchema = false
)
public abstract class CashGuardDatabase extends RoomDatabase {

    // DAOs
    public abstract UserDao userDao();
    public abstract WalletDao walletDao();
    public abstract CategoryDao categoryDao();
    public abstract TransactionDao transactionDao();

    // Singleton instance
    private static volatile CashGuardDatabase INSTANCE;

    // Thread pool untuk operasi database (jangan di main thread)
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    // Callback untuk isi data awal (kategori default)
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate();
            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = INSTANCE.categoryDao();
                insertDefaultCategories(categoryDao);
            });
        }
    };

    // Singleton getter
    public static CashGuardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CashGuardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    CashGuardDatabase.class,
                                    "cashguard_database"
                            )
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Kategori default saat pertama kali install
    private static void insertDefaultCategories(CategoryDao dao) {
        // ── Kategori Usaha ──
        dao.insert(new Category("Penjualan",      Category.CategoryType.INCOME,  "USAHA",   "ic_shop"));
        dao.insert(new Category("Modal",          Category.CategoryType.INCOME,  "USAHA",   "ic_money"));
        dao.insert(new Category("Operasional",    Category.CategoryType.EXPENSE, "USAHA",   "ic_tools"));
        dao.insert(new Category("Bahan Baku",     Category.CategoryType.EXPENSE, "USAHA",   "ic_box"));
        dao.insert(new Category("Gaji Karyawan",  Category.CategoryType.EXPENSE, "USAHA",   "ic_people"));
        dao.insert(new Category("Iklan/Marketing",Category.CategoryType.EXPENSE, "USAHA",   "ic_ads"));

        // ── Kategori Pribadi ──
        dao.insert(new Category("Gaji/Upah",      Category.CategoryType.INCOME,  "PRIBADI", "ic_wallet"));
        dao.insert(new Category("Makanan",         Category.CategoryType.EXPENSE, "PRIBADI", "ic_food"));
        dao.insert(new Category("Transportasi",    Category.CategoryType.EXPENSE, "PRIBADI", "ic_car"));
        dao.insert(new Category("Belanja",         Category.CategoryType.EXPENSE, "PRIBADI", "ic_bag"));
        dao.insert(new Category("Kesehatan",       Category.CategoryType.EXPENSE, "PRIBADI", "ic_health"));
        dao.insert(new Category("Hiburan",         Category.CategoryType.EXPENSE, "PRIBADI", "ic_entertainment"));

        // ── Kategori Umum (BOTH) ──
        dao.insert(new Category("Lainnya",         Category.CategoryType.INCOME,  "BOTH",    "ic_more"));
        dao.insert(new Category("Lainnya",         Category.CategoryType.EXPENSE, "BOTH",    "ic_more"));
    }
}
