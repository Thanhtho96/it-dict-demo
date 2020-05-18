package com.tt.it_dictionary.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tt.it_dictionary.databinding.ItemRecyclerViewBinding
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.ui.WordDetail

class RecyclerViewAdapter(private var context: Context, private var listWord: MutableList<Word>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ItemRecyclerViewBinding.inflate(layoutInflater, parent, false).also {
            return ViewHolder(it)
        }
    }

    override fun getItemCount(): Int {
        return listWord.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = listWord[position]
        holder.wordName.text = word.en

        holder.itemView.setOnClickListener {
            Intent(context, WordDetail::class.java).also {
                it.putExtra("en", word.en)
                it.putExtra("vn", word.vn)
                it.putExtra("wordId", word.id)
                context.startActivity(it)
            }
        }
    }

    class ViewHolder(binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val wordName = binding.wordName
    }
}