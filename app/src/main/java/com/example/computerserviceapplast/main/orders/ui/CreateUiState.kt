package com.example.computerserviceapplast.main.orders.ui

open class CreateUiState<T>(val data: T? = null, val exception: Exception? = null) {

    class Ready<T>(): CreateUiState<T>()

    class Loading<T>(): CreateUiState<T>()

    class Created<T>(data: T?): CreateUiState<T>(data = data)

    class Error<T>(e: Exception): CreateUiState<T>(exception =  e)

    fun isSuccess(): Boolean {
        return this is Created<T>
    }

    fun isError(): Boolean{
        return this is Error<T>
    }

}