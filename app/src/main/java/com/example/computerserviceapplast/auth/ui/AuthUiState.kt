package com.example.computerserviceapplast.auth.ui

import com.example.computerserviceapp.auth.data.LoggedUser

open class AuthUiState {
    data class Success(val loggedUser: LoggedUser): AuthUiState()
    data class Error(val e: Exception) : AuthUiState()

    object Empty : AuthUiState()

    object Loading : AuthUiState()
    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading
}