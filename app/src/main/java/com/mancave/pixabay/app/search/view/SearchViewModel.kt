package com.mancave.pixabay.app.search.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.mancave.pixabay.core.model.Image
import com.mancave.pixabay.core.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
  private val repository: ImageRepository
) : ViewModel() {

  private val queryFlow = MutableStateFlow("fruits")

  var onImageSelected: ((Image) -> Unit)? = null

  @OptIn(FlowPreview::class)
  val items = queryFlow
    .distinctUntilChangedBy { it }
    .debounce(500)
    .flatMapLatest {
      repository.find(it)
    }.map { pagingData ->
      pagingData.map {
        ImageViewModel(it) { img ->
          onImageSelected?.invoke(img)
        }
      }
    }.cachedIn(viewModelScope)

  fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    queryFlow.value = s.toString()
  }
}
