package com.lifelink.app.activities

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.base.BaseActivity
import com.lifelink.app.databinding.ActivityRegisterBinding
import com.lifelink.app.models.RegisterRequest
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    // Blood types: index + 1 = blood_type_id as per DB seed order
    private val bloodTypes = listOf("O-", "O+", "B+", "B-", "A+", "A-", "AB+", "AB-")
    private val genders    = listOf("Male", "Female", "Other")

    override fun inflateBinding(inflater: LayoutInflater) = ActivityRegisterBinding.inflate(inflater)

    override fun setupUI() {
        // Populate spinners
        binding.spinnerBloodType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bloodTypes)
        binding.spinnerGender.adapter    = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)

        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName  = binding.etLastName.text.toString().trim()
        val email     = binding.etEmail.text.toString().trim()
        val password  = binding.etPassword.text.toString().trim()
        val confirm   = binding.etConfirmPassword.text.toString().trim()
        val phone     = binding.etPhone.text.toString().trim()
        val dob       = binding.etDob.text.toString().trim()
        val address   = binding.etAddress.text.toString().trim()
        val bloodTypeId = binding.spinnerBloodType.selectedItemPosition + 1
        val gender    = genders[binding.spinnerGender.selectedItemPosition]

        when {
            firstName.isEmpty() || lastName.isEmpty() -> { toast("Name is required"); return }
            email.isEmpty() -> { toast("Email is required"); return }
            password.length < 6 -> { toast("Password must be at least 6 characters"); return }
            password != confirm  -> { toast("Passwords do not match"); return }
            dob.isEmpty() -> { toast("Date of birth is required"); return }
        }

        binding.progressBar.visible()
        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val request = RegisterRequest(firstName, lastName, email, password, phone, dob, gender, address, bloodTypeId)
                val response = RetrofitClient.api.register(body = request)
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    SessionManager.saveSession(this@RegisterActivity, body.donorId ?: 0, body.firstName ?: "", body.bloodType ?: "")
                    toast("Account created! Welcome, $firstName!")
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    toast(body?.error ?: body?.message ?: "Registration failed")
                }
            } catch (e: Exception) {
                toast("Connection error: ${e.localizedMessage}")
            } finally {
                binding.progressBar.gone()
                binding.btnRegister.isEnabled = true
            }
        }
    }
}
