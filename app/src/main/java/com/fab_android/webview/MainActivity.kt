package com.fab_android.webview

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.webview.R
import com.fab.sdk.constants.AppConstants
import com.fab.sdk.reels.ReelsExperience
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64
import java.util.Random


class MainActivity : AppCompatActivity(), ReelsExperience.ReelsExperienceCallback {

    private var reelsExperience: ReelsExperience? = null
    private val reelsDetailsArray = JSONArray(listOf(
        "gcc-a80e1d84-bc6a-4bc6-aadf-7e483885b793",
        "gcc-525f6919-1640-4c83-96d6-e6160cfee194",
        "gcc-5f5d855a-f116-44cc-82a1-62b2139a3d5a",
        "gcc-8c0f97d6-8285-48a6-95e1-1aabd8f9911f",
        "gcc-aa4e649e-1d82-4b32-bea5-34ba803d4bee",
        "gcc-119c536f-2ea5-4f5a-9486-b79d281fb72a"
    ))

    private val reelsDetailsArrayNew = JSONArray(listOf(
        "gcc-277e2fbe-7bb6-4f44-9a47-56838d31199e",
        "gcc-3e5c5410-191d-4142-81a3-10693d0fad80",
        "gcc-e6c57d63-fd70-453b-b6a5-1c07bae8aea3",
        "gcc-c1880ac2-4042-46fd-95f2-33f666688567",
        "gcc-8c0f97d6-8285-48a6-95e1-1aabd8f9911f",
        "gcc-5f5d855a-f116-44cc-82a1-62b2139a3d5a"
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        reelsExperience = findViewById<ReelsExperience>(R.id.fabWebView)
        reelsExperience?.setCallbacks(this)
        reelsExperience?.getReelsConfigDetails(createReelsJSONString())

    }

    private fun createReelsJSONString(): String {
        val jsonObject = JSONObject()

        jsonObject.put(AppConstants.showTitle, true)
        jsonObject.put(AppConstants.showDescription, true)
        jsonObject.put(AppConstants.showAvatar, true)
        jsonObject.put(AppConstants.showClose, true)
        jsonObject.put(AppConstants.showComment, true)
        jsonObject.put(AppConstants.showLike, true)
        jsonObject.put(AppConstants.showShare, true)
        jsonObject.put(AppConstants.showUserName, true)
        jsonObject.put(AppConstants.host, "demos.kpoint.com")
        jsonObject.put(AppConstants.appKey, "myAppKey")
        jsonObject.put(AppConstants.reelsIds, reelsDetailsArray)

        return jsonObject.toString()
    }

    override fun onNextPage(currentPlayerIndex: Int) {
//        Log.e("TAG", " Main Activity:->onNextPage: $currentPlayerIndex")
        val reelsDetailsArray = JSONObject()
        reelsDetailsArray.put(AppConstants.reelsIds, this.reelsDetailsArrayNew)
        reelsExperience?.onNextPage(reelsDetailsArray,this,currentPlayerIndex)
    }

    override fun onWidgetDetailsReceived(videoId: String) : String {
        return getBase64ConvertedString(videoId);
    }

    // onLikeClicked
    override fun onLike(like: String) : Boolean {
        Log.e("TAG Demo", "onLike: $like")
        return  false;
    }
    // onCommentClicked
    override fun onComment(comment: String) : Boolean {
        Log.e("TAG", "onComment: $comment")
        return false;
    }

    override fun onShare(share: String) : Boolean {
        Log.e("TAG", "onShare: $share")
        return false;
    }

    override fun onClose(close: String) {
        //Close the reels experience activity and go back to the previous activity
        this.finish()
    }


    private fun getBase64ConvertedString(videoId: String): String {
        // Generate random number
        val likeCount = Random().nextInt(100).toString()
        val commentCount = Random().nextInt(100).toString()
        val shareCount = Random().nextInt(100).toString()
        val videoDetails =
            "likeCount:$likeCount;commentCount:$commentCount;shareCount:$shareCount;kpwVideoId:$videoId"
        // Function converts string into base 64 format
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(videoDetails.toByteArray())
        } else {
            TODO("VERSION.SDK_INT < O")
        }

    }

}


