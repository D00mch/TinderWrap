package com.livermor.tinderwrap.ui.screen


sealed class Message {

    class Choose(val like: Boolean) : Message()
    class Swipe(val isLeft: Boolean, val position: Int): Message()
}