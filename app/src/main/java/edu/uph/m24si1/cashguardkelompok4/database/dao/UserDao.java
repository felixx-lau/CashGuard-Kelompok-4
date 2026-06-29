package edu.uph.m24si1.cashguardkelompok4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import edu.uph.m24si1.cashguardkelompok4.database.entity.User;


@Dao
public interface UserDao {

    @Insert
    long insert(User user); // return id user baru

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email); // untuk login, dijalankan di background thread

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int isEmailExists(String email); // cek duplikat email saat register
}