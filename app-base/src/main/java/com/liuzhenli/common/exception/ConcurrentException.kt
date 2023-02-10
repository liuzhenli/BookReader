@file:Suppress("unused")

package com.liuzhenli.common.exception

/**
 * 并发限制
 */
class ConcurrentException(msg: String, val waitTime: Int) : NoStackTraceException(msg)