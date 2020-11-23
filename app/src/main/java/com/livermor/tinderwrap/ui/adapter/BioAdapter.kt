package com.livermor.tinderwrap.ui.adapter

import android.annotation.SuppressLint
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import com.livermor.tinderwrap.Bio
import com.livermor.tinderwrap.databinding.ItemBioBinding
import com.livermor.tinderwrap.factory.color

class BioAdapter : ViewBindingDelegateAdapter<Bio, ItemBioBinding>(ItemBioBinding::inflate) {

    @SuppressLint("ClickableViewAccessibility")
    override fun ItemBioBinding.onBind(item: Bio) {
        tvBio.text = if (item.text.isBlank()) "no bio" else item.text
        vIndicator.setBackgroundColor(item.type.color())
    }

    override fun isForViewType(item: Any) = item is Bio
    override fun Bio.getItemId() = this
}