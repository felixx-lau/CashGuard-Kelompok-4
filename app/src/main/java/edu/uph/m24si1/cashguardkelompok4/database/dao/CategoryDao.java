package edu.uph.m24si1.cashguardkelompok4.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import edu.uph.m24si1.cashguardkelompok4.database.entity.Category;

@Dao
public interface CategoryDao {

    @Insert
    long insert(Category category);

    @Delete
    void delete(Category category);

    // Ambil semua kategori
    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    // Kategori berdasarkan tipe (INCOME / EXPENSE)
    @Query("SELECT * FROM categories WHERE category_type = :type")
    LiveData<List<Category>> getCategoriesByType(String type);

    // Kategori berdasarkan scope (USAHA / PRIBADI / BOTH)
    @Query("SELECT * FROM categories WHERE wallet_scope = :scope OR wallet_scope = 'BOTH'")
    LiveData<List<Category>> getCategoriesByScope(String scope);
}
