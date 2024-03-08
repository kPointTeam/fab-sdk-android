package com.fab.sdk.reels

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.StrictMode
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.fab.sdk.constants.AppConstants
import org.json.JSONObject


class ReelsExperience: WebView {
    private var reelsView = ""
    private val reelsSource = ReelsSource()

    private var showTitle: Boolean = true
    private var showDescription: Boolean = true
    private var showAvatar: Boolean = true
    private var showClose: Boolean = true
    private var showComment: Boolean = true
    private var showLike: Boolean = true
    private var showShare: Boolean = true
    private var showUserName: Boolean = true
    private var host: String = ""
    private var appKey: String = ""
    private var reelsDetails = ""
    private var reelsExperienceCallback: ReelsExperienceCallback? = null

    interface ReelsExperienceCallback {
        fun loadNextPage(currentPlayerIndex: Int)
        fun onVideoDetailsFetched(widgets: String) : String
        fun onLikeButtonPressed(like: String) : Boolean
        fun onCommentButtonPressed(comment: String) : Boolean
        fun onShareButtonPressed(share: String) : Boolean
        fun closeReelsExperience(close: String)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun setCallbacks(reelsExperienceCallback: ReelsExperienceCallback) {
        this.reelsExperienceCallback = reelsExperienceCallback
    }


    private fun List<*>.listToString(): String {
        val listString = this.joinToString("\",\"")
        return "[\"$listString\"]"
    }

    fun getReelsConfigDetails(reelsExperienceValues: String) {
        setWebViewValues(reelsExperienceValues)
        reelsView = reelsSource.getReelsView(
            this.reelsDetails,
            this.appKey,
            this.host,
            showTitle,
            showDescription,
            showUserName,
            showAvatar,
            showLike,
            showComment,
            showShare,
            showClose,
        ).trimIndent()

        val reels_view = """
        <html>
        <body>
        $reelsView
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
        setupWebView(reels_view)
    }

    private fun setWebViewValues(createJsonString: String) {
        val jsonObject = JSONObject(createJsonString)
        // Extracting reelsDetails as List<String>
        val reelsDetailsArray = jsonObject.getJSONArray(AppConstants.reelsIds)
        val reelsDetails = mutableListOf<String>()
        for (i in 0 until reelsDetailsArray.length()) {
            reelsDetails.add(reelsDetailsArray.getString(i))
        }

        this.reelsDetails = reelsDetails.listToString()

        showTitle = jsonObject.getBoolean(AppConstants.showTitle)
        showDescription = jsonObject.getBoolean(AppConstants.showDescription)
        showAvatar = jsonObject.getBoolean(AppConstants.showAvatar)
        showClose = jsonObject.getBoolean(AppConstants.showClose)
        showComment = jsonObject.getBoolean(AppConstants.showComment)
        showLike = jsonObject.getBoolean(AppConstants.showLike)
        showShare = jsonObject.getBoolean(AppConstants.showShare)
        showUserName = jsonObject.getBoolean(AppConstants.showUserName)
        host = jsonObject.getString(AppConstants.host)
        appKey = jsonObject.getString(AppConstants.appKey)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(reels_view: String) {
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
        val fabSdkJavaScriptInterface = WebAppInterface( this, this.reelsExperienceCallback)
        // Add a JavaScript interface to communicate with the WebView
        this.addJavascriptInterface(fabSdkJavaScriptInterface, AppConstants.fabJavaScriptName)
        loadUrlWithParameters(reels_view)
    }

    private fun loadUrlWithParameters(url: String) {
        // Assuming you want to pass parameters via URL, modify as needed
        this.loadDataWithBaseURL("https://assets.zencite.com",url, "text/html", "UTF-8",null)
    }

    fun onNextPage(videoIds: JSONObject, activity: Activity, currentPlayerIndex: Int) {
        activity.runOnUiThread {
            val reelsDetailsArray = videoIds.getJSONArray(AppConstants.reelsIds)
            val reelsDetails = mutableListOf<String>()
            for (i in 0 until reelsDetailsArray.length()) {
                reelsDetails.add(reelsDetailsArray.getString(i))
            }

            this.reelsDetails = reelsDetails.listToString()
            this.evaluateJavascript("onNextPageReelsIdsReceived(${reelsDetails.listToString()}, $currentPlayerIndex);"){
            }
        }
    }

    // Class for JavaScript interface
    class WebAppInterface(
        private val webView: WebView,
        private val reelsExperienceCallback: ReelsExperienceCallback?
    ) {
        private var fabWebView = webView

        @JavascriptInterface
        fun onClose(result: String) {
            // Handle the JavaScript message in Kotlin
            if (this.reelsExperienceCallback != null) {
                this.reelsExperienceCallback.closeReelsExperience(result)
            }
        }

        @JavascriptInterface
        fun onMessageReceived(result: String) {
            // Handle the JavaScript message in Kotlin
        }


        @JavascriptInterface
        fun onNextPage(currentPlayerIndex: Int) {
            // Handle onNextPage logic
            if (this.reelsExperienceCallback != null) {
                this.reelsExperienceCallback.loadNextPage(currentPlayerIndex)
            }
        }

        @JavascriptInterface
        fun onLike(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
            if (reelsExperienceCallback?.onLikeButtonPressed(videoIds)!!) {
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onLikeSuccess();");
                }
            }
            else{
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onLikeFailure();");
                }
            }

        }
        @JavascriptInterface
        fun onComment(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
            if (reelsExperienceCallback?.onCommentButtonPressed(videoIds)!!) {
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onCommentSuccess();");
                }
            }else{
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onCommentFailure();");
                }
            }


        }

        @JavascriptInterface
        fun onShare(videoIds: String) {
            // Handle onNextPageStoriesIdsReceived logic
            Log.e("JavascriptInterface", "onShare: ${reelsExperienceCallback?.onShareButtonPressed(videoIds)}")
            if (!reelsExperienceCallback?.onShareButtonPressed(videoIds)!!) {
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onShareFailure();");
                }
            }else{
                fabWebView.post {
                    fabWebView.loadUrl("javascript:onShareFailure();");
                }
            }


        }


        @JavascriptInterface
        fun getVideoConfig(videoId: String) {
            // Handle getVideoConfig logic
        }

        @JavascriptInterface
        fun onWidgetDetailsReceived(videoId: String) {
            // Handle onWidgetDetailsReceived logic
            fabWebView.post {
                fabWebView.loadUrl("javascript:onWidgetDetailsReceived('" + reelsExperienceCallback?.onVideoDetailsFetched(videoId) + "', '" + videoId + "')");
            }

        }

    }
}