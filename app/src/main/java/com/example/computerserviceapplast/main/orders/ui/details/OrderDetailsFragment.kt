package com.example.computerserviceapplast.main.orders.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.computerserviceapp.utils.getTrimText
import com.example.computerserviceapplast.R
import com.example.computerserviceapplast.databinding.FragmentOrderDetailsBinding
import com.example.computerserviceapplast.databinding.ReportLayoutBinding
import com.example.computerserviceapplast.databinding.WorkerAddLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class OrderDetailsFragment : Fragment() {

    private lateinit var viewModel: OrderDetailsViewModel
    private lateinit var binding: FragmentOrderDetailsBinding
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        val orderId = requireArguments().getString("orderId", null)
        viewModel = ViewModelProvider(
            this,
            OrderDetailsViewModel(auth.currentUser!!.uid, orderId)
        )[OrderDetailsViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addWorkerButton.setOnClickListener {
            showAddWorkerDialog()
        }


        binding.processEditImg.setOnClickListener {
            showReportDialog()
        }

        binding.createBtn.setOnClickListener {
            viewModel.save()
        }

        binding.cancelBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_orderDetailsFragment_to_listFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orderOwner.collect {
                    binding.info = it
                    showUiByRole(it.role)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.order.collect{
                   binding.order = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.worker.collect{
                    binding.worker = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect {
                    if (it.isSuccess()) {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_orderDetailsFragment_to_listFragment)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loggedUser.collect{
                    showUiByRole(it.role)
                }
            }
        }



    }

    private fun showUiByRole(role: String) {
        binding.processEditImg.isVisible = role == "WORKER"
       // binding.chatIcon.isVisible = (role == "WORKER") || (role == "MANAGER")
        binding.addWorkerButton.isVisible = role == "MANAGER"
        binding.createBtn.isVisible = (role == "WORKER") || (role == "MANAGER")
    }




    private fun showReportDialog(){
        val binding = ReportLayoutBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton("Save"){ _, _ ->
                run {
                    val services = binding.services.getTrimText()
                    val price = binding.price.getTrimText().toDouble()
                    viewModel.report(services, price)
                }
            }
            .setNegativeButton("Cancel"){ d, _ ->
                run {
                    d.cancel()
                }
            }
            .show()
    }


    private fun showAddWorkerDialog(){
        val binding = WorkerAddLayoutBinding.inflate(layoutInflater)

        viewLifecycleOwner.lifecycleScope.launch {
          viewModel.workers.collect{
              val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
              binding.list.adapter = adapter
          }
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setNegativeButton("Cancel"){ d, _ ->
                run {
                    d.cancel()
                }
            }
            .show()

        binding.list.setOnItemClickListener { _, _, position, _ ->
            viewModel.addWorker(position, auth.currentUser!!.uid)
            dialog.cancel()
        }

    }

}