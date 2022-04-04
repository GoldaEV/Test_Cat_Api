package com.golda.test.cats.data.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "CatFavorites")
@Parcelize
class  CatData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String?,
    val idString: String?,
    val url: String?
) : Parcelable