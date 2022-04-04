package com.golda.test.cats.usecases

import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.data.room.FavoriteDao
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(private val dao: FavoriteDao) {

    operator fun invoke(cat: Cat): Int {
        return dao.deleteFavorite(cat.idString!!)
    }
}