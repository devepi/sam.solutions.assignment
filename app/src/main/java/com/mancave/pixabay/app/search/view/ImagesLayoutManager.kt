package com.mancave.pixabay.app.search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class ImagesLayoutManager(
  context: Context,
  attributeSet: AttributeSet,
  defStyleAttr: Int,
  defStyleRes: Int
) : GridLayoutManager(context, attributeSet, defStyleAttr, defStyleRes) {

  companion object {
    const val COLUMN_WIDTH = 450
  }

  private var columnWidthChanged = true

  private var columnWidth: Int = COLUMN_WIDTH
    set(value) {
      if (value > 0 && value != columnWidth) {
        field = value
        columnWidthChanged = true
      }
    }

  override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
    if (columnWidthChanged && columnWidth > 0) {
      spanCount = calculateSpanCount(columnWidth)
      columnWidthChanged = false
    }
    super.onLayoutChildren(recycler, state)
  }

  private fun calculateSpanCount(columnWidth: Int): Int {
    val totalSpace: Int = if (orientation == VERTICAL) {
      width - paddingRight - paddingLeft
    } else {
      height - paddingTop - paddingBottom
    }
    return 1.coerceAtLeast(totalSpace / columnWidth)
  }
}
