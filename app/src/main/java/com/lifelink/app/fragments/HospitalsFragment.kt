package com.lifelink.app.fragments

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.adapters.HospitalAdapter
import com.lifelink.app.base.BaseFragment
import com.lifelink.app.databinding.FragmentHospitalsBinding
import com.lifelink.app.models.Hospital
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class HospitalsFragment : BaseFragment<FragmentHospitalsBinding>() {

    private var allHospitals: List<Hospital> = emptyList()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHospitalsBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterHospitals(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.swipeRefresh.setOnRefreshListener { loadHospitals() }
        loadHospitals()
    }

    private fun filterHospitals(query: String) {
        val q = query.lowercase()
        val filtered = allHospitals.filter {
            it.hospitalName.lowercase().contains(q) || it.address.lowercase().contains(q)
        }
        updateAdapter(filtered)
    }

    private fun loadHospitals() {
        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getHospitals()
                allHospitals = response.body()?.data ?: emptyList()
                if (allHospitals.isEmpty()) {
                    binding.tvEmpty.visible()
                    binding.rvHospitals.gone()
                } else {
                    binding.tvEmpty.gone()
                    binding.rvHospitals.visible()
                    updateAdapter(allHospitals)
                }
            } catch (e: Exception) {
                toast("Error: ${e.localizedMessage}")
            } finally {
                binding.progressBar.gone()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun updateAdapter(list: List<Hospital>) {
        binding.rvHospitals.adapter = HospitalAdapter(list) { hospital ->
            val lat = hospital.latitude; val lng = hospital.longitude
            if (lat != null && lng != null) {
                val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${hospital.hospitalName})")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } else toast("No location data available")
        }
    }
}
