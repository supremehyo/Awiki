package com.supremehyo.awiki.utils

import android.util.Log
import com.supremehyo.awiki.DTO.HometoEditDTO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object EventBus {

    private val events = MutableSharedFlow<HometoEditDTO>()
    val mutableEvents = events.asSharedFlow()

    suspend fun post(event: HometoEditDTO) {
        events.emit(event)
    }

    inline fun <reified T> subscribe(): Flow<T> {
        return mutableEvents.filter { it is T }.map { it as T }
    }
}






