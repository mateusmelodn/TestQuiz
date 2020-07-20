package com.mateusmelodn.android.testquiz.db.dao

import androidx.room.*
import com.mateusmelodn.android.testquiz.db.entity.Language

@Dao
interface LanguageDao {
    @Query("SELECT * FROM language")
    fun getAll(): List<Language>

    @Insert
    fun insert(language: Language)

    @Update
    fun update(language: Language)

    @Delete
    fun delete(language: Language)
}