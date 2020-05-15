package com.tt.it_dictionary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tt.it_dictionary.WordRoomDatabase
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.repository.WordRepository

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    var wordFoundResult: LiveData<List<Word>> = MutableLiveData()

    init {
        val wordsDao = WordRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).wordDao()
        repository = WordRepository(wordsDao)
    }

    fun findWordByEnglish(en: String) {
        wordFoundResult = repository.searchByEnglish(en)
    }

}