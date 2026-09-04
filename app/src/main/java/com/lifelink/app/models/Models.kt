package com.lifelink.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// ─── API Response Wrappers ───

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data")    val data: T? = null,
    @SerializedName("error")   val error: String? = null
)

data class LoginResponse(
    @SerializedName("success")    val success: Boolean,
    @SerializedName("message")    val message: String? = null,
    @SerializedName("donor_id")   val donorId: Int? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("blood_type") val bloodType: String? = null,
    @SerializedName("error")      val error: String? = null
)

// ─── Domain Models ───

@Parcelize
data class Donor(
    @SerializedName("donor_id")          val donorId: Int,
    @SerializedName("first_name")        val firstName: String,
    @SerializedName("last_name")         val lastName: String,
    @SerializedName("email")             val email: String,
    @SerializedName("phone")             val phone: String?,
    @SerializedName("date_of_birth")     val dateOfBirth: String?,
    @SerializedName("gender")            val gender: String?,
    @SerializedName("address")           val address: String?,
    @SerializedName("full_type")         val bloodType: String,
    @SerializedName("last_donation_date") val lastDonationDate: String?,
    @SerializedName("is_eligible")       val isEligible: Int,
    @SerializedName("is_active")         val isActive: Int,
    @SerializedName("created_at")        val createdAt: String
) : Parcelable {
    val fullName get() = "$firstName $lastName"
    val eligible get() = isEligible == 1
}

@Parcelize
data class Hospital(
    @SerializedName("hospital_id")    val hospitalId: Int,
    @SerializedName("hospital_name")  val hospitalName: String,
    @SerializedName("address")        val address: String,
    @SerializedName("latitude")       val latitude: Double?,
    @SerializedName("longitude")      val longitude: Double?,
    @SerializedName("contact_email")  val contactEmail: String?,
    @SerializedName("contact_phone")  val contactPhone: String?,
    @SerializedName("operating_hours") val operatingHours: String?,
    @SerializedName("is_approved")    val isApproved: Int,
    @SerializedName("available_types") val availableTypes: String?
) : Parcelable

@Parcelize
data class Appointment(
    @SerializedName("appointment_id")  val appointmentId: Int,
    @SerializedName("donor_id")        val donorId: Int,
    @SerializedName("hospital_id")     val hospitalId: Int,
    @SerializedName("hospital_name")   val hospitalName: String?,
    @SerializedName("scheduled_date")  val scheduledDate: String,
    @SerializedName("scheduled_time")  val scheduledTime: String,
    @SerializedName("donation_type")   val donationType: String,
    @SerializedName("status")          val status: String,
    @SerializedName("notes")           val notes: String?,
    @SerializedName("created_at")      val createdAt: String
) : Parcelable

@Parcelize
data class EmergencyAlert(
    @SerializedName("alert_id")       val alertId: Int,
    @SerializedName("hospital_id")    val hospitalId: Int,
    @SerializedName("hospital_name")  val hospitalName: String,
    @SerializedName("full_type")      val bloodType: String,
    @SerializedName("units_needed")   val unitsNeeded: Int,
    @SerializedName("urgency_level")  val urgencyLevel: String,
    @SerializedName("status")         val status: String,
    @SerializedName("alerted_at")     val alertedAt: String
) : Parcelable

@Parcelize
data class Notification(
    @SerializedName("notification_id") val notificationId: Int,
    @SerializedName("type")            val type: String,
    @SerializedName("message")         val message: String,
    @SerializedName("sent_at")         val sentAt: String,
    @SerializedName("is_read")         val isRead: Int
) : Parcelable

@Parcelize
data class BloodInventory(
    @SerializedName("hospital_name")    val hospitalName: String,
    @SerializedName("full_type")        val bloodType: String,
    @SerializedName("units_available")  val unitsAvailable: Int,
    @SerializedName("units_reserved")   val unitsReserved: Int,
    @SerializedName("min_threshold")    val minThreshold: Int,
    @SerializedName("stock_status")     val stockStatus: String
) : Parcelable

// ─── Request Bodies ───

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("first_name")    val firstName: String,
    @SerializedName("last_name")     val lastName: String,
    @SerializedName("email")         val email: String,
    @SerializedName("password")      val password: String,
    @SerializedName("phone")         val phone: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("gender")        val gender: String,
    @SerializedName("address")       val address: String,
    @SerializedName("blood_type_id") val bloodTypeId: Int
)

data class BookAppointmentRequest(
    @SerializedName("donor_id")       val donorId: Int,
    @SerializedName("hospital_id")    val hospitalId: Int,
    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("scheduled_time") val scheduledTime: String,
    @SerializedName("donation_type")  val donationType: String,
    @SerializedName("notes")          val notes: String
)
