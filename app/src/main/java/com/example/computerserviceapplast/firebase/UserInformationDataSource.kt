package com.example.computerserviceapplast.firebase

import com.google.firebase.firestore.DocumentSnapshot

class UserInformationDataSource : FirebaseDataSource<UserInformation>("user-info") {
    override fun parse(v: DocumentSnapshot): UserInformation? {
        return v.toObject(UserInformation::class.java)
    }
}