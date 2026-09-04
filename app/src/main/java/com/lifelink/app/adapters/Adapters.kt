package com.lifelink.app.adapters

import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lifelink.app.R
import com.lifelink.app.models.*

// ─── Base List Adapter ───────────────────────────────────────────────────────

abstract class BaseListAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    protected var items: List<T> = emptyList()
) : RecyclerView.Adapter<VH>() {

    fun updateItems(newItems: List<T>, diffCallback: (List<T>, List<T>) -> DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(diffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size
}

// ─── Appointment Adapter ───────────────────────────────────────────────────────

class AppointmentAdapter(
    items: List<Appointment>,
    private val onCancel: (Appointment) -> Unit
) : BaseListAdapter<Appointment, AppointmentAdapter.VH>(items) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvHospital: TextView    = view.findViewById(R.id.tvHospital)
        val tvDate: TextView        = view.findViewById(R.id.tvDate)
        val tvTime: TextView        = view.findViewById(R.id.tvTime)
        val tvType: TextView        = view.findViewById(R.id.tvType)
        val tvStatus: TextView      = view.findViewById(R.id.tvStatus)
        val btnCancel: Button       = view.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val appt = items[position]
        holder.tvHospital.text = appt.hospitalName ?: "Hospital"
        holder.tvDate.text = "📅 ${appt.scheduledDate}"
        holder.tvTime.text = "🕐 ${appt.scheduledTime}"
        holder.tvType.text = appt.donationType
        holder.tvStatus.text = appt.status

        val statusColor = when (appt.status) {
            "Confirmed"  -> Color.parseColor("#17a2b8")
            "Completed"  -> Color.parseColor("#28a745")
            "Cancelled"  -> Color.parseColor("#dc3545")
            else         -> Color.parseColor("#ffc107")
        }
        holder.tvStatus.setTextColor(statusColor)

        if (appt.status == "Confirmed" || appt.status == "Pending") {
            holder.btnCancel.visibility = View.VISIBLE
            holder.btnCancel.setOnClickListener { onCancel(appt) }
        } else {
            holder.btnCancel.visibility = View.GONE
        }
    }
}

// ─── Hospital Adapter ─────────────────────────────────────────────────────────

class HospitalAdapter(
    items: List<Hospital>,
    private val onMapClick: (Hospital) -> Unit
) : BaseListAdapter<Hospital, HospitalAdapter.VH>(items) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView     = view.findViewById(R.id.tvHospitalName)
        val tvAddress: TextView  = view.findViewById(R.id.tvAddress)
        val tvPhone: TextView    = view.findViewById(R.id.tvPhone)
        val tvHours: TextView    = view.findViewById(R.id.tvHours)
        val tvTypes: TextView    = view.findViewById(R.id.tvBloodTypes)
        val btnMap: Button       = view.findViewById(R.id.btnMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val h = items[position]
        holder.tvName.text    = h.hospitalName
        holder.tvAddress.text = "📍 ${h.address}"
        holder.tvPhone.text   = if (h.contactPhone != null) "📞 ${h.contactPhone}" else ""
        holder.tvHours.text   = if (h.operatingHours != null) "🕐 ${h.operatingHours}" else ""
        holder.tvTypes.text   = if (!h.availableTypes.isNullOrEmpty()) "🩸 ${h.availableTypes}" else "No stock data"
        holder.btnMap.setOnClickListener { onMapClick(h) }
    }
}

// ─── Alert Adapter ────────────────────────────────────────────────────────────

class AlertAdapter(
    items: List<EmergencyAlert>
) : BaseListAdapter<EmergencyAlert, AlertAdapter.VH>(items) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvBloodType: TextView  = view.findViewById(R.id.tvBloodType)
        val tvHospital: TextView   = view.findViewById(R.id.tvHospital)
        val tvUnits: TextView      = view.findViewById(R.id.tvUnits)
        val tvUrgency: TextView    = view.findViewById(R.id.tvUrgency)
        val card: CardView         = view.findViewById(R.id.cardAlert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alert, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val alert = items[position]
        holder.tvBloodType.text = alert.bloodType
        holder.tvHospital.text  = alert.hospitalName
        holder.tvUnits.text     = "Needs: ${alert.unitsNeeded} units"
        holder.tvUrgency.text   = alert.urgencyLevel

        val bgColor = when (alert.urgencyLevel) {
            "Critical" -> Color.parseColor("#FFE0E0")
            "High"     -> Color.parseColor("#FFF3E0")
            else       -> Color.parseColor("#E8F5E9")
        }
        holder.card.setCardBackgroundColor(bgColor)
    }
}

// ─── Notification Adapter ─────────────────────────────────────────────────────

class NotificationAdapter(
    items: List<Notification>
) : BaseListAdapter<Notification, NotificationAdapter.VH>(items) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvDate: TextView    = view.findViewById(R.id.tvDate)
        val tvType: TextView    = view.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = items[position]
        holder.tvMessage.text = n.message
        holder.tvDate.text    = n.sentAt.take(16)
        holder.tvType.text    = n.type
    }
}
