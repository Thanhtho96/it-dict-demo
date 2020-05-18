package com.tt.it_dictionary.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tt.it_dictionary.adapter.TabAdapter
import com.tt.it_dictionary.databinding.ActivityWordDetailBinding
import com.tt.it_dictionary.model.FavoriteWord
import com.tt.it_dictionary.viewmodel.WordViewModel

class WordDetail : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel
    private lateinit var adapter: TabAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var textView: TextView
    private lateinit var imageButton: ImageButton
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityWordDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            viewPager = it.viewPager
            tabLayout = it.tabLayout
            textView = it.english
            imageButton = it.backButton
            checkBox = it.checkboxFavoriteWord
        }
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

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

        val wordId = intent.getIntExtra("wordId", 0)
        wordViewModel.findFavoriteWord(wordId)
        wordViewModel.favoriteWord.observe(this, Observer {
            checkBox.isChecked = it.favoriteWord?.wordId != null
        })

        checkBox.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                wordViewModel.insertFavoriteWord(FavoriteWord(wordId))
                Toast.makeText(
                    this,
                    "[${intent?.getStringExtra("en")}] is added to your Favorite Word",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                wordViewModel.deleteFavoriteWord(FavoriteWord(wordId))
                Toast.makeText(
                    this,
                    "[${intent?.getStringExtra("en")}] is removed from your Favorite Word",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
