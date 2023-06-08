package com.example.computerserviceapplast.main.orders.ui.list

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.computerserviceapplast.firebase.Order
import com.example.computerserviceapplast.firebase.OrderDataSource
import com.example.computerserviceapplast.firebase.UserInformationDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val userId: String) : ViewModel(), ViewModelProvider.Factory {

    private val _orders: MutableStateFlow<ListUiState<Order>> = MutableStateFlow(ListUiState.Empty())
    val orders = _orders.asStateFlow()

    private val orderDataSource = OrderDataSource()
    private val userInformationDataSource = UserInformationDataSource()

    private fun init(){
        viewModelScope.launch {
            try {
                val user = userInformationDataSource.getById(userId)
                    .getOrThrow()
                if(user.role == "USER" ){
                    val list = orderDataSource.where("userId", userId).getOrNull()
                    if (list != null){
                        _orders.value = ListUiState.Success(list)
                    }
                }

                if (user.role == "WORKER"){
                    val list = orderDataSource.where("workerId", userId).getOrNull()
                    if (list != null){
                        _orders.value = ListUiState.Success(list)
                    }
                }

                if (user.role == "MANAGER"){
                    val list = orderDataSource.getAll().getOrNull()
                    if (list != null){
                        _orders.value = ListUiState.Success(list)
                    }
                }
            }
            catch (e: Exception){
                _orders.value = ListUiState.Error(e)
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)){
            val viewModel = ListViewModel(userId)
            viewModel.init()
            return viewModel as T
        }
        throw NotFoundException("ListViewModel not found")
    }
}

open class ListUiState<T>(val data: List<T>?, val exception: Exception? = null){

    class Empty<T>(list: List<T> = emptyList()) : ListUiState<T>(list)
    class Success<T>(list: List<T>): ListUiState<T>(list)

    class Error<T>(e: Exception) : ListUiState<T>(emptyList(), e)

    fun isSuccess(): Boolean {
        return this is Success<T>
    }

    fun isError(): Boolean{
        return this is Error
    }
 }