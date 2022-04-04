package com.golda.test.cats.usecases

import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.data.network.model.toCat
import com.golda.test.cats.data.room.FavoriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteCatUseCase @Inject constructor(private val dao: FavoriteDao) {

    operator fun invoke(): Flow<List<Cat>> {
        return dao.getAll()
            .map {
                it.map { it.toCat() }
            }
    }
}