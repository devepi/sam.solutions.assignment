package com.mancave.pixabay.app.internal.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
internal fun ImageView.bindImageUrl(imageUrl: String) {
  load(imageUrl) {
    crossfade(true)
  }
}
