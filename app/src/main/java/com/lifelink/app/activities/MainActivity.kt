package com.lifelink.app.activities

import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.lifelink.app.R
import com.lifelink.app.base.BaseActivity
import com.lifelink.app.databinding.ActivityMainBinding
import com.lifelink.app.fragments.*
import com.lifelink.app.utils.SessionManager

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun setupUI() {
        if (!SessionManager.isLoggedIn(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_dashboard     -> DashboardFragment()
                R.id.nav_appointments  -> AppointmentsFragment()
                R.id.nav_hospitals     -> HospitalsFragment()
                R.id.nav_alerts        -> AlertsFragment()
                R.id.nav_profile       -> ProfileFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }

        // Load default fragment if not already present
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            loadFragment(DashboardFragment())
            binding.bottomNav.selectedItemId = R.id.nav_dashboard
        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (supportFragmentManager.isStateSaved) return
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
