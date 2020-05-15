package com.tt.it_dictionary.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tt.it_dictionary.adapter.TabAdapter
import com.tt.it_dictionary.databinding.ActivityWordDetailBinding

class WordDetail : AppCompatActivity() {
    private lateinit var adapter: TabAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var textView: TextView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityWordDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            viewPager = it.viewPager
            tabLayout = it.tabLayout
            textView = it.english
            imageButton = it.backButton
        }

        imageButton.setOnClickListener { onBackPressed() }
        textView.text = intent.getStringExtra("en")

        adapter = TabAdapter(this, intent.getStringExtra("en")!!, intent.getStringExtra("vn")!!)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Meaning"
                1 -> tab.text = "Image"
            }
        }.attach()
    }
}
