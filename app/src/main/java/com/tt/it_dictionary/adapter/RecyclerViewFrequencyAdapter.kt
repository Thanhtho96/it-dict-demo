package com.tt.it_dictionary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tt.it_dictionary.databinding.ItemFrequencyBinding
import com.tt.it_dictionary.ui.RemindDialogFragment

class RecyclerViewFrequencyAdapter(
    context: Context,
    private val list: List<String>,
    private val checkedPosition: Int
) :
    RecyclerView.Adapter<RecyclerViewFrequencyAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ItemFrequencyBinding.inflate(layoutInflater, parent, false).also {
            return ViewHolder(it)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string = list[position]
        holder.frequencyName.text = string
        holder.itemView.setOnClickListener {
            holder.radio.isChecked = true
            RemindDialogFragment.chooseFrequency(position)
        }

        holder.radio.setOnClickListener {
            RemindDialogFragment.chooseFrequency(position)
        }

        if (position == checkedPosition) {
            holder.radio.isChecked = true
        }
    }

    class ViewHolder(binding: ItemFrequencyBinding) : RecyclerView.ViewHolder(binding.root) {
        val frequencyName = binding.frequency
        val radio = binding.radio
    }
}