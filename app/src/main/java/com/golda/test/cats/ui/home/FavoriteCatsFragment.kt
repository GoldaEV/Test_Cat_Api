package com.golda.test.cats.ui.home

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import by.kirich1409.viewbindingdelegate.viewBinding
import com.golda.test.cats.*
import com.golda.test.cats.databinding.FragmentFavoritescatsBinding
import com.golda.test.cats.utils.recycler.MultiBindingAdapter
import com.golda.test.cats.utils.recycler.favoriteCatAdapter
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Provider


class FavoriteCatsFragment : Fragment(R.layout.fragment_favoritescats) {

    @Inject
    lateinit var viewModeProvider: Provider<FavoriteViewModel.Factory>

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val viewBinding by viewBinding(FragmentFavoritescatsBinding::bind)
    private val viewModel: FavoriteViewModel by viewModels { viewModeProvider.get() }

    private val catAdapter by lazy {
        MultiBindingAdapter(
            favoriteCatAdapter({

            }, {
                viewModel.deleteFavoriteCat(it)
            }, {
                saveImage(it)
            })
        )
    }


    private fun saveImage(drawable: Drawable) {
        checkPermissionAndDownloadBitmap(drawable)
    }

    private fun checkPermissionAndDownloadBitmap(drawable: Drawable) {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                try {
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    saveMediaToStorage(bitmap)
                } catch (e: Exception) {
                    context?.toast(e.message)
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                context?.showPermissionRequestDialog(
                    getString(R.string.permission_title),
                    getString(R.string.write_permission_request)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

            }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireActivity().contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            context?.toast("Saved to Photos")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            rvCat.adapter = catAdapter

        }

        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.catsList
                .collect {
                    catAdapter.items = it
                }
        }
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.result
                .collect {
                    viewBinding.content.showSnackBar("Removed from favorites")
                }
        }

        setPermissionCallback()
    }

}