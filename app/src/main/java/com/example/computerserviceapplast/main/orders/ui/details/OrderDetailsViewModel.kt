package com.example.computerserviceapp.main.orders.ui.details

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.*
import com.example.computerserviceapp.firebase.Order
import com.example.computerserviceapp.firebase.OrderDataSource
import com.example.computerserviceapp.firebase.UserInformation
import com.example.computerserviceapp.firebase.UserInformationDataSource
import com.example.computerserviceapp.main.orders.ui.CreateUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class OrderDetailsViewModel(private val loggedUserId: String, private val orderId: String) :
    ViewModel(), ViewModelProvider.Factory {

    private val _orderOwner: MutableStateFlow<UserInformation> = MutableStateFlow(UserInformation())
    val orderOwner: StateFlow<UserInformation> = _orderOwner.asStateFlow()

    private val _worker: MutableStateFlow<UserInformation> = MutableStateFlow(UserInformation())
    val worker: StateFlow<UserInformation> = _worker.asStateFlow()

    private val _loggedUser: MutableStateFlow<UserInformation> = MutableStateFlow(UserInformation())
    val loggedUser: StateFlow<UserInformation> = _loggedUser.asStateFlow()

    private val _order: MutableStateFlow<Order> = MutableStateFlow(Order())
    val order: StateFlow<Order> = _order.asStateFlow()

    private val _workers: MutableStateFlow<List<UserInformation>> = MutableStateFlow(emptyList())
    val workers = _workers.asStateFlow()


    private val _uiState: MutableStateFlow<CreateUiState<Order>> =
        MutableStateFlow(CreateUiState.Ready())
    val uiState = _uiState.asStateFlow()

    private val orderDataSource = OrderDataSource()
    private val userInformationDataSource = UserInformationDataSource()


    private fun init() {
        viewModelScope.launch {
            try {

                val order = orderDataSource.getById(orderId).getOrThrow()
                _order.value = order

                val user = userInformationDataSource.getById(order.userId).getOrThrow()
                _orderOwner.value = user

                if (order.workerId != ""){
                    val worker = userInformationDataSource.getById(order.workerId).getOrThrow()
                    _worker.value = worker
                }


                val loggedUser = userInformationDataSource.getById(loggedUserId).getOrThrow()
                _loggedUser.value = loggedUser


            } catch (e: Exception) {
                _uiState.value = CreateUiState.Error(e)
            }
        }
    }


    fun create(userId: String, tittle: String, about: String) {
        val order = _order.value.copy(userId = userId, tittle = tittle, body = about)
        order.id = _order.value.id
        _order.value = order
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)) {
            val vm = OrderDetailsViewModel(loggedUserId, orderId)
            vm.init()
            vm.initWorkers()
            return vm as T
        }
        throw NotFoundException("OrderDetailsViewModel not found")
    }

    private fun initWorkers() {
        viewModelScope.launch {
            val list = userInformationDataSource.where("role", "WORKER").getOrNull()
            if (list != null) {
                _workers.value = list
            }
        }
    }

    fun report(services: String, price: Double) {
        val order = _order.value.copy(services = services, price = price, status = "COMPLETE")
        order.id = _order.value.id
        _order.value = order
    }

    fun save() {
        viewModelScope.launch {
            if (orderId == null) {
                val order = orderDataSource.insert(_order.value).getOrThrow()
                _uiState.value = CreateUiState.Created(order)
            } else {
                val order = orderDataSource.update(_order.value).getOrThrow()
                _uiState.value = CreateUiState.Created(order)
            }

        }
    }

    fun addWorker(position: Int, uid: String) {
        val worker = _workers.value[position]
        _order.value = _order.value.copy(workerId = worker.id, status = "IN PROGRESS").apply {
            id = _order.value.id
        }
        _worker.value = _worker.value.copy(name = worker.name).apply {
            id = _worker.value.id
        }

        createChat(worker.id, uid)
    }

    private fun createChat(id: String, uid: String) {
        viewModelScope.launch {

        }
    }

}