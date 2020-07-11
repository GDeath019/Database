package com.example.apprealm.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    // getAll data from table User
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    long[] insertAll(User... users);

    @Delete
    int delete(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    User findUser(int id);

    @Update
    int update(User user);
}

