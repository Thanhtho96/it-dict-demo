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

    @Query("SELECT * FROM word w LEFT JOIN favorite_word fw on w.id = fw.word_id where w.id = :id")
    fun getFavoriteWord(id: Int): LiveData<WordAndFavoriteWord>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listWord: List<Word>)
}