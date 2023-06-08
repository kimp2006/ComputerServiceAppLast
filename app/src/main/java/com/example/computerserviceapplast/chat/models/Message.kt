package com.example.computerserviceapplast.chat.models

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.logging.SimpleFormatter

data class Message(
    val id: String = "",
    val senderId: String = "",
    var senderName: String = "",
    val value: String = "",
    val date: Long = Date().time
){

    fun getTime(): String {
        val time = Date(date)
        val formatter = SimpleDateFormat("HH:mm", Locale.US)
        return formatter.format(time)
    }
}