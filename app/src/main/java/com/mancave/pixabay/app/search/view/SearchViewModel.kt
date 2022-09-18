package com.mancave.pixabay.app.search.view

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mancave.pixabay.app.BR
import com.mancave.pixabay.app.R
import com.mancave.pixabay.app.search.view.SearchStateViewModel.*
import com.mancave.pixabay.core.model.Image
import com.mancave.pixabay.core.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    val state: ObservableField<Any> = ObservableField(Loading)

    val stateBinding: ItemBinding<Any> = ItemBinding.of(
        OnItemBindClass<Any>()
            .map(
                Loading::class.java,
                BR.viewModel,
                R.layout.search_loading_view
            )
            .map(
                Failure::class.java,
                BR.viewModel,
                R.layout.search_error_view
            )
            .map(
                Success::class.java,
                BR.viewModel,
                R.layout.search_items_view
            )
    )

    var onImageSelected: ((Image) -> Unit)? = null

    init {
        viewModelScope.launch {
            state.set(Loading)
            val response = withContext(Dispatchers.IO) {
                repository.find("fruits")
            }
            response.fold(
                onSuccess = {
                    state.set(
                        Success(
                            images = it.images,
                            onItemSelected = { image ->
                                onImageSelected?.invoke(image)
                            }
                        )
                    )
                },
                onFailure = {
                    state.set(Failure(it))
                }
            )
        }
    }
}
