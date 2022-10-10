package com.mancave.pixabay.app.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mancave.pixabay.app.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment() {

  private val viewModel: SearchViewModel by viewModels()

  private val adapter = SearchResultAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = FragmentSearchBinding.inflate(inflater, container, false)
    .apply {
      this.lifecycleOwner = viewLifecycleOwner
      this.adapter = this@SearchFragment.adapter
      this.viewModel = this@SearchFragment.viewModel
    }
    .root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    lifecycleScope
      .launchWhenStarted {
        viewModel
          .items
          .collectLatest {
            adapter.submitData(it)
          }
      }

    viewModel.onImageSelected = {
      findNavController().navigate(SearchFragmentDirections.showDetails(it))
    }
  }
}
