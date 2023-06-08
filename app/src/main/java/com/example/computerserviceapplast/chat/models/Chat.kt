package com.example.computerserviceapp.chat.models

data class Chat(
    val id: String = "",
    val name: String = "",
    val members: List<String> = emptyList(),
    val messages: List<Message> = emptyList()
)
