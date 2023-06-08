package com.example.computerserviceapp.main.orders.ui.create

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.computerserviceapp.R
import com.example.computerserviceapp.databinding.FragmentOrderCreateBinding
import com.example.computerserviceapp.utils.getTrimText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderCreateFragment : Fragment() {

    private lateinit var viewModel: OrderCreateViewModel
    private lateinit var binding: FragmentOrderCreateBinding
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderCreateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderCreateViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    if (it.isSuccess()){
                        Navigation.findNavController(binding.root).navigate(R.id.action_global_home)
                    }

                    if (it.isError()){
                        Toast.makeText(requireContext(), it.exception?.message ?: "Server error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.createBtn2.setOnClickListener {
            val title = binding.tittle2.getTrimText()
            val body = binding.body2.getTrimText()
            viewModel.create(auth.currentUser!!.uid, title, body)
        }

        binding.cancelBtn2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_orderCreateFragment_to_listFragment)
        }

    }

}