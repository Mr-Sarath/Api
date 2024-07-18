package com.example.api.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.example.api.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.hypot




fun getContrastColor(color: Int): Int { //returns white or black depending on the background color given
    val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
    return if (y >= 128) Color.BLACK else Color.WHITE
}

fun parsePhoneNumber(number: CharSequence): String {
    return number.toString().replace(" ", "").replace("-", "").replace("+", "")
}

/**
 * Handles back press evens inside fragments
 */
fun interface OnBackPressedHandler {
    fun onBackPressed(): Boolean
}


fun String?.toFirstCharUpperCase(): String {
    return this?.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } ?: ""
}

fun CharSequence?.toFirstCharUpperCase(): String {
    return this.toString().toFirstCharUpperCase()
}

fun View?.circularRevealTransition(visibility: Int) {
    this?.let {
        val cx = it.width / 2
        val cy = it.height / 2
        if (visibility == View.VISIBLE) {
            val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(it, cx, cy, 0f, finalRadius)
            it.visibility = View.VISIBLE
            anim.start()
        } else {
            val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim =
                ViewAnimationUtils.createCircularReveal(it, cx, cy, initialRadius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    it.visibility = View.INVISIBLE
                }
            })
            anim.start()
        }
    }
}

fun View?.fadeTransition(visibility: Int, duration: Long = 250) {
    this?.let {
        val transition: Transition = Fade()
        transition.duration = duration
        transition.addTarget(it)
        TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
        it.visibility = visibility
    }
}

fun Context.showValidationError(message: String?) {
    showAlert(null, message.toString(), "Okay", null, null, null)
}

fun Fragment.showValidationError(message: String?) {
    showAlert(null, message.toString(), "Okay", null, null, null)
}

fun Fragment.showAlert(
    title: String?,
    message: String?,
    positiveButtonText: String?,
    negativeButtonText: String?,
    neutralButtonText: String?,
    alertCallback: ((which: Int) -> Unit)?
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNeutralButton(neutralButtonText) { _, _ ->
            alertCallback?.let { it(DialogInterface.BUTTON_NEUTRAL) }
        }
        .setNegativeButton(negativeButtonText) { _, _ ->
            alertCallback?.let { it(DialogInterface.BUTTON_NEGATIVE) }
        }
        .setPositiveButton(positiveButtonText) { _, _ ->
            alertCallback?.let { it(DialogInterface.BUTTON_POSITIVE) }
        }
        .show()
}

fun Context.showAlert(
    title: String?,
    message: String?,
    positiveButtonText: String?,
    negativeButtonText: String?,
    neutralButtonText: String?,
    alertCallback: ((which: Int) -> Unit)?
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setNeutralButton(neutralButtonText) { _, which ->
            alertCallback?.let { it(which) }
        }
        .setNegativeButton(negativeButtonText) { _, which ->
            if (alertCallback != null) {
                alertCallback(which)
            }
        }
        .setPositiveButton(positiveButtonText) { _, which ->
            if (alertCallback != null) {
                alertCallback(which)
            }
        }
        .show()
}


fun Context.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun TextView.highlightText(searchText: String, colorCode: String) {
    val actualText = "$text"
    val startIndex = actualText.indexOf(searchText, 0, true)
    val endIndex = startIndex + searchText.length
    if (startIndex != -1 && endIndex > 0) {
        SpannableString(actualText).apply {
            setSpan(
                ForegroundColorSpan(Color.parseColor(colorCode)),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = this
        }
    }
}

fun View?.hideSoftKeyboard() {
    this?.let {
        val inputMethodManager =
            it.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Int.getPercentage(percentage: Float): Int {
    if (percentage in 0.0..1.0) {
        return (percentage * this).toInt()
    } else {
        throw IllegalArgumentException("Percentage must be between 0 and 1")
    }
}

fun ViewPager2.autoScroll(lifecycleScope: LifecycleOwner, interval: Long) {
    lifecycleScope.lifecycleScope.launchWhenResumed {
        scrollIndefinitely(interval)
    }
}

fun ViewPager2.autoScroll(interval: Long) {
    CoroutineScope(Dispatchers.IO).launch {
        scrollIndefinitely(interval)
    }
}

fun ViewHolder.shortToast(message: Any?) {
    itemView.context.shortToast("$message")
}

private suspend fun ViewPager2.scrollIndefinitely(interval: Long) {
    try {
        while (true) {
            delay(interval)
            withContext(Dispatchers.Main) {
                val numberOfItems = adapter?.itemCount ?: 0
                if (numberOfItems > 0) {
                    val nextItem = (currentItem + 1) % numberOfItems
                    setCurrentItem(nextItem, true)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun logThis(message: Any?, tag: String = "logThis") {
    Log.d(tag, "-->  $message")
}

fun <T> T?.toLogThis(extraMessage: String = ""): T? {
    logThis("$extraMessage:  $this")
    return this
}


private var previousToast = ""

private fun Toast.toastCallback() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        addCallback(
            object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    previousToast = ""
                }
            })
    } else previousToast = ""
}

fun Context.longToast(message: String?) {
    if (previousToast != (message ?: "")) {
        previousToast = message ?: ""
        Toast.makeText(this, message ?: "null", Toast.LENGTH_LONG).apply {
            toastCallback()
            show()
        }
    }
}

fun Context.shortToast(message: String?) {
    if (previousToast != (message ?: "")) {
        previousToast = message ?: ""
        Toast.makeText(this, message ?: "null", Toast.LENGTH_SHORT).apply {
            toastCallback()
            show()
        }
    }
}

fun Fragment.longToast(message: String?) {
    if (previousToast != (message ?: "")) {
        previousToast = message ?: ""
        Toast.makeText(requireContext(), message ?: "null", Toast.LENGTH_LONG).apply {
            toastCallback()
            show()
        }
    }
}

fun Fragment.shortToast(message: String?) {
    if (previousToast != (message ?: "")) {
        previousToast = message ?: ""
        Toast.makeText(requireContext(), message ?: "null", Toast.LENGTH_SHORT).apply {
            toastCallback()
            show()
        }
    }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.appBackSnack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
    snack.show()
}


fun EditText.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    doAfterTextChanged { query.value = it.toString() }

    return query

}

fun parseProductCount(count: Any?): String {
    return count.toString().toIntOrNull()?.let { if (it == 1) "$it item" else "$it items" } ?: ""
}

