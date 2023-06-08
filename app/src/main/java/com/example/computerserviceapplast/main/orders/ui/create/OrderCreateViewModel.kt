package com.example.computerserviceapplast.main.orders.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerserviceapplast.firebase.Order
import com.example.computerserviceapplast.firebase.OrderDataSource
import com.example.computerserviceapplast.main.orders.ui.CreateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderCreateViewModel : ViewModel() {


    private val _uiState: MutableStateFlow<CreateUiState<Order>> =
        MutableStateFlow(CreateUiState.Ready())
    val uiState = _uiState.asStateFlow()

    private val orderDataSource = OrderDataSource()

    fun create(userId: String, tittle: String, about: String) {
        viewModelScope.launch {
            try {
                val order = orderDataSource.insert(Order(userId = userId, tittle = tittle, body = about)).getOrThrow()
                _uiState.value = CreateUiState.Created(order)
            }
            catch (e: Exception){
                _uiState.value = CreateUiState.Error(e)
            }

        }
    }
}