package com.lifelink.app.activities

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.lifelink.app.base.BaseActivity
import com.lifelink.app.databinding.ActivitySplashBinding
import com.lifelink.app.utils.SessionManager

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun inflateBinding(inflater: LayoutInflater) = ActivitySplashBinding.inflate(inflater)

    override fun setupUI() {
        // Delay 1.5 seconds then route based on login state
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (SessionManager.isLoggedIn(this)) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1500)
    }
}
