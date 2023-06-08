package com.example.computerserviceapplast.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class FirebaseDataSource<T : DTO>(private val collectionName: String) {

    private val db = Firebase.firestore
    private val ioDispatcher = Dispatchers.IO


    suspend fun <V> where(field: String, value: V) = withContext(ioDispatcher){
        try {
            val list = db.collection(collectionName).whereEqualTo(field, value).get().await()
            Result.success(list.map { q -> parse(q)!! })
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun getAll() = withContext(ioDispatcher) {
        try {
            val list = db.collection(collectionName).get().await()
            Result.success(list.map { q -> parse(q)!! })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insert(t: T) = withContext(ioDispatcher) {
        try {
            val doc = db.collection(collectionName).document()
            t.id = doc.id
            doc.set(t).await()
            Result.success(t)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insert(t: T, id: String) = withContext(ioDispatcher){
        try {
            val doc = db.collection(collectionName).document(id)
            t.id = doc.id
            doc.set(t).await()
            Result.success(t)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun delete(t: T) = withContext(ioDispatcher) {
        try {
            db.collection(collectionName).document(t.id).delete().await()
            Result.success(t)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun update(t: T) = withContext(ioDispatcher) {
        try {
            val doc = db.collection(collectionName).document(t.id)
            doc.set(t).await()
            Result.success(t)
        }
        catch (e : Exception){
            Result.failure(e)
        }

    }


    suspend fun getById(id: String) = withContext(ioDispatcher) {
        try {
            val task = db.collection(collectionName).document(id).get().await()
            Result.success(parse(task)!!)
        }
        catch (e: Exception){
            Result.failure(e)
        }


    }


    abstract fun parse(v: DocumentSnapshot): T?

}
