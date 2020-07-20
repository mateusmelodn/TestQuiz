package com.mateusmelodn.android.testquiz.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "language_title") val languageTitle: String, // Linked to Languague Entity, title is PK
    @ColumnInfo(name = "primary_word") var primaryWord: String, // Word inserted by user without translation
    @ColumnInfo(name = "translated_word") var translatedWord: String  // Translated word inserted by user for current primaryWord
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    fun isPrimaryWordValid(): Boolean {
        return primaryWord.isNotEmpty()
    }

    fun isTranslatedWordValid(): Boolean {
        return translatedWord.isNotEmpty()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(languageTitle)
        parcel.writeString(primaryWord)
        parcel.writeString(translatedWord)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }

        fun isTranslatedWordValid(translatedWord: String): Boolean {
            return translatedWord.isNotEmpty()
        }
    }
}