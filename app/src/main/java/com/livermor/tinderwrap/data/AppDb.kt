package com.livermor.tinderwrap.data

import com.chibatching.kotpref.KotprefModel

object AppDb : KotprefModel() {
    var token: String by stringPref("9c9ce063-d22d-4d48-b7fd-0489868a27e4")
    var fixSize: Int by intPref(5)
}