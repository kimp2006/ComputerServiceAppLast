package com.example.computerserviceapp.main.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.computerserviceapp.R
import com.example.computerserviceapp.databinding.FragmentAccountBinding
import com.example.computerserviceapp.utils.getTrimText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this, AccountViewModel(auth.currentUser!!.uid))[AccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOutTextView.setOnClickListener {
            Firebase.auth.signOut()
            Navigation.findNavController(it).navigate(R.id.action_global_auth)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.info.collect{
                    binding.info = it
                }
            }
        }

        binding.saveBtn.setOnClickListener {
            viewModel.save(
                name = binding.regNameEditText.getTrimText(),
                phone = binding.regPhoneEditText.getTrimText(),
                address = binding.rgAddressEditText.getTrimText(),
            )
        }
    }

}