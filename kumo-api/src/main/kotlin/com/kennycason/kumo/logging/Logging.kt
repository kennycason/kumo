package com.kennycason.kumo.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

interface Logging {
    val logger: KLogger
        get() = KotlinLogging.logger(this::class.java.name)
}
