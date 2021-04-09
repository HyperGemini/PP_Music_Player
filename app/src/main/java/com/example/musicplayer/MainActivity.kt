package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate a ViewPager2 and a PagerAdapter
        viewPager = findViewById(R.id.view_pager2)

        // The pager adapter, which provides the pages to the view pager widget
        val pagerAdapter = FragmentAdapter(this)
        viewPager.adapter = pagerAdapter
    }
}