package com.edu.sun.oereminder.data

interface SourceCallback<T> {
    fun onSuccess(data: T)
    fun onError(e: Exception)
}
