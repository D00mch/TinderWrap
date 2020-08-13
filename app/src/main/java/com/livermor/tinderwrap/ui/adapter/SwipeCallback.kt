package com.livermor.tinderwrap.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.media.MediaPlayer
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.livermor.tinderwrap.R
import com.livermor.tinderwrap.TinderApp
import com.livermor.tinderwrap.ui.screen.SwapMessage

class SwipeCallback(
    private val onSwipe: (SwapMessage.Swipe) -> Unit
) : ItemTouchHelper.Callback() {

    private val swipeWidth = 150
    private var swipeBack = false
    private val mp: MediaPlayer by lazy { MediaPlayer.create(TinderApp.instance, R.raw.drip2) }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        error("we are not gonna swipe it out")
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder, dX)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(recyclerView: RecyclerView, viewHolder: ViewHolder, dX: Float) {
        recyclerView.setOnTouchListener { v, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                when {
                    dX < -swipeWidth -> {
                        onSwipe(SwapMessage.Swipe(true, viewHolder.adapterPosition))
                        playSound()
                    }
                    dX > swipeWidth -> {
                        onSwipe(SwapMessage.Swipe(false, viewHolder.adapterPosition))
                        playSound()
                    }
                }
            }
            false
        }
    }

    private fun playSound() {
        mp.start()
    }
}