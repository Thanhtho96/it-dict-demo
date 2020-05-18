package com.tt.it_dictionary.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
class Word(
    val en: String,
    val vn: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
