package com.golda.test.cats

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class Dispatchers(
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
) {

    companion object {

        val Default = Dispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
            main = Dispatchers.Main.immediate,
        )
    }
}