package com.example.computerserviceapplast.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.computerserviceapp.utils.getTrimText
import com.example.computerserviceapplast.R
import com.example.computerserviceapplast.auth.data.UserSession
import com.example.computerserviceapplast.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    if(it is AuthUiState.Success){
                        UserSession.loggedUser = it.loggedUser
                        Navigation.findNavController(binding.root).navigate(R.id.action_global_home)
                    }
                    if (it is AuthUiState.Error){
                        Toast.makeText(requireContext(), it.e.message ?: "Server error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.registerBtn.setOnClickListener(regBtnClickListener)

        binding.regBackToSigninBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }


    private val regBtnClickListener : (View) -> Unit = {
        viewModel.registration(
            binding.regEmailEditText.getTrimText(),
            binding.regPassEditText.getTrimText(),
            binding.regNameEditText.getTrimText(),
            binding.regPhoneEditText.getTrimText(),
            binding.rgAddressEditText.getTrimText()
        )
    }


}