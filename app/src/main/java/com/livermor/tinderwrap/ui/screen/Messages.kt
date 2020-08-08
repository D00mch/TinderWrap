package com.livermor.tinderwrap.ui.screen

sealed class SwapMessage {
    class Choose(val like: Boolean) : SwapMessage()
    class Swipe(val isLeft: Boolean, val position: Int) : SwapMessage()
    object Next : SwapMessage()
}