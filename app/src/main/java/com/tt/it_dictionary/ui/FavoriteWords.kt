package com.tt.it_dictionary.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tt.it_dictionary.R
import com.tt.it_dictionary.adapter.RecyclerViewAdapter
import com.tt.it_dictionary.databinding.ActivityFavoriteWordsBinding
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.viewmodel.WordViewModel

class FavoriteWords : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel
    private lateinit var recyclerView: RecyclerView
    private var listWord: MutableList<Word> = ArrayList()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityFavoriteWordsBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            recyclerView = it.recyclerView
        }
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        recyclerViewAdapter = RecyclerViewAdapter(
            this,
            listWord,
            ResourcesCompat.getColor(resources, R.color.black, null)
        )
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recyclerView.adapter = recyclerViewAdapter

        wordViewModel.getAllFavoriteWord()
        wordViewModel.wordFoundResult.observe(this, Observer {
            listWord.clear()
            listWord.addAll(it)

            recyclerViewAdapter.notifyDataSetChanged()
        })
    }
}
