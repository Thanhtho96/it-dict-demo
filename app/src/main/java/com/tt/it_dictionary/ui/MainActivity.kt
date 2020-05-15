package com.tt.it_dictionary.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var listWord: MutableList<Word> = ArrayList()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val scope = CoroutineScope(SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            recyclerView = it.recyclerView
            editText = it.editText
        }
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        recyclerViewAdapter = RecyclerViewAdapter(this, listWord)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recyclerView.adapter = recyclerViewAdapter

        editText.addTextChangedListener(object : TextWatcher {
            private var timer: Timer = Timer()
            private val DELAY: Long = 277
            override fun afterTextChanged(s: Editable?) {
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
}
