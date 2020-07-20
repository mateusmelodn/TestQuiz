package com.mateusmelodn.android.testquiz.db.dao

import androidx.room.*
import com.mateusmelodn.android.testquiz.db.entity.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word WHERE language_title = :languageTitle")
    fun getAll(languageTitle: String): List<Word>

    @Insert
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Delete
    fun delete(word: Word)

    @Query("DELETE FROM word WHERE language_title = :languageTitle")
    fun deleteAllWords(languageTitle: String)
}
