package com.mancave.pixabay.app.internal.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.ViewSwitcher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.internal.managers.ViewComponentManager
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ViewSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewSwitcher(context, attrs) {

    var itemBinding: ItemBinding<Any>? = null
        set(value) {
            if (field == null) {
                field = value
                value?.let { itemBinding ->
                    adapter = ViewSwitcherAdapter(itemBinding)
                }
            }
        }

    var item: Any? = null
        set(value) {
            field = value
            adapter?.item = item
        }

    private var adapter: ViewSwitcherAdapter<Any>? = null
        set(value) {
            field = value
            value?.let { adapter ->
                adapter.attach(
                    onSwitchView = { view ->
                        switchView(view = view)
                    },
                    context = context,
                    lifecycleOwner = findLifecycleOwner()
                )
                adapter.item = item
            }
        }

    private fun switchView(view: View?) {
        if (view != null && childCount >= MAX_CHILD_COUNT) {
            if (currentView != null) {
                removeView(currentView)
            } else {
                removeViewAt(0)
            }
        }
        currentView?.let { currentView ->
            if (view != null) {
                addView(view)
                showNext()
            }

            Handler().post {
                removeView(currentView)
            }
        } ?: run {
            if (view != null) {
                addView(view)
            }
        }
    }

    private fun findLifecycleOwner(): LifecycleOwner? {
        val binding: ViewDataBinding? = DataBindingUtil.findBinding(this)
        var lifecycleOwner: LifecycleOwner? = null
        if (binding != null) {
            lifecycleOwner = binding.lifecycleOwner
        }
        if (lifecycleOwner == null) {
            lifecycleOwner = (context as? LifecycleOwner)
                ?: (context as? ViewComponentManager.FragmentContextWrapper)?.baseContext as? LifecycleOwner
        }
        return lifecycleOwner
    }

    companion object {
        private const val MAX_CHILD_COUNT = 2
    }
}