package com.tt.it_dictionary.repository

import androidx.lifecycle.LiveData
import com.tt.it_dictionary.dao.WordDao
import com.tt.it_dictionary.model.Word

class WordRepository(private val wordDao: WordDao) {

    fun searchByEnglish(en: String): LiveData<List<Word>> {
        return wordDao.findByEnglish(en)
    }

}