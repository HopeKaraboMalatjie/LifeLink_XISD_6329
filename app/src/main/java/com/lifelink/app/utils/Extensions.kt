package com.lifelink.app.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, duration)
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

fun View.visible() { visibility = View.VISIBLE }
fun View.gone()    { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun String.capitalizeFirst(): String =
    if (isEmpty()) this else this[0].uppercaseChar() + substring(1).lowercase()
