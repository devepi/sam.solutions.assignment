package com.mancave.pixabay.app.search.view

import com.mancave.pixabay.app.BR
import com.mancave.pixabay.app.R
import com.mancave.pixabay.core.model.Image
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

sealed class SearchStateViewModel {
    object Loading: SearchStateViewModel()

    class Success(
        images: List<Image> = emptyList(),
        val onItemSelected: ((Image) -> Unit)
    ): SearchStateViewModel() {

        val items = images
            .map {
                ImageViewModel(it) { img ->
                    onItemSelected(img)
                }
            }

        val itemBinding: ItemBinding<Any> = ItemBinding.of(
            OnItemBindClass<Any>()
                .map(
                    ImageViewModel::class.java,
                    BR.viewModel,
                    R.layout.image_row
                )
        )
    }

    class Failure(
        val throwable: Throwable
    ): SearchStateViewModel()
}
