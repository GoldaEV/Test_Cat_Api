package com.golda.test.cats.usecases

import androidx.paging.PagingSource
import com.golda.test.cats.data.CatsRepository
import com.golda.test.cats.data.model.Cat
import javax.inject.Inject

class QueryCatsUseCase @Inject constructor(private val repository: CatsRepository) {

    operator fun invoke(): PagingSource<Int, Cat> {
        return repository.queryListCatFromServer()
    }
}