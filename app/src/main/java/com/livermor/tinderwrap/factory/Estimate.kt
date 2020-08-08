package com.livermor.tinderwrap.factory

import android.graphics.Color
import androidx.annotation.ColorInt
import com.livermor.tinderwrap.Estimated

fun Estimated.swipeLeft(): Estimated = when (this.type) {
    Estimated.Type.BAD -> this
    Estimated.Type.NEUTRAL -> rate(Estimated.Type.BAD)
    Estimated.Type.GOOD -> rate(Estimated.Type.NEUTRAL)
}

fun Estimated.swipeRight(): Estimated = when (this.type) {
    Estimated.Type.BAD -> rate(Estimated.Type.NEUTRAL)
    Estimated.Type.NEUTRAL -> rate(Estimated.Type.GOOD)
    Estimated.Type.GOOD -> this
}

fun Estimated.swipe(isLeft: Boolean): Estimated = if (isLeft) swipeLeft() else swipeRight()

@ColorInt
fun Estimated.Type.color(): Int = when (this) {
    Estimated.Type.BAD -> Color.RED
    Estimated.Type.NEUTRAL -> Color.YELLOW
    Estimated.Type.GOOD -> Color.GREEN
}

fun Estimated.Type.prefix(): Int = when (this) {
    Estimated.Type.BAD -> 0
    Estimated.Type.GOOD -> 1
    Estimated.Type.NEUTRAL -> 2
}
