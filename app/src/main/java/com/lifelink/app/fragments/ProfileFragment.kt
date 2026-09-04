package com.lifelink.app.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.activities.LoginActivity
import com.lifelink.app.base.BaseFragment
import com.lifelink.app.databinding.FragmentProfileBinding
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private var donorId: Int = -1

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun setupUI() {
        val ctx = requireContext()
        donorId = SessionManager.getDonorId(ctx)

        loadProfile()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(ctx)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    SessionManager.clearSession(ctx)
                    startActivity(Intent(ctx, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun loadProfile() {
        if (donorId == -1) return

        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getDonorProfile(donorId = donorId)
                val donor = response.body()?.data
                
                if (!isAdded || bindingOrNull == null) return@launch

                donor?.let { d ->
                    binding.tvName.text = d.fullName
                    binding.tvEmail.text = d.email
                    binding.tvBloodType.text = "Blood Type: ${d.bloodType}"
                    binding.tvPhone.text = "Phone: ${d.phone ?: "—"}"
                    binding.tvAddress.text = "Address: ${d.address ?: "—"}"
                    binding.tvDob.text = "Date of Birth: ${d.dateOfBirth ?: "—"}"
                    binding.tvLastDonation.text = "Last Donation: ${d.lastDonationDate ?: "Never"}"
                    binding.tvEligible.text = if (d.eligible) "✅ Eligible to donate" else "⏳ Not eligible yet"
                    
                    val memberDate = d.createdAt.takeIf { it.length >= 10 }?.take(10) ?: d.createdAt
                    binding.tvMemberSince.text = "Member since: $memberDate"
                    
                    binding.layoutProfile.visible()
                }
            } catch (e: Exception) {
                if (isAdded) toast("Error loading profile: ${e.localizedMessage}")
            } finally {
                bindingOrNull?.progressBar?.gone()
            }
        }
    }
}
