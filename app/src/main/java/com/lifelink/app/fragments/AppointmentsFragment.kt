package com.lifelink.app.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.activities.BookAppointmentActivity
import com.lifelink.app.adapters.AppointmentAdapter
import com.lifelink.app.base.BaseFragment
import com.lifelink.app.databinding.FragmentAppointmentsBinding
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class AppointmentsFragment : BaseFragment<FragmentAppointmentsBinding>() {

    private var donorId: Int = -1

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAppointmentsBinding.inflate(inflater, container, false)

    override fun setupUI() {
        donorId = SessionManager.getDonorId(requireContext())

        binding.fabBook.setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener { loadAppointments() }
        
        loadAppointments()
    }

    private fun loadAppointments() {
        if (donorId == -1) return

        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getAppointments(donorId = donorId)
                val appointments = response.body()?.data ?: emptyList()
                
                if (!isAdded || bindingOrNull == null) return@launch

                if (appointments.isEmpty()) {
                    binding.tvEmpty.visible()
                    binding.rvAppointments.gone()
                } else {
                    binding.tvEmpty.gone()
                    binding.rvAppointments.visible()
                    binding.rvAppointments.adapter = AppointmentAdapter(appointments) { appt ->
                        if (appt.status == "Confirmed" || appt.status == "Pending") {
                            cancelAppointment(appt.appointmentId)
                        }
                    }
                }
            } catch (e: Exception) {
                if (isAdded) toast("Error: ${e.localizedMessage}")
            } finally {
                bindingOrNull?.let {
                    it.progressBar.gone()
                    it.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun cancelAppointment(appointmentId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.cancelAppointment(appointmentId = appointmentId, donorId = donorId)
                if (!isAdded) return@launch
                
                if (response.body()?.success == true) {
                    toast("Appointment cancelled")
                    loadAppointments()
                } else {
                    toast("Could not cancel appointment")
                }
            } catch (e: Exception) {
                if (isAdded) toast("Error: ${e.localizedMessage}")
            }
        }
    }
}
