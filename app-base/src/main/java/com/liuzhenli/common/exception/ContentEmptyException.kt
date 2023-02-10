package com.micoredu.reader.exception

import com.liuzhenli.common.exception.NoStackTraceException

/**
 * 内容为空
 */
class ContentEmptyException(msg: String) : NoStackTraceException(msg)