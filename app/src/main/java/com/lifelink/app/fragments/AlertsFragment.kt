package com.lifelink.app.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.adapters.AlertAdapter
import com.lifelink.app.base.BaseFragment
import com.lifelink.app.databinding.FragmentAlertsBinding
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class AlertsFragment : BaseFragment<FragmentAlertsBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAlertsBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.swipeRefresh.setOnRefreshListener { loadAlerts() }
        loadAlerts()
    }

    private fun loadAlerts() {
        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getEmergencyAlerts()
                val alerts = response.body()?.data ?: emptyList()
                if (alerts.isEmpty()) {
                    binding.tvEmpty.visible()
                    binding.rvAlerts.gone()
                } else {
                    binding.tvEmpty.gone()
                    binding.rvAlerts.visible()
                    binding.rvAlerts.adapter = AlertAdapter(alerts)
                }
            } catch (e: Exception) {
                toast("Error: ${e.localizedMessage}")
            } finally {
                binding.progressBar.gone()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
}
