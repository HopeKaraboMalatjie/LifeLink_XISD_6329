package com.lifelink.app.activities

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.lifelink.app.base.BaseActivity
import com.lifelink.app.databinding.ActivityBookAppointmentBinding
import com.lifelink.app.models.BookAppointmentRequest
import com.lifelink.app.models.Hospital
import com.lifelink.app.network.RetrofitClient
import com.lifelink.app.utils.SessionManager
import com.lifelink.app.utils.gone
import com.lifelink.app.utils.toast
import com.lifelink.app.utils.visible
import kotlinx.coroutines.launch
import java.util.*

class BookAppointmentActivity : BaseActivity<ActivityBookAppointmentBinding>() {

    private var hospitals: List<Hospital> = emptyList()

    private val timeSlots = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
        "14:00", "14:30", "15:00", "15:30", "16:00"
    )

    override fun inflateBinding(inflater: LayoutInflater) = ActivityBookAppointmentBinding.inflate(inflater)

    override fun setupUI() {
        supportActionBar?.title = "Book Appointment"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Time slots
        binding.spinnerTime.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timeSlots)

        // Donation types
        binding.spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("WholeBlood", "Plasma"))

        // Date picker
        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, 1)
            DatePickerDialog(this, { _, y, m, d ->
                binding.etDate.setText("$y-${(m+1).toString().padStart(2,'0')}-${d.toString().padStart(2,'0')}")
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .also { it.datePicker.minDate = cal.timeInMillis }
                .show()
        }

        loadHospitals()

        binding.btnBook.setOnClickListener { submitBooking() }
    }

    private fun loadHospitals() {
        binding.progressBar.visible()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getHospitals()
                hospitals = response.body()?.data ?: emptyList()
                binding.spinnerHospital.adapter = ArrayAdapter(
                    this@BookAppointmentActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    hospitals.map { it.hospitalName }
                )
            } catch (e: Exception) {
                toast("Error loading hospitals: ${e.localizedMessage}")
            } finally {
                binding.progressBar.gone()
            }
        }
    }

    private fun submitBooking() {
        val donorId = SessionManager.getDonorId(this)
        val date    = binding.etDate.text.toString().trim()
        val notes   = binding.etNotes.text.toString().trim()

        if (hospitals.isEmpty()) { toast("No hospitals available"); return }
        if (date.isEmpty()) { toast("Please select a date"); return }

        val hospital = hospitals[binding.spinnerHospital.selectedItemPosition]
        val time     = timeSlots[binding.spinnerTime.selectedItemPosition]
        val type     = if (binding.spinnerType.selectedItemPosition == 0) "WholeBlood" else "Plasma"

        binding.progressBar.visible()
        binding.btnBook.isEnabled = false

        lifecycleScope.launch {
            try {
                val request = BookAppointmentRequest(donorId, hospital.hospitalId, date, time, type, notes)
                val response = RetrofitClient.api.bookAppointment(body = request)
                if (response.body()?.success == true) {
                    toast("Appointment booked successfully!")
                    finish()
                } else {
                    toast(response.body()?.message ?: "Booking failed")
                }
            } catch (e: Exception) {
                toast("Error: ${e.localizedMessage}")
            } finally {
                binding.progressBar.gone()
                binding.btnBook.isEnabled = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
