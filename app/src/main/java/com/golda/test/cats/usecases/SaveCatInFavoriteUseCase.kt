package com.golda.test.cats.usecases

import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.data.network.model.toData
import com.golda.test.cats.data.room.FavoriteDao
import javax.inject.Inject

class SaveCatInFavoriteUseCase @Inject constructor(private val dao: FavoriteDao) {

    operator fun invoke(cat: Cat): Long {
        return dao.add(cat.toData())
    }
}