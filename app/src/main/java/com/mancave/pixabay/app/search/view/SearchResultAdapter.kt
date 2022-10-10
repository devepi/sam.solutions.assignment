package com.mancave.pixabay.app.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mancave.pixabay.app.R
import com.mancave.pixabay.app.databinding.ImageRowBinding

class SearchResultAdapter
  : PagingDataAdapter<ImageViewModel, ImageViewHolder>(Comparator) {

  override fun onBindViewHolder(
    holder: ImageViewHolder,
    position: Int
  ) {
    getItem(position)
      ?.let {
        holder.bind(it)
      }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ImageViewHolder =
    ImageViewHolder(
      inflate(
        LayoutInflater.from(parent.context),
        R.layout.image_row,
        parent,
        false
      )
    )

  object Comparator : DiffUtil.ItemCallback<ImageViewModel>() {
    override fun areItemsTheSame(
      oldItem: ImageViewModel,
      newItem: ImageViewModel
    ): Boolean = oldItem.image.id == newItem.image.id

    override fun areContentsTheSame(
      oldItem: ImageViewModel,
      newItem: ImageViewModel
    ): Boolean = oldItem == newItem
  }
}

class ImageViewHolder(
  private val binding: ImageRowBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(viewModel: ImageViewModel) {
    binding.viewModel = viewModel
    binding.executePendingBindings()
  }
}
