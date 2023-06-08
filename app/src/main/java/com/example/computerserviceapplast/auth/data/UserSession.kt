package com.example.computerserviceapplast.auth.data

class UserSession {
    companion object{

        var loggedUser: LoggedUser? = null

        fun isLogged(): Boolean {
            return loggedUser == null
        }

        fun clear(){
            loggedUser = null
        }
    }
}