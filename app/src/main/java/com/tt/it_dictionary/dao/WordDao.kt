package com.tt.it_dictionary.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.model.WordAndFavoriteWord

@Dao
interface WordDao {

    // pass value "string%"
    @Query("SELECT * FROM word WHERE en LIKE :en LIMIT 17")
    fun findByEnglish(en: String): LiveData<List<Word>>

    @Query("SELECT * FROM word w LEFT JOIN favorite_word fw ON w.id = fw.word_id WHERE w.id = :id")
    fun getFavoriteWord(id: Int): LiveData<WordAndFavoriteWord>

    @Query("SELECT w.* FROM word w INNER JOIN favorite_word fw ON word_id = id")
    fun getAllFavoriteWord(): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listWord: List<Word>)
}