package com.tt.it_dictionary.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.tt.it_dictionary.model.FavoriteWord

@Dao
interface FavoriteWordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: FavoriteWord)

    @Delete
    suspend fun delete(word: FavoriteWord)
}