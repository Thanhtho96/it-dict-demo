package com.tt.it_dictionary.model

import androidx.room.Entity

@Entity(tableName = "word", primaryKeys = ["en"])
class Word(
    val en: String,
    val vn: String
)
