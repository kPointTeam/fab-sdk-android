package com.fab_android.webview.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fab_android.webview.MainActivity
import com.example.webview.R
import com.fab_android.webview.StoriesActivity

class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
//        val reelsExperience = findViewById<ReelsExperience>(R.id.fabWebView)
//        reelsExperience.getReelsConfigDetails()
    }

    fun onReelsClick(view: View) {
        Intent (this, StoriesActivity::class.java).also {
            startActivity(it)
        }
//        val reelsExperience = ReelsExperience(this)
//        reelsExperience.getReelsConfigDetails()
    }
}