package com.golda.test.cats.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.golda.test.cats.data.network.model.toCat
import com.golda.test.cats.data.model.Cat
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException

class AllCatsPagingSource @AssistedInject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Cat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {

        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize.coerceAtMost(ApiService.MAX_PAGE_SIZE)
            val response = apiService.getListCats(pageNumber, pageSize)

            if (response.isSuccessful) {
                val itemCat = response.body()!!.map { it.toCat() }
                val nextPageNumber = if (itemCat.isEmpty()) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(itemCat, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    @AssistedFactory
    interface Factory {
        fun create(): AllCatsPagingSource
    }

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}