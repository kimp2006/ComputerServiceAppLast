package com.example.computerserviceapplast.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerserviceapplast.firebase.UserInformationDataSource
import com.example.computerserviceapplast.auth.data.LoggedUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val dataSource: UserInformationDataSource = UserInformationDataSource()

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.Empty);
    val uiState = _uiState.asStateFlow()


    fun signIn(login : String, pass: String){

        if (!(validate(login) && validate(pass))){
            _uiState.value = AuthUiState.Error(Exception("Field is empty"))
            return
        }

        viewModelScope.launch {
            try {
                val user =  auth.signInWithEmailAndPassword(login, pass).await().user
                if (user != null){
                    val info  = dataSource.getById(user.uid).getOrThrow()
                    _uiState.value = AuthUiState.Success(
                        LoggedUser(
                        id = info.id,
                        name = info.name,
                        role = info.role
                    )
                    )
                }
            }
            catch (e:Exception){
                _uiState.value = AuthUiState.Error(e)
            }
        }

    }

    private fun validate(value: String): Boolean {
        return value.isNotEmpty() && value.isNotBlank()
    }



}