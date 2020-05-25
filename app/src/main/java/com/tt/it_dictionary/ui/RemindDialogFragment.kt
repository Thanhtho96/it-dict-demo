package com.tt.it_dictionary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tt.it_dictionary.R
import com.tt.it_dictionary.adapter.RecyclerViewFrequencyAdapter
import com.tt.it_dictionary.databinding.FragmentRemindDialogBinding


class RemindDialogFragment : DialogFragment() {
    private var list: MutableList<String> = ArrayList()
    private lateinit var recyclerViewAdapter: RecyclerViewFrequencyAdapter
    private lateinit var recyclerView: RecyclerView
    private var binding: FragmentRemindDialogBinding? = null
    private var frequency: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frequency = arguments?.getInt("frequency")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentContext = this
        FragmentRemindDialogBinding.inflate(layoutInflater, container, false).also {
            recyclerView = it.recyclerView
            binding = it
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = RecyclerViewFrequencyAdapter(
            requireContext(), list, frequency?.minus(1)!!
        )
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.scrollToPosition(frequency?.minus(1)!!)
        for (i in 1..15) {
            list.add(
                resources.getQuantityString(
                    R.plurals.plural_time, i, i
                )
            )
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    companion object {
        private lateinit var fragmentContext: DialogFragment
        private lateinit var onClickListener: OnClickListener

        fun chooseFrequency(position: Int) {
            onClickListener.onItemClick(position + 1)
            fragmentContext.dismiss()
        }

        @JvmStatic
        fun newInstance(frequency: Int) = RemindDialogFragment().apply {
            arguments = Bundle().apply {
                putInt("frequency", frequency)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
