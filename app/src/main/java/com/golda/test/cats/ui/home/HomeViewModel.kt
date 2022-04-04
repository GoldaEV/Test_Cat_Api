package com.golda.test.cats.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.usecases.QueryCatsUseCase
import com.golda.test.cats.usecases.SaveCatInFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class HomeViewModel @Inject constructor(
    private val queryGetCatsUseCaseProvider: Provider<QueryCatsUseCase>,
    private val saveCatInFavoriteUseCase: Provider<SaveCatInFavoriteUseCase>
) : ViewModel() {

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult = _saveResult.asSharedFlow()

    private var catPagingSource: PagingSource<*, *>? = null

    val catsList: StateFlow<PagingData<Cat>> = flowOf(true)
        .distinctUntilChanged()
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private fun newPager(start: Boolean): Pager<Int, Cat> {
        return Pager(PagingConfig(5, enablePlaceholders = false)) {
            queryGetCatsUseCaseProvider.get()().also { catPagingSource = it }
        }
    }

    fun saveCatInFavorite(cat: Cat) {
        viewModelScope.launch(Dispatchers.IO) {
            saveCatInFavoriteUseCase.get()(cat)
            _saveResult.emit(true)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val viewModerProvider: Provider<HomeViewModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == HomeViewModel::class.java)
            return viewModerProvider.get() as T
        }
    }
}
