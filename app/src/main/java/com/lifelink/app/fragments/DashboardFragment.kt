package com.lifelink.app.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.R
import com.lifelink.app.adapters.AlertAdapter
import com.lifelink.app.base.BaseFragment
import com.lifelink.app.databinding.FragmentDashboardBinding
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private var donorId: Int = -1

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        val ctx = requireContext()
        donorId = SessionManager.getDonorId(ctx)
        val firstName = SessionManager.getFirstName(ctx)
        val bloodType = SessionManager.getBloodType(ctx)

        binding.tvGreeting.text = getString(R.string.welcome_back, firstName)
        binding.tvBloodType.text = getString(R.string.blood_type_format, bloodType)

        binding.swipeRefresh.setOnRefreshListener { loadDashboard() }

        loadDashboard()
    }

    private fun loadDashboard() {
        if (donorId == -1) return
        
        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                // Load donor profile
                val profileResp = RetrofitClient.api.getDonorProfile(donorId = donorId)
                val donor = profileResp.body()?.data
                
                // Safety check: Ensure fragment is still attached and binding exists
                if (!isAdded || bindingOrNull == null) return@launch

                donor?.let { d ->
                    val eligible = d.eligible
                    binding.cardEligibility.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), if (eligible) android.R.color.holo_green_light else android.R.color.holo_orange_light)
                    )
                    binding.tvEligibility.text = if (eligible) {
                        getString(R.string.eligible_to_donate)
                    } else {
                        getString(R.string.not_eligible_format, d.lastDonationDate ?: "—")
                    }
                }

                // Load upcoming appointments
                val apptResp = RetrofitClient.api.getAppointments(donorId = donorId, status = "Confirmed")
                val appointments = apptResp.body()?.data ?: emptyList()
                
                if (!isAdded || bindingOrNull == null) return@launch
                
                binding.tvUpcomingCount.text = appointments.size.toString()
                if (appointments.isNotEmpty()) {
                    val next = appointments.first()
                    binding.tvNextAppointment.text = getString(
                        R.string.next_appointment_format,
                        next.hospitalName ?: "Hospital",
                        next.scheduledDate,
                        next.scheduledTime
                    )
                    binding.layoutNextAppt.visible()
                } else {
                    binding.tvNextAppointment.text = getString(R.string.no_upcoming_appointments)
                    binding.layoutNextAppt.gone()
                }

                // Load donation history count
                val historyResp = RetrofitClient.api.getDonationHistory(donorId = donorId)
                binding.tvTotalDonations.text = (historyResp.body()?.data?.size ?: 0).toString()

                // Load emergency alerts
                val alertsResp = RetrofitClient.api.getEmergencyAlerts()
                val alerts = alertsResp.body()?.data ?: emptyList()
                binding.tvAlertsCount.text = alerts.size.toString()

                if (alerts.isNotEmpty()) {
                    binding.rvAlerts.adapter = AlertAdapter(alerts.take(3))
                    binding.layoutAlerts.visible()
                } else {
                    binding.layoutAlerts.gone()
                }

            } catch (e: Exception) {
                if (isAdded) toast(getString(R.string.error_loading_dashboard, e.localizedMessage))
            } finally {
                bindingOrNull?.let {
                    it.progressBar.gone()
                    it.swipeRefresh.isRefreshing = false
                }
            }
        }
    }
}
