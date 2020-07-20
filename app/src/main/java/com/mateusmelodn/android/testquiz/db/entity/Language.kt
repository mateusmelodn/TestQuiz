package com.mateusmelodn.android.testquiz.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Language(
    @PrimaryKey @ColumnInfo(name = "title") val title: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString()
    )

    fun isTitleValid(): Boolean {
        return title.isNotEmpty()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Language> {
        override fun createFromParcel(parcel: Parcel): Language {
            return Language(parcel)
        }

        override fun newArray(size: Int): Array<Language?> {
            return arrayOfNulls(size)
        }
    }
}