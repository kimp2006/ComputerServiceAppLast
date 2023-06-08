package com.example.computerserviceapp.utils

import android.widget.EditText

fun EditText.getTrimText(): String {
    return this.text.toString().trim();
}