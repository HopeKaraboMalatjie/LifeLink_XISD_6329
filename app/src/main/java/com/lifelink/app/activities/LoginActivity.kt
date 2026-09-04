package com.lifelink.app.activities

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.R
import com.lifelink.app.base.BaseActivity
import com.lifelink.app.databinding.ActivityLoginBinding
import com.lifelink.app.models.LoginRequest
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun inflateBinding(inflater: LayoutInflater) = ActivityLoginBinding.inflate(inflater)

    override fun setupUI() {
        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email    = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            toast(getString(R.string.please_enter_credentials))
            return
        }

        binding.progressBar.visible()
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.login(body = LoginRequest(email, password))
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    SessionManager.saveSession(
                        this@LoginActivity,
                        body.donorId ?: 0,
                        body.firstName ?: "",
                        body.bloodType ?: ""
                    )
                    toast(getString(R.string.welcome_back, body.firstName))
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    toast(body?.error ?: body?.message ?: getString(R.string.invalid_credentials))
                }
            } catch (e: Exception) {
                toast(getString(R.string.connection_error, e.localizedMessage))
            } finally {
                binding.progressBar.gone()
                binding.btnLogin.isEnabled = true
            }
        }
    }
}
