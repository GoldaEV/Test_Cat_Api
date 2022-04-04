package com.golda.test.cats.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.usecases.DeleteFavoriteUseCase
import com.golda.test.cats.usecases.GetFavoriteCatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class FavoriteViewModel @Inject constructor(
    private val getFavoriteCatUseCase: Provider<GetFavoriteCatUseCase>,
    private val deleteFavoriteUseCase: Provider<DeleteFavoriteUseCase>
) : ViewModel() {

    private val _result = MutableSharedFlow<Boolean>()
    val result = _result.asSharedFlow()


    val catsList: Flow<List<Cat>> = getFavoriteCatUseCase.get()()


    fun deleteFavoriteCat(cat: Cat) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavoriteUseCase.get()(cat)
            _result.emit(true)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val viewModerProvider: Provider<FavoriteViewModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == FavoriteViewModel::class.java)
            return viewModerProvider.get() as T
        }
    }
}
