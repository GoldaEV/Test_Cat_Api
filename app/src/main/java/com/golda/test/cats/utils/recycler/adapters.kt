package com.golda.test.cats.utils.recycler

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil.load
import com.golda.test.cats.R
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.databinding.ItemRvCatBinding
import com.golda.test.cats.makeVisible

fun favoriteCatAdapter(
    addToFavorite: (Cat) -> Unit,
    removeToFavorite: (Cat) -> Unit,
    savePicture: (Drawable) -> Unit
) = viewBinder<Cat, ItemRvCatBinding>(
    binder = ItemRvCatBinding::bind,
    layoutResId = R.layout.item_rv_cat
) {
    bindView { cat ->
        cbInFavorite.isChecked = cat.inFavorite ?: false

        image.load(cat.url) {
            placeholder(ColorDrawable(Color.TRANSPARENT))
        }
        title.text = cat.name
        cbInFavorite.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                addToFavorite(cat)
            } else {
                removeToFavorite(cat!!)
            }
        }
        ivSaveImage.makeVisible()
        ivSaveImage.setOnClickListener {
            savePicture(image.drawable)
        }
    }
}