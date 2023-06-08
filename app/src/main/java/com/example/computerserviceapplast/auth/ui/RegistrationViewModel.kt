package com.example.computerserviceapplast.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerserviceapp.auth.data.LoggedUser
import com.example.computerserviceapp.firebase.UserInformation
import com.example.computerserviceapp.firebase.UserInformationDataSource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistrationViewModel : ViewModel() {

    private val dataSource: UserInformationDataSource = UserInformationDataSource()
    private val aut = Firebase.auth

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.Empty);
    val uiState = _uiState.asStateFlow()

    fun registration(login: String, pass: String, name: String, phone: String, address: String){
        viewModelScope.launch {
            try {
                val user = aut.createUserWithEmailAndPassword(login, pass).await().user
                if (user != null){
                    val info = UserInformation(
                        name = name,
                        phone = phone,
                        address = address,
                        role = "USER"
                    )
                    val result = dataSource.insert(info, user.uid).getOrThrow()
                    _uiState.value = AuthUiState.Success(
                        LoggedUser(
                            id = result.id,
                            name = result.name,
                            role = result.role
                        )
                    )
                }

            }
            catch (e: Exception){
                _uiState.value = AuthUiState.Error(e)
            }
        }
    }

}