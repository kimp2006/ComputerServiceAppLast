package com.example.computerserviceapplast.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.computerserviceapp.R
import com.example.computerserviceapp.auth.data.UserSession
import com.example.computerserviceapp.databinding.FragmentLoginBinding
import com.example.computerserviceapp.utils.getTrimText
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel = LoginViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        initSignInBtnClick()
        return binding.root
    }

    private fun initSignInBtnClick() {
        binding.signInBtn.setOnClickListener {
            it.isEnabled = !(it.isEnabled)
            val email = binding.emailEditText.getTrimText()
            val password = binding.paswordEditText.getTrimText()
            viewModel.signIn(email, password)
        }

        binding.rgTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registrationFragment))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it is AuthUiState.Success) {
                        UserSession.loggedUser = it.loggedUser
                        view.findNavController()
                            .navigate(R.id.action_global_home)
                    }
                    if (it.isError()) {
                        binding.signInBtn.isEnabled = true
                        val message = (it as AuthUiState.Error).e.message
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    if (it.isLoading()) {

                    }

                }
            }
        }
    }

}