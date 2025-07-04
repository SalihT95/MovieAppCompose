package com.turkoglu.moviecomposeapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.turkoglu.moviecomposeapp.data.paging.SearchPagingSource
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.domain.model.Search
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: MovieAPI) {
    fun multiSearch(queryParam: String): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 27),
            pagingSourceFactory = {
                SearchPagingSource(api, queryParam)
            }
        ).flow
    }
}