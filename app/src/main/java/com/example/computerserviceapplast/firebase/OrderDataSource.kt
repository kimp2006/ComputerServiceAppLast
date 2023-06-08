package com.example.computerserviceapplast.firebase

import com.google.firebase.firestore.DocumentSnapshot

class OrderDataSource(collectionName: String = "orders") : FirebaseDataSource<Order>(collectionName) {
    override fun parse(v: DocumentSnapshot): Order? {
        return v.toObject(Order::class.java)
    }
}