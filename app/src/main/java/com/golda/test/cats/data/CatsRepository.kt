package com.golda.test.cats.data

import androidx.paging.PagingSource
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.data.network.AllCatsPagingSource
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val allCatsPagingSourceFactory: AllCatsPagingSource.Factory
) {

    fun queryListCatFromServer(): PagingSource<Int, Cat> {
        return allCatsPagingSourceFactory.create()
    }
}