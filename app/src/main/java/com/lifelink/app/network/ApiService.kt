package com.lifelink.app.network

import com.lifelink.app.models.*
import retrofit2.Response
import retrofit2.http.*

interface LifeLinkApiService {

    // Auth
    @POST("auth.php")
    suspend fun login(
        @Query("action") action: String = "login",
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("auth.php")
    suspend fun register(
        @Query("action") action: String = "register",
        @Body body: RegisterRequest
    ): Response<LoginResponse>

    // Donor
    @GET("donor.php")
    suspend fun getDonorProfile(
        @Query("action") action: String = "profile",
        @Query("donor_id") donorId: Int
    ): Response<ApiResponse<Donor>>

    @GET("donor.php")
    suspend fun getDonationHistory(
        @Query("action") action: String = "history",
        @Query("donor_id") donorId: Int
    ): Response<ApiResponse<List<Appointment>>>

    // Appointments
    @GET("appointments.php")
    suspend fun getAppointments(
        @Query("action") action: String = "list",
        @Query("donor_id") donorId: Int,
        @Query("status") status: String? = null
    ): Response<ApiResponse<List<Appointment>>>

    @POST("appointments.php")
    suspend fun bookAppointment(
        @Query("action") action: String = "book",
        @Body body: BookAppointmentRequest
    ): Response<ApiResponse<Any>>

    @POST("appointments.php")
    suspend fun cancelAppointment(
        @Query("action") action: String = "cancel",
        @Query("appointment_id") appointmentId: Int,
        @Query("donor_id") donorId: Int
    ): Response<ApiResponse<Any>>

    // Hospitals
    @GET("hospitals.php")
    suspend fun getHospitals(
        @Query("action") action: String = "list"
    ): Response<ApiResponse<List<Hospital>>>

    // Emergency Alerts
    @GET("alerts.php")
    suspend fun getEmergencyAlerts(
        @Query("action") action: String = "active"
    ): Response<ApiResponse<List<EmergencyAlert>>>

    // Notifications
    @GET("notifications.php")
    suspend fun getNotifications(
        @Query("action") action: String = "list",
        @Query("donor_id") donorId: Int
    ): Response<ApiResponse<List<Notification>>>

    @POST("notifications.php")
    suspend fun markAllRead(
        @Query("action") action: String = "mark_read",
        @Query("donor_id") donorId: Int
    ): Response<ApiResponse<Any>>
}
