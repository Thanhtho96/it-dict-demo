package com.tt.it_dictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tt.it_dictionary.dao.WordDao
import com.tt.it_dictionary.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val context: Context,
        private val scope: CoroutineScope
    ) :
        RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.insertAll(readWordFromAssets(context))
        }

        private fun readWordFromAssets(context: Context): MutableList<Word> {
            var reader: BufferedReader? = null
            val listWord: MutableList<Word> = ArrayList()
            try {
                reader = BufferedReader(
                    InputStreamReader(context.assets.open("it_dictionary.txt"))
                )
                for (word in reader.readLines()) {
                    val eachWord = word.split(": ")
                    listWord.add(
                        Word(
                            eachWord[0],
                            eachWord[1]
                        )
                    )
                }
            } catch (e: IOException) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                    }
                }
            }
            return listWord
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(context, scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

