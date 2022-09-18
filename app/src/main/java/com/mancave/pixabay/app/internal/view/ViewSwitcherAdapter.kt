package com.mancave.pixabay.app.internal.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import me.tatarka.bindingcollectionadapter2.ItemBinding

internal class ViewSwitcherAdapter<T>(private val itemBinding: ItemBinding<T>) {

    private var currentBinding: ViewDataBinding? = null
    private var compareString: String? = null

    fun attach(
        context: Context,
        lifecycleOwner: LifecycleOwner?,
        onSwitchView: (view: View?) -> (Unit)
    ) {
        this.context = context
        this.onSwitchView = onSwitchView
        this.lifecycleOwner = lifecycleOwner

        trySwitchView()
    }

    var item: T? = null
        set(value) {
            synchronized(this) {
                field = value
                trySwitchView()
            }
        }

    private var context: Context? = null

    private var lifecycleOwner: LifecycleOwner? = null

    private var onSwitchView: ((view: View?) -> (Unit))? = null

    private fun trySwitchView() {
        context?.let { context ->
            onSwitchView?.let { onSwitchView ->
                item?.let { item ->
                    switchView(
                        itemBinding = itemBinding,
                        item = item,
                        context = context,
                        onSwitchView = onSwitchView
                    )
                } ?: clearView(onSwitchView)
            }
        }
    }

    private fun clearView(onSwitchView: (view: View?) -> Unit) {
        itemBinding.set(0, 0)
        currentBinding = null
        onSwitchView.invoke(null)
    }

    private fun switchView(
        itemBinding: ItemBinding<T>,
        item: T,
        context: Context,
        onSwitchView: (view: View?) -> Unit
    ) {
        val currentLayoutRes = itemBinding.layoutRes()
        itemBinding.onItemBind(0, item)

        var inflate = currentLayoutRes != itemBinding.layoutRes()
        var bind = true
        if (!inflate) {
            if (item is ModuleDiffInterface) {
                if (item.compareString() != compareString) {
                    inflate = true
                    bind = true
                }
            } else if (item is UIDiffInterface) {
                if (item.id != compareString) {
                    inflate = true
                    bind = true
                }
            }
        }

        if (inflate) {
            currentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                itemBinding.layoutRes(),
                null,
                false
            )
        }

        currentBinding?.let { binding ->
            if (bind) {
                binding.setVariable(itemBinding.variableId(), item)
                binding.lifecycleOwner = lifecycleOwner
                binding.executePendingBindings()
            }

            if (inflate) {
                onSwitchView.invoke(binding.root)
            }
        }

        compareString = when (item) {
            is ModuleDiffInterface -> item.compareString()
            is UIDiffInterface -> item.id
            else -> null
        }
    }
}