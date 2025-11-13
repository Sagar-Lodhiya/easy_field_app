package com.easyfield.utils.extension

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment


fun Context.getColor(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, null)
}

fun Fragment.getColor(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, null)
}

fun Context.getDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, null)
}

fun Fragment.getDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, null)
}

fun Fragment.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Activity.hideProgressBar(progressBar: ProgressBar?) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar?.hide()
}

fun Activity.showProgressBar(progressBar: ProgressBar?, boolean: Boolean? = true) {
    if (boolean == true) {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBar?.show()
    } else {
        hideProgressBar(progressBar)
    }
}

fun Fragment.hideProgressBar(progressBar: ProgressBar?) {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar?.hide()
}

/**
 * Extension method to provide show keyboard for View.
 */
fun Fragment.showKeyboard(view: View? = activity?.currentFocus) {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.requestFocus()
    imm.showSoftInput(view, 0)
}

fun Activity.showKeyboard(view: View? = currentFocus) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.requestFocus()
    imm.showSoftInput(view, 0)
}

fun Activity.hideKeyboard(view: View? = currentFocus) {
    if (currentFocus != null) {
        val manager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}


fun Fragment.hideKeyboard(view: View? = activity?.currentFocus) {
    if (activity?.currentFocus != null) {
        val manager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

fun ScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}

fun Fragment.showProgressBar(progressBar: ProgressBar?, boolean: Boolean? = true) {
    if (boolean == true) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBar?.show()
    } else {
        hideProgressBar(progressBar)
    }
}


/**
 * Programmatically shows the soft keyboard.
 */
fun Window.showSoftKeyboard() {
    WindowInsetsControllerCompat(this, decorView.findViewById(R.id.content))
        .show(WindowInsetsCompat.Type.ime())
}

/**
 * Programmatically hides the soft keyboard.
 */
fun Window.hideSoftKeyboard() {
    WindowInsetsControllerCompat(this, decorView.findViewById(R.id.content))
        .hide(WindowInsetsCompat.Type.ime())
}

