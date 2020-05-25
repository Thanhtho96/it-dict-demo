package com.tt.it_dictionary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tt.it_dictionary.WordRoomDatabase
import com.tt.it_dictionary.model.FavoriteWord
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.model.WordAndFavoriteWord
import com.tt.it_dictionary.repository.FavoriteWordRepository
import com.tt.it_dictionary.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val wordRepository: WordRepository
    private val favoriteWordRepository: FavoriteWordRepository
    var wordFoundResult: LiveData<List<Word>> = MutableLiveData()
    var favoriteWord: LiveData<WordAndFavoriteWord> = MutableLiveData()

    init {
        val wordRoomDatabase =
            WordRoomDatabase.getDatabase(
                application,
                viewModelScope
            )
        wordRepository = WordRepository(wordRoomDatabase.wordDao())
        favoriteWordRepository = FavoriteWordRepository(wordRoomDatabase.favoriteWordDao())

    }

    fun findWordByEnglish(en: String) {
        wordFoundResult = wordRepository.searchByEnglish(en)
    }

    fun findFavoriteWord(id: Int) {
        favoriteWord = wordRepository.getFavoriteWord(id)
    }

    fun insertFavoriteWord(favoriteWord: FavoriteWord) = viewModelScope.launch(Dispatchers.IO) {
        favoriteWordRepository.insertFavoriteWord(favoriteWord)
    }

    fun deleteFavoriteWord(favoriteWord: FavoriteWord) = viewModelScope.launch(Dispatchers.IO) {
        favoriteWordRepository.deleteFavoriteWord(favoriteWord)
    }

    fun getAllFavoriteWord() {
        wordFoundResult = wordRepository.getAllFavoriteWord()
    }
}