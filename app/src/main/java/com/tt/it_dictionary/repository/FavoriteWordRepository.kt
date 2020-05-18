package com.tt.it_dictionary.repository

import com.tt.it_dictionary.dao.FavoriteWordDao
import com.tt.it_dictionary.model.FavoriteWord

class FavoriteWordRepository(private val favoriteWordDao: FavoriteWordDao) {

    suspend fun insertFavoriteWord(word: FavoriteWord) {
        favoriteWordDao.insert(word)
    }

    suspend fun deleteFavoriteWord(word: FavoriteWord) {
        favoriteWordDao.delete(word)
    }
}