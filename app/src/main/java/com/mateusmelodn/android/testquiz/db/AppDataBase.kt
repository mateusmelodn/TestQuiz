package com.mateusmelodn.android.testquiz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mateusmelodn.android.testquiz.db.dao.LanguageDao
import com.mateusmelodn.android.testquiz.db.dao.WordDao
import com.mateusmelodn.android.testquiz.db.entity.Language
import com.mateusmelodn.android.testquiz.db.entity.Word

@Database(entities = [Language::class, Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
    abstract fun wordDao(): WordDao

    object Singleton {
        fun getDBInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, "testquiz-sqlite.db"
            )
                .fallbackToDestructiveMigration() // Used only in development
                .build()
        }
    }

    companion object {
        fun saveLanguage(language: Language, context: Context) {
            val appDataBase = Singleton.getDBInstance(context)
            appDataBase.languageDao().insert(language)
        }

        fun saveWord(word: Word, context: Context) {
            val appDataBase = Singleton.getDBInstance(context)
            appDataBase.wordDao().insert(word)
        }

        fun getAllLanguages(context: Context): List<Language> {
            val appDataBase = Singleton.getDBInstance(context)
            return appDataBase.languageDao().getAll()
        }

        fun getAllWordsFromLanguage(languageTitle: String, context: Context): List<Word> {
            val appDataBase = Singleton.getDBInstance(context)
            return appDataBase.wordDao().getAll(languageTitle)
        }

        fun updateWord(word: Word, context: Context) {
            val appDataBase = Singleton.getDBInstance(context)
            appDataBase.wordDao().update(word)
        }

        fun deleteWord(word: Word, context: Context) {
            val appDataBase = Singleton.getDBInstance(context)
            appDataBase.wordDao().delete(word)
        }
    }
}