package com.fab.sdk.stories

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.fab.sdk.constants.AppConstants
import org.json.JSONObject
import java.util.Base64
import java.util.Random

class StoriesExperience : WebView {
    private val mContext: Context = context
    private var storiesView = ""
    private var storiesSource = StoriesSource()

    private var showTitle: Boolean = true
    private var showDescription: Boolean = true
    private var showAvatar: Boolean = true
    private var showClose: Boolean = true
    private var showComment: Boolean = true
    private var showLike: Boolean = true
    private var showShare: Boolean = true
    private var showUserName: Boolean = true
    private var host: String = "demos.kpoint.com"
    private var appKey: String = "myAppKey"
    private var storiesDetails = ""
    private var storiesExperienceCallback: StoriesExperienceCallback? = null
    /*listOf<String>("gcc-a80e1d84-bc6a-4bc6-aadf-7e483885b793",
    "gcc-525f6919-1640-4c83-96d6-e6160cfee194",
    "gcc-5f5d855a-f116-44cc-82a1-62b2139a3d5a",
    "gcc-8c0f97d6-8285-48a6-95e1-1aabd8f9911f",
    "gcc-aa4e649e-1d82-4b32-bea5-34ba803d4bee",
    "gcc-119c536f-2ea5-4f5a-9486-b79d281fb72a")*/

    interface StoriesExperienceCallback {
        fun onNextPage(currentPlayerIndex: Int)
        fun onVideoDetailsFetched(widgets: String)
        fun onClose(close: String)
    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }


    fun setCallbacks(reelsExperienceCallback: StoriesExperienceCallback) {
        this.storiesExperienceCallback = reelsExperienceCallback
    }


    private fun List<*>.listToString(): String {
        val listString = this.joinToString("\",\"")
        return "[\"$listString\"]"
    }

    fun getStoriesConfigDetails(reelsExpereinceValues: String) {
        setWebviewValues(reelsExpereinceValues)
        Log.e("ReelsExperience", "JSON Array :-$reelsExpereinceValues")

        storiesView = storiesSource.getStoriesView(
            this.storiesDetails,
            this.appKey,
            this.host,
            showClose,
        ).trimIndent()

        val _storiesView = """
        <html>
        <body>
        $storiesView
        </body>
        </html>
        """.trimIndent()

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyDeath()
                .build()
        )
        setupWebView(_storiesView)
    }

    private fun setWebviewValues(createJsonString: String) {
        val jsonObject = JSONObject(createJsonString)
        // Extracting reelsDetails as List<String>
        val reelsDetailsArray = jsonObject.getString("storiesDetails")

        this.storiesDetails = reelsDetailsArray.toString()
        Log.e("reelsDetails", "JSON Array :-${this.storiesDetails}")
        host = jsonObject.getString("host")
        appKey = jsonObject.getString("appKey")
    }

    private fun setupWebView(_storiesView: String) {
        val webSettings: WebSettings = this.settings
        webSettings.javaScriptEnabled = true  // Enable JavaScript
        webSettings.domStorageEnabled = true  // Enable DOM Storage
        webSettings.allowFileAccess = true     // Allow file access
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.mediaPlaybackRequiresUserGesture = false

        this.webChromeClient = object : WebChromeClient() {
            // Handle web chrome client callbacks if needed
        }

        // Create an instance of the JavaScript interface
        val fabSdkJavaScriptInterface = WebAppInterface( this, this.storiesExperienceCallback)
        // Add a JavaScript interface to communicate with the WebView
        this.addJavascriptInterface(fabSdkJavaScriptInterface, "fab_sdk")
        loadUrlWithParameters(_storiesView)
    }

    private fun loadUrlWithParameters(_storiesView: String) {
        // Assuming you want to pass parameters via URL, modify as needed
        this.loadDataWithBaseURL("https://assets.zencite.com",_storiesView, "text/html", "UTF-8",null)
    }
    fun onNextPage(storiesDetails : String, activity: Activity, currentPlayerIndex: Int) {
        activity.runOnUiThread {
            this.evaluateJavascript("onNextPageReelsIdsReceived(${storiesDetails}, $currentPlayerIndex);"){
            }
        }
    }

    // Class for JavaScript interface
    class WebAppInterface(
        private val webView: WebView,
        private val reelsExperienceCallback: StoriesExperienceCallback?
    ) {
        private var fabWebView = webView

        @JavascriptInterface
        fun onClose(result: String) {
            // Handle the JavaScript message in Kotlin
            Log.e("onMessageReceived", "onMessageReceived: $result")
        }

        @JavascriptInterface
        fun onMessageReceived(result: String) {
            // Handle the JavaScript message in Kotlin
            Log.e("onMessageReceived", "onMessageReceived: $result")
        }


        @JavascriptInterface
        fun onNextPage(currentPlayerIndex: Int) {
            // Handle onNextPage logic
            if (this.reelsExperienceCallback != null) {
                this.reelsExperienceCallback.onNextPage(currentPlayerIndex)
            }
            Log.e("TAG", "onNextPage: $currentPlayerIndex")
        }

        @JavascriptInterface
        fun onLike(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
//            fabWebView.post {
//                fabWebView.loadUrl("javascript:onLikeSuccess();");
//            }
        }
        @JavascriptInterface
        fun onComment(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
            Log.e("TAG", "onComment: $videoIds")
            fabWebView.post {
                fabWebView.loadUrl("javascript:onCommentSuccess();");
            }

        }

        @JavascriptInterface
        fun onShare(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
            Log.e("TAG", "onShare: $videoIds")
            fabWebView.post {
                fabWebView.loadUrl("javascript:onShareSuccess();");
            }

        }


        @JavascriptInterface
        fun getVideoConfig(videoId: String) {
            // Handle getVideoConfig logic
            Log.e("TAG", "getVideoConfig: $videoId")
        }

        @JavascriptInterface
        fun onWidgetDetailsReceived(videoId: String) {
            // Handle onWidgetDetailsReceived logic
            fabWebView.post {
                fabWebView.loadUrl("javascript:onWidgetDetailsReceived('" + reelsExperienceCallback?.onVideoDetailsFetched(videoId) + "', '" + videoId + "')");
            }
//            fabWebView.post {
//                fabWebView.loadUrl("javascript:onWidgetDetailsReceived('" + getBase64ConvertedString(videoId) + "', '" + videoId + "')");
//            }

        }
    }
}