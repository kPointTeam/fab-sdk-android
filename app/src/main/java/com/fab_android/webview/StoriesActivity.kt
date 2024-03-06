package com.fab_android.webview
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.webview.R
import com.fab.sdk.stories.StoriesExperience
import org.json.JSONObject

class StoriesActivity : AppCompatActivity(), StoriesExperience.StoriesExperienceCallback {

    private var storiesExperience: StoriesExperience? = null
    private val storiesResponse = """
      [
        {
          "categoryName": "smartPhone",
          "videoIds": [
            "gcc-9e1b7226-513b-4388-8de7-ac953bcd3155",
            "gcc-a80e1d84-bc6a-4bc6-aadf-7e483885b793",
            "gcc-5252454c-06b0-497b-aaa4-7e7312a27e0c"
          ]
        },
        {
          "categoryName": "laptop",
          "videoIds": [
            "gcc-f8ea0a03-a57c-4a3b-a5a4-21a617b08d50",
            "gcc-97a74ef1-35b0-40d7-a535-28bce3add347",
            "gcc-8bf821d6-16bc-4e95-90b3-226c837615a1"
          ]
        },
        {
          "categoryName": "monitors",
          "videoIds": [
            "gcc-198d1270-4cef-480e-a98f-97ac5ebb3e71",
            "gcc-c639a37f-df5f-4ad5-97b4-ebf5b1ce040a"
          ]
        }
      ]
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        storiesExperience = findViewById(R.id.fabStoriesView)
        storiesExperience?.setCallbacks(this)
        storiesExperience?.getStoriesConfigDetails(createJsonString())
    }
    private fun createJsonString(): String {
        val jsonObject = JSONObject()

        jsonObject.put("host", "demos.kpoint.com")
        jsonObject.put("appKey", "myAppKey")

//        val reelsDetailsArray = JSONArray()
        jsonObject.put("storiesDetails", storiesResponse)

        return jsonObject.toString()
    }

    override fun onNextPage(currentPlayerIndex: Int) {
        Log.e("TAG", "onNextPage: $currentPlayerIndex")
        if (currentPlayerIndex == 2) {
            storiesExperience?.updateReelsUI(storiesResponse)
        }
    }

    override fun onNextPageReelsIdsReceived(videoIds: List<String>, currentPlayerIndex: Int) {
        Log.e("TAG", "onNextPageReelsIdsReceived: $currentPlayerIndex")
    }

    override fun onReelsViewError(error: String) {
        Log.e("TAG", "onReelsViewError: $error")
    }

    override fun onWidgetDetailsReceived(widgets: String) {
        Log.e("TAG", "onWidgetDetailsReceived: $widgets")
    }

    override fun onLike(like: String) {
        Log.e("TAG Demo", "onLike: $like")
        storiesExperience?.updateReelsUI(like)
    }

    override fun onComment(comment: String) {
        Log.e("TAG", "onComment: $comment")
    }

    override fun onShare(share: String) {
        Log.e("TAG", "onShare: $share")
    }

    override fun onClose(close: String) {
        Log.e("TAG", "Stories Activity onClose: $close")

    }

    override fun getConfigDetails(config: String) {
        Log.e("TAG", "getConfigDetails: $config")
    }

}