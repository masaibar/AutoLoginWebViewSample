package com.masaibar.inputeventcatchtest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val URL_TOP = "https://yoyaku.sports.metro.tokyo.jp/sp/"
    }

    interface WebViewEventListener {
        fun onUserIdFocused()
        fun onPasswordFocused()
        fun onLoginClicked()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web_view.apply {
            webChromeClient = WebChromeClient()
            addJavascriptInterface(
                JsInterface(
                    object : WebViewEventListener {
                        override fun onUserIdFocused() {
                            Log.d("!!!", "onUserIdFocused")
                        }

                        override fun onPasswordFocused() {
                            Log.d("!!!", "onPasswordFocused")
                        }

                        override fun onLoginClicked() {
                            Log.d("!!!", "onLoginClicked")
                        }
                    }),
                "android"
            )
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("!!!", url)
                    if (url != URL_TOP) {
                        button.visibility = View.GONE
                    } else {
                        button.visibility = View.VISIBLE
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
            }
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                lightTouchEnabled = true
            }
            web_view.loadUrl(URL_TOP)
        }

        button.setOnClickListener {
            web_view.evaluateJavascript(
                "javascript:" +
                        "document.getElementsByName('userId')[0].onfocus = function() { android.onUserIdFocused() };" +
                        "document.getElementsByName('password')[0].onfocus = function() { android.onPasswordFocused() };" +
                        "document.getElementsByName('login')[0].onclick = function() { android.onLoginClicked() };" +
                        "document.getElementsByName('password')[0].value = 'password';"
//                        +
//                        "document.getElementsByName('login')[0].click();",
                ,
                null
            )
        }
    }

    private class JsInterface(
        private val webViewEventListener: WebViewEventListener
    ) {

        @JavascriptInterface
        fun onUserIdFocused() {
            webViewEventListener.onUserIdFocused()
        }

        @JavascriptInterface
        fun onPasswordFocused() {
            webViewEventListener.onPasswordFocused()
        }

        @JavascriptInterface
        fun onLoginClicked() {
            webViewEventListener.onLoginClicked()
        }
    }
}
