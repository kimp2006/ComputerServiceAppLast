package com.example.computerserviceapplast.firebase

import java.util.Date

open class DTO(
    open var id: String = "",
)

data class RoleDTO(
    val value: String
): DTO()


data class UserInformation(
    var name: String = "",
    var phone: String = "",
    var address: String = "",
    var role: String = "",
) : DTO()
{

    override fun toString(): String {
        return name
    }
    companion object{
        fun empty(): UserInformation {
            return UserInformation("", "", "" , "")
        }
    }
}

data class Order(
    var tittle: String = "",
    var body: String = "",
    var userId: String = "",
    var managerId: String = "",
    var workerId: String = "",
    var date: Long = Date().time,
    var status: String = "CREATED",
    var services: String = "",
    var price: Double = 0.0,
    var chatId: String = ""
): DTO(){


}
