package com.tt.it_dictionary.model

import androidx.room.Embedded

data class WordAndFavoriteWord(
    @Embedded val word: Word?,
    @Embedded val favoriteWord: FavoriteWord?
)