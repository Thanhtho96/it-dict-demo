package com.tt.it_dictionary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tt.it_dictionary.R
import com.tt.it_dictionary.adapter.RecyclerViewAdapter
import com.tt.it_dictionary.databinding.ActivityMainBinding
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.viewmodel.WordViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    private lateinit var imageButton: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFavorite: Button
    private lateinit var btnSettings: Button
    private var listWord: MutableList<Word> = ArrayList()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val scope = CoroutineScope(SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            recyclerView = it.recyclerView
            editText = it.editText
            imageButton = it.clearButton
            fab = it.fab
            btnFavorite = it.favoriteWord
            btnSettings = it.setting
        }
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        recyclerViewAdapter = RecyclerViewAdapter(
            this,
            listWord,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recyclerView.adapter = recyclerViewAdapter

        imageButton.setOnClickListener {
            editText.text = null
            recyclerView.visibility = View.GONE
            it.visibility = View.GONE
            closeSoftKeyboard(editText)
        }

        fab.setOnClickListener {
            showSoftKeyboard(editText)
        }

        btnFavorite.setOnClickListener {
            startActivity(Intent(this, FavoriteWords::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        editText.addTextChangedListener(object : TextWatcher {
            private var timer: Timer = Timer()
            private val DELAY: Long = 277
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.trim().isNotEmpty()) {
                        imageButton.visibility = View.VISIBLE
                    } else {
                        imageButton.visibility = View.GONE
                    }
                }
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            scope.launch {
                                if (s != null) {
                                    if (s.trim().isNotEmpty()) {
                                        wordViewModel.findWordByEnglish("$s%")
                                        withContext(Dispatchers.Main) {
                                            wordViewModel.wordFoundResult.observe(
                                                this@MainActivity,
                                                androidx.lifecycle.Observer {
                                                    listWord.clear()
                                                    listWord.addAll(it)

                                                    recyclerView.visibility = View.VISIBLE
                                                    recyclerViewAdapter.notifyDataSetChanged()
                                                })
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            recyclerView.visibility = View.GONE
                                        }
                                    }
                                }
                            }
                        }
                    },
                    DELAY
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun closeSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}