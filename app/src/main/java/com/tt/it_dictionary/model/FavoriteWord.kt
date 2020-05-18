package com.tt.it_dictionary.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "favorite_word", primaryKeys = ["word_id"])
class FavoriteWord(@ColumnInfo(name = "word_id") val wordId: Int)