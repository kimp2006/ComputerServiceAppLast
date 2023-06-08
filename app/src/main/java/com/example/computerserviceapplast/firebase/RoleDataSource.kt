package com.example.computerserviceapplast.firebase

import com.google.firebase.firestore.DocumentSnapshot

class RoleDataSource : FirebaseDataSource<RoleDTO>("roles") {
    override fun parse(v: DocumentSnapshot): RoleDTO? {
        return v.toObject(RoleDTO::class.java)!!
    }
}