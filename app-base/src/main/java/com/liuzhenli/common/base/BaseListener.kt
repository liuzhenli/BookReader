package com.liuzhenli.common.base

interface BaseListener<T> {
    fun onResponse(content: T)
}