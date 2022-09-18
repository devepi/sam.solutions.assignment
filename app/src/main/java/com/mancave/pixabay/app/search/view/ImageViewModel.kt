package com.mancave.pixabay.app.search.view

import com.mancave.pixabay.core.model.Image

data class ImageViewModel(
    val image: Image,
    val onImageClicked: (Image) -> Unit
) {
    fun clicked() {
        onImageClicked(image)
    }
}
