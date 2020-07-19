package com.livermor.tinderwrap.ui


sealed class Message {

    class Choose(val like: Boolean) : Message()
    class Swipe(val isLeft: Boolean, val position: Int): Message()
}