package com.easyfield.utils

import android.content.Context
import android.content.DialogInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StyleRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showTermsAndConditionDialog(
    @StyleRes style: Int = 0,
    url: String,
    dialogBuilder: MaterialAlertDialogBuilder.() -> Unit
) {
    val webView = WebView(this)
    webView.loadUrl(url)
    webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(url)
            return true
        }
    }
    MaterialAlertDialogBuilder(this, style)
        .apply {
            setCancelable(false)
            setView(webView)
            dialogBuilder()
            create()
            show()
        }
}

fun MaterialAlertDialogBuilder.disagreeButton(
    text: String = "Disagree",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNegativeButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.agreeButton(
    text: String = "Agree",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setPositiveButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}