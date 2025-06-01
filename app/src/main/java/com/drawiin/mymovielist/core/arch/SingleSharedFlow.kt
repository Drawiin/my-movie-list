package com.drawiin.mymovielist.core.arch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A [MutableSharedFlow] implementation that only emits the latest value to new subscribers,
 * and resets its replay cache after each emission.
 *
 * This is useful for one-time events (such as navigation or showing a message) where
 * you want to ensure the event is only handled once.
 *
 * @param T The type of values to be emitted.
 */
@ExperimentalCoroutinesApi
class SingleSharedFlow<T>() : MutableSharedFlow<T> {
    private val _flow = MutableSharedFlow<T>(replay = 1)

    /** The values currently held in the replay cache. */
    override val replayCache = _flow.replayCache

    /** The number of active subscribers to this flow. */
    override val subscriptionCount = _flow.subscriptionCount

    /** Resets the replay cache, removing any stored values. */
    override fun resetReplayCache() = _flow.resetReplayCache()

    /** Attempts to emit a value to the flow. */
    override fun tryEmit(value: T) = _flow.tryEmit(value)

    /** Emits a value to the flow, suspending until all subscribers receive it. */
    override suspend fun emit(value: T) = _flow.emit(value)

    /**
     * Collects values from the flow. After each emission, the replay cache is reset
     * to ensure the value is only delivered once.
     */
    override suspend fun collect(collector: kotlinx.coroutines.flow.FlowCollector<T>): Nothing {
        _flow.collect {
            collector.emit(it)
            _flow.resetReplayCache()
        }
    }
}
