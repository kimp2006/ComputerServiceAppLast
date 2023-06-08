package com.example.computerserviceapp.main.account

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.computerserviceapp.firebase.UserInformation
import com.example.computerserviceapp.firebase.UserInformationDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(private val userId: String) : ViewModel(), ViewModelProvider.Factory {

    private val _info: MutableStateFlow<UserInformation> = MutableStateFlow(UserInformation())
    val info = _info.asStateFlow()

    private val userInformationDataSource = UserInformationDataSource()
    private fun init(){
        viewModelScope.launch {
            val info = userInformationDataSource.getById(userId).getOrNull()
            if (info != null){
                _info.value = info
            }
        }
    }

    fun save(name: String, phone: String, address: String){
        viewModelScope.launch {
            userInformationDataSource.update(
                _info.value.copy(name = name, phone = phone, address = address).apply {
                    id = _info.value.id
                }
            )
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)){
            val vm = AccountViewModel(userId = userId)
            vm.init()
            return  vm as T
        }
        throw NotFoundException("AccountViewModel not found")
    }
}