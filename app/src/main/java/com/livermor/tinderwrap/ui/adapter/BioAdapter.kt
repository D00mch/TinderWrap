package com.livermor.tinderwrap.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import com.livermor.tinderwrap.Bio
import com.livermor.tinderwrap.databinding.ItemBioBinding

class BioAdapter : ViewBindingDelegateAdapter<Bio, ItemBioBinding>(ItemBioBinding::inflate) {

    @SuppressLint("ClickableViewAccessibility")
    override fun ItemBioBinding.onBind(item: Bio) {
        tvBio.text = if (item.text.isBlank()) "no bio" else item.text
        vIndicator.setBackgroundColor(
            if (item.isGood) Color.GREEN else Color.RED
        )
    }

    override fun isForViewType(item: Any) = item is Bio
    override fun Bio.getItemId() = this
}