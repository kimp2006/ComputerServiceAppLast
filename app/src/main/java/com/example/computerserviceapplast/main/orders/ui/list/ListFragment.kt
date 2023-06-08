package com.example.computerserviceapp.main.orders.ui.list

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
import com.example.computerserviceapp.auth.data.UserSession
import com.example.computerserviceapp.databinding.FragmentListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var binding: FragmentListBinding
    private lateinit var orderListAdapter: OrderListAdapter

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        orderListAdapter = OrderListAdapter()
        orderListAdapter.itemClick {
            val bundle = Bundle()
            bundle.putString("orderId", it.id)
            Navigation.findNavController(binding.root).navigate(R.id.action_listFragment_to_orderDetailsFragment, bundle)
        }
        binding.recyclerView.adapter = orderListAdapter
        viewModel = ViewModelProvider(
            this,
            ListViewModel(auth.currentUser!!.uid)
        )[ListViewModel::class.java]


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_listFragment_to_orderCreateFragment))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orders.collect {
                    orderListAdapter.items = it.data!!

                    if (it.isError()) {
                        Toast.makeText(
                            requireContext(),
                            it.exception?.message ?: "Server error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

    }


}