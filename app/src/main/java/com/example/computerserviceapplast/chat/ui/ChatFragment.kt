package com.example.computerserviceapp.chat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.computerserviceapp.chat.models.Message
import com.example.computerserviceapp.firebase.UserInformationDataSource
import com.example.computerserviceapp.databinding.FragmentChatBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null
    private val userInformationDataSource = UserInformationDataSource()
    private lateinit var messagesRef: DatabaseReference
    private lateinit var messageListAdapter: MessageListAdapter

    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        val chatId = arguments?.getString(ID_KEY)!!
        messagesRef = Firebase.database.getReference("chats").child(chatId).child("messages")
        messageListAdapter = MessageListAdapter(auth.currentUser!!.uid)
        binding?.recyclerView?.adapter = messageListAdapter
        binding?.sendBtn?.setOnClickListener(sendBtnClickListener)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesRef.addChildEventListener(childEventListener)
    }


    private val sendBtnClickListener: (View) -> Unit = {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
               val userInfo =  withContext(Dispatchers.IO) {
                    userInformationDataSource.getById(auth.currentUser!!.uid)
                }.getOrThrow()
                val key = messagesRef.push().key!!
                val message = Message(
                    id = key,
                    senderId = auth.currentUser!!.uid,
                    senderName = userInfo.name,
                    value = binding?.messageText?.text.toString()
                )
                send(message)
            }
            catch (e: Exception){

            }

        }
    }

    private fun send(message: Message) {
        binding?.messageText?.setText("")
        messagesRef.child(message.id).setValue(message).addOnCompleteListener {
            if (it.isSuccessful){
                messageListAdapter.addMessage(message)
            }
            else{

            }
        }
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = snapshot.getValue<Message>()
            messageListAdapter.addMessage(message!!)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val ID_KEY = "chatId"
    }

}