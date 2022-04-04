package com.golda.test.cats.data.network.model

import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.data.room.model.CatData


fun ResponseCat.toCat() = Cat(
    name = this.breeds?.firstOrNull()?.name,
    idString = id,
    url = url
)

fun CatData.toCat() = Cat(
    idString = idString,
    name = name,
    url = url,
    inFavorite = true
)

fun Cat.toData() = CatData(
    id = 0,
    name = name,
    idString = idString,
    url = url
)