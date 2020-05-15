package com.tt.it_dictionary.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.it_dictionary.model.Word

@Dao
interface WordDao {

    // pass value "string%"
    @Query(
        value = "SELECT * FROM word WHERE en LIKE :en LIMIT 17"
    )
    fun findByEnglish(en: String): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listWord: List<Word>)
}