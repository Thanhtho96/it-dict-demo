package com.tt.it_dictionary.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import java.util.*


class WordDetail : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel
    private lateinit var adapter: TabAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var textView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var ttsButton: ImageButton
    private lateinit var checkBox: CheckBox
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityWordDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            viewPager = it.viewPager
            tabLayout = it.tabLayout
            textView = it.english
            backButton = it.backButton
            checkBox = it.checkboxFavoriteWord
            ttsButton = it.tts
        }
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        backButton.setOnClickListener { onBackPressed() }
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

        ttsButton.setOnClickListener {
            tts = TextToSpeech(this, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.speak(intent.getStringExtra("en"), TextToSpeech.QUEUE_ADD, null, "0")
                } else {
                    Toast.makeText(this, "Text-to-speech error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        checkBox.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
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

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts.also {
                it?.stop()
                it?.shutdown()
            }
        }
    }
}
