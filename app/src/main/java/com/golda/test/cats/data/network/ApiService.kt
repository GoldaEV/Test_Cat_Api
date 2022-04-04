package com.golda.test.cats.data.network

import com.golda.test.cats.data.network.model.ResponseCat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/images/search")
    suspend fun getListCats(
        @Query("page") page: Int,
        @Query("limit") pageSize: Int = 10,
        @Query("order") order: String = "DESC"
    ): Response<List<ResponseCat>>


    companion object {
        const val MAX_PAGE_SIZE = 20
    }
}