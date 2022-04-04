package com.golda.test.cats.di

import android.app.Application
import com.golda.test.cats.ui.home.CatsFragment
import com.golda.test.cats.ui.home.FavoriteCatsFragment
import com.golda.test.cats.ui.home.HomeActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(homeActivity: HomeActivity)

    fun inject(fragmentCats: CatsFragment)

    fun inject(favoriteCatsFragment: FavoriteCatsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun create(): AppComponent
    }
}