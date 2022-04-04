package com.golda.test.cats.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.golda.test.cats.R
import com.golda.test.cats.data.model.Cat
import com.golda.test.cats.databinding.ItemRvCatBinding

class CatsAdapter(context: Context, val callback: Callback?) :
    PagingDataAdapter<Cat, CatsAdapter.CatViewHolder>(ArticleDiffItemCallback) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder(layoutInflater.inflate(R.layout.item_rv_cat, parent, false))
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding by viewBinding(ItemRvCatBinding::bind)

        fun bind(cat: Cat?) {
            with(viewBinding) {
                cbInFavorite.isChecked = cat?.inFavorite ?: false
                image.load(cat?.url) {
                    placeholder(ColorDrawable(Color.TRANSPARENT))
                }
                title.text = cat?.name
                cbInFavorite.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        callback?.addToFavorite(cat!!)
                    } else {
                        callback?.removeToFavorite(cat!!)
                    }
                }
                image.setOnLongClickListener {
                    callback?.savePicture(image.drawable.toBitmap())
                    true
                }
            }
        }
    }
}

interface Callback {
    fun addToFavorite(cat: Cat)
    fun removeToFavorite(cat: Cat)
    fun savePicture(cat: Bitmap)
}

private object ArticleDiffItemCallback : DiffUtil.ItemCallback<Cat>() {

    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.idString == newItem.idString && oldItem.url == newItem.url
    }
}