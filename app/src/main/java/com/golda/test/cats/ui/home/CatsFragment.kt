package com.golda.test.cats.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.golda.test.cats.R
import com.golda.test.cats.appComponent
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.databinding.ActivityHomeBinding
import com.golda.test.cats.databinding.FragmentCatsBinding
import com.golda.test.cats.showSnackBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Provider

class CatsFragment : Fragment(R.layout.fragment_cats), Callback {

    @Inject
    lateinit var viewModeProvider: Provider<HomeViewModel.Factory>

    private val viewBinding by viewBinding(FragmentCatsBinding::bind)
    private val viewModel: HomeViewModel by viewModels { viewModeProvider.get() }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CatsAdapter(requireContext(), this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            rvCat.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CatsLoaderStateAdapter(),
                footer = CatsLoaderStateAdapter()
            )
            btnFav.setOnClickListener {
                findNavController().navigate(R.id.action_catsFragment_to_favoriteCatsFragment)
            }
        }

        adapter.addLoadStateListener { state ->
            with(viewBinding) {
                rvCat.isVisible = state.refresh != LoadState.Loading
                progress.isVisible = state.refresh == LoadState.Loading
            }
        }

        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.catsList
                .collectLatest(adapter::submitData)
        }
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.saveResult
                .collect {
                    viewBinding.content.showSnackBar("Saved in favorite")
                }
        }

    }

    override fun addToFavorite(cat: Cat) {
        viewModel.saveCatInFavorite(cat)
    }

    override fun removeToFavorite(cat: Cat) {
    }

    override fun savePicture(cat: Bitmap) {
    }

}