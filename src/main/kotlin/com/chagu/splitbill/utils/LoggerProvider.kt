package com.chagu.splitbill.utils

import org.slf4j.LoggerFactory

object LoggerProvider {
    fun <T> getLogger(cl: Class<T>) = LoggerFactory.getLogger(cl)
}
