package com.mancave.pixabay.app.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mancave.pixabay.core.model.Image
import java.text.DecimalFormat

class DetailsViewModel(
    private val image: Image
): ViewModel() {

    private val formatter = DecimalFormat("#,###")

    val user: String by lazy { "user: ${image.user}" }
    val tags: String by lazy { "tags: ${image.tags}" }
    val imageUrl: String by lazy { image.largeImage }
    val likes: String by lazy { "likes: ${formatter.format(image.likes)}" }
    val downloads: String by lazy { "downloads: ${formatter.format(image.downloads)}" }
    val comments: String by lazy { "comments: ${formatter.format(image.comments)}" }

    class Factory(
        private val image: Image
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(image) as T
        }
    }
}
